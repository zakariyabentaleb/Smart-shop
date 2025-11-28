package com.smartshopp.service;



import com.smartshopp.dto.PaymentDTO;
import com.smartshopp.exception.ResourceNotFoundException;
import com.smartshopp.mapper.PaymentMapper;
import com.smartshopp.model.Commande;
import com.smartshopp.model.Payment;
import com.smartshopp.enums.StatutPaiement;
import com.smartshopp.enums.TypePaiement;
import com.smartshopp.repository.CommandeRepository;
import com.smartshopp.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CommandeRepository orderRepository;
    private final PaymentMapper paymentMapper;

    private final double ESPECES_LIMIT = 20000;

    @Transactional
    public PaymentDTO payOrder(PaymentDTO dto){
        Commande commande = orderRepository.findById(dto.getCommandeId()).orElseThrow(
                () -> new ResourceNotFoundException("Aucun order avec id: "+dto.getCommandeId())
        );

        Payment payment = paymentMapper.toEntity(dto);

        payment.setStatut(StatutPaiement.EN_ATTENTE);

        int nextPaymentNumber = paymentRepository.countByCommande_Id(dto.getCommandeId()) + 1;
        payment.setNumeroPaiement(nextPaymentNumber);

        if(isOrderFullPaid(commande)){
            throw new RuntimeException("Order is Full paid!");
        }

        if(commande.getMontantRestant() != null && commande.getMontantRestant() < payment.getMontant()){
            throw new RuntimeException("price you gived is more than montant restant!");
        }

        // switch on the entity enum (mapped from dto)
        TypePaiement typePaiement = payment.getTypePaiement();
        switch (typePaiement){
            case ESPECES -> {
                if(payment.getMontant() >= ESPECES_LIMIT){
                    throw new RuntimeException("You have reached the limite of espece pay! our especes limite is: "+ESPECES_LIMIT);
                }
                payment.setStatut(StatutPaiement.ENCAISSE);
                payment.setReference("RECU-"+ UUID.randomUUID().toString().subSequence(0, 5));
            }
            case CHEQUE -> {
                if(payment.getEcheance() == null){
                    throw new RuntimeException("You should enter a valid echeance Date in CHEQUE types!");
                }

                LocalDate dateEch = payment.getEcheance();

                if(!dateEch.isAfter(LocalDate.now())){
                    throw new RuntimeException("You should enter a valid echeance date after today!");
                }
                payment.setStatut(StatutPaiement.ENCAISSE);
                payment.setReference("CHQ-"+ UUID.randomUUID().toString().subSequence(0, 5));
            }
            case VIREMENT -> {
                payment.setStatut(StatutPaiement.ENCAISSE);
                payment.setReference("VIR-"+ UUID.randomUUID().toString().subSequence(0, 7));
            }
        }

        payment.setCommande(commande);
        payment.setDatePaiement(LocalDateTime.now());

        Payment savedPayment =  paymentRepository.save(payment);

        calculatingOrderMontantRestant(commande, savedPayment);

        return paymentMapper.toDTO(savedPayment);
    }

    public boolean isOrderFullPaid(Commande commande){
        // If Commande doesn't have montantRestant set, treat as not full paid
        Double montantRestant = null;
        try {
            montantRestant = commande.getMontantRestant();
        } catch (Exception ignored) {}
        return montantRestant != null && montantRestant == 0;
    }

    @Transactional
    public void calculatingOrderMontantRestant(Commande commande, Payment savedPayment){
        Double current = commande.getMontantRestant();
        if(current == null) current = 0.0;
        commande.setMontantRestant(current - savedPayment.getMontant());
        orderRepository.save(commande);
    }
}
