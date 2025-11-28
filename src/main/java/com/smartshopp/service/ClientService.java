package com.smartshopp.service;


import org.mindrot.jbcrypt.BCrypt;
import com.smartshopp.dto.ClientDTO;
import com.smartshopp.dto.UserDTO;
import com.smartshopp.enums.Role;
import com.smartshopp.enums.CustomerTier;
import com.smartshopp.enums.StatutCommande;
import com.smartshopp.mapper.ClientMapper;
import com.smartshopp.mapper.UserMapper;
import com.smartshopp.model.Client;
import com.smartshopp.model.Commande;
import com.smartshopp.model.User;
import com.smartshopp.repository.ClientRepository;
import com.smartshopp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;

    @Transactional
    public ClientDTO createClient(ClientDTO dto) {
        if (userRepository.existsByUsername(dto.getUser().getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Client client = clientMapper.toEntity(dto);
        User user = client.getUser();

        String hashedPassword = BCrypt.hashpw(dto.getUser().getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        user.setRole(Role.CLIENT);

        client.setNiveauFidelite(CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.valueOf(0));

        client.setUser(user);
        Client savedClient = clientRepository.save(client);

        return clientMapper.toDTO(savedClient);
    }

    @Transactional
    public ClientDTO updateClient(Long clientId, ClientDTO newClient){
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new RuntimeException("Aucun Client avec id: "+clientId)
        );
        // Map fields from newClient to existing client. Only overwrite when DTO fields are non-null
        if (newClient.getNom() != null) client.setNom(newClient.getNom());
        if (newClient.getEmail() != null) client.setEmail(newClient.getEmail());

        if (newClient.getTotalOrders() != null) {
            client.setTotalOrders(newClient.getTotalOrders());
        }

        if (newClient.getTotalSpent() != null) {
            client.setTotalSpent(BigDecimal.valueOf(newClient.getTotalSpent()));
        }

        // update user info if provided
        if (newClient.getUser() != null) {
            UserDTO u = newClient.getUser();
            if (client.getUser() == null) {
                User userEntity = userMapper.toEntity(u);
                client.setUser(userEntity);
            } else {
                if (u.getUsername() != null) client.getUser().setUsername(u.getUsername());
                // do not overwrite password here
            }
        }

        Client savedClient = clientRepository.save(client);

        return clientMapper.toDTO(savedClient);
    }

    @Transactional
    public ClientDTO clientProfile(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new RuntimeException("Aucun Client avec id: " + clientId)
        );
        return clientMapper.toDTO(client);
    }

    public void deleteClient(Long clientId){
        clientRepository.deleteById(clientId);
    }

    @Transactional
    public void updateClientStatistics(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<Commande> confirmedOrders = client.getCommandes().stream()
                .filter(order -> order.getStatut() == StatutCommande.CONFIRMED)
                .toList();

        client.setTotalOrders(confirmedOrders.size());

        BigDecimal totalSpent = confirmedOrders.stream()
                .map(order -> BigDecimal.valueOf(order.getTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        client.setTotalSpent(totalSpent);

        if (!confirmedOrders.isEmpty()) {
            Optional<LocalDate> firstDate = confirmedOrders.stream()
                    .map(Commande::getDate)
                    .min(Comparator.naturalOrder());

            Optional<LocalDate> lastDate = confirmedOrders.stream()
                    .map(Commande::getDate)
                    .max(Comparator.naturalOrder());

            client.setFirstOrderDate(firstDate.orElse(null));
            client.setLastOrderDate(lastDate.orElse(null));
        } else {
            client.setFirstOrderDate(null);
            client.setLastOrderDate(null);
        }

        updateLoyaltyTier(client);

        clientRepository.save(client);
    }

    private void updateLoyaltyTier(Client client) {
        int count = client.getTotalOrders();
        BigDecimal spent = client.getTotalSpent();

        if (count >= 20 || spent.compareTo(new BigDecimal("15000")) >= 0) {
            client.setNiveauFidelite(CustomerTier.PLATINUM);
        } else if (count >= 10 || spent.compareTo(new BigDecimal("5000")) >= 0) {
            client.setNiveauFidelite(CustomerTier.GOLD);
        } else if (count >= 3 || spent.compareTo(new BigDecimal("1000")) >= 0) {
            client.setNiveauFidelite(CustomerTier.SILVER);
        } else {
            client.setNiveauFidelite(CustomerTier.BASIC);
        }
    }
}
