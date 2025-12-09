package com.smartshopp.service;

import com.smartshopp.dto.CommandeDTO;
import com.smartshopp.dto.CommandeLigneDTO;
import com.smartshopp.enums.CustomerTier;
import com.smartshopp.enums.StatutCommande;
import com.smartshopp.exception.ResourceNotFoundException;
import com.smartshopp.mapper.CommandeMapper;
import com.smartshopp.model.*;
import com.smartshopp.repository.ClientRepository;
import com.smartshopp.repository.CommandeRepository;
import com.smartshopp.repository.ProductRepository;
import com.smartshopp.repository.PromoCodeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository orderRepository;
    private final CommandeMapper orderMapper;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final PromoCodeRepository promoCodeRepository;

    @Transactional
    public CommandeDTO createOrder(CommandeDTO dto){
        Commande order = orderMapper.toEntity(dto);

        Client client = clientRepository.findById(dto.getClientId()).orElseThrow(() ->
                new ResourceNotFoundException("Aucun client avec id :" + dto.getClientId())
        );

        double promoCode = 0;

        if(dto.getCodePromo() != null){
            PromoCode pc = promoCodeRepository.findPromoCodeByCode(dto.getCodePromo()).orElseThrow(() ->
                    new ResourceNotFoundException("Aucun promo code avec code: "+dto.getCodePromo())
            );
            if(pc.isActive()){
                promoCode = pc.getPercentageRemise();
            }else{
                throw new RuntimeException("promo code is expired! try another one!");
            }
        }

        double sub_total = 0;
        List<CommandeLigne> orderItems = new ArrayList<>();

        if (dto.getLignes() != null) {
            for(CommandeLigneDTO item : dto.getLignes()){
                Product product = productRepository.findById(item.getProduitId()).orElseThrow(
                        () -> new ResourceNotFoundException("Aucun produit avec id: "+item.getProduitId())
                );

                if(item.getQuantite() > product.getStockDisponible()){
                    order.setStatut(StatutCommande.REJECTED);
                    throw new RuntimeException("Quantity est plus élevé que nos stock disponible!");
                }

                double lineTotal = product.getPrixUnitaire() * item.getQuantite();

                sub_total = sub_total + lineTotal;

                CommandeLigne orderItem = CommandeLigne.builder()
                        .commande(order)
                        .produit(product)
                        .quantite(item.getQuantite())
                        .prixTotal(lineTotal)
                        .build();

                orderItem.setProduit(product);

                orderItems.add(orderItem);
                order.setLignes(orderItems);
            }
        }

        decrementStock(order);

        double loyalityDiscount = calculatingLoyalityDiscount(client);

        double finalDiscount = sub_total * ((promoCode / 100.0) + (loyalityDiscount / 100.0));

        double tax_base = sub_total - finalDiscount;

        double tva = tax_base * 0.2;

        double finalTotal = tax_base + tva;

        // set values on commande (adapt to fields present in Commande)
        order.setRemise(finalDiscount);
        // store promo code as string if needed
        order.setCodePromo(dto.getCodePromo());
        order.setTva(tva);
        order.setSousTotal(sub_total);
        order.setClient(client);
        order.setTotal(finalTotal);
        order.setDate(LocalDate.now());

        Commande savedOrder =  orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<CommandeDTO> getOrdersByClient(Long clientId) {
        List<Commande> orders = orderRepository.findAllByClient_Id(clientId);

        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void decrementStock(Commande order) {
        if (order.getLignes() == null) return;
        for (CommandeLigne item : order.getLignes()) {
            Product product = productRepository.findById(item.getProduit().getId()).orElseThrow(() ->
                    new ResourceNotFoundException("aucun produit avec id :" + item.getProduit().getId())
            );

            if (product.getStockDisponible() < item.getQuantite()) {
                throw new RuntimeException("Stock changed! Insufficient stock for: " + product.getNom());
            }

            int newStock = product.getStockDisponible() - item.getQuantite();
            product.setStockDisponible(newStock);

            productRepository.save(product);
        }
    }

    @Transactional
    public double calculatingLoyalityDiscount(Client client){
        if(client.getNiveauFidelite() == null) return 0;
        if(client.getNiveauFidelite().equals(CustomerTier.BASIC)){
            return 0;
        }else if(client.getNiveauFidelite().equals(CustomerTier.SILVER)){
            return 5;
        }else if (client.getNiveauFidelite().equals(CustomerTier.GOLD)){
            return 10;
        }else if (client.getNiveauFidelite().equals(CustomerTier.PLATINUM)){
            return 15;
        }
        return 0;
    }
    @Transactional
    public CommandeDTO confirmOrderAfterCompletingPayment(Long orderId){
        Commande commande = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("aucun order avec id : "+orderId)
        );

        Double montantRestant = commande.getMontantRestant();
        if(montantRestant != null && montantRestant != 0){
            throw new RuntimeException("can't confirme order, because it is not full paid");
        }

        commande.setStatut(StatutCommande.CONFIRMED);
        clientService.updateClientStatistics(commande.getClient().getId());
        Commande savedCommande = orderRepository.save(commande);

        return orderMapper.toDTO(savedCommande);
    }
}
