package com.smartshopp.service;

import com.smartshopp.dto.ClientDTO;
import com.smartshopp.dto.UserDTO;
import com.smartshopp.enums.CustomerTier;
import com.smartshopp.enums.Role;
import com.smartshopp.mapper.ClientMapper;
import com.smartshopp.mapper.UserMapper;
import com.smartshopp.model.Client;
import com.smartshopp.model.User;
import com.smartshopp.repository.ClientRepository;
import com.smartshopp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
     private final ClientRepository clientRepository ;
    private final ClientMapper clientMapper;

    public UserDTO login(String username, String password) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if(!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");

        }
        return userMapper.toDTO(user);
    }

    @Transactional
    public ClientDTO register(ClientDTO dto) {
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

        client.setUser(user);
        client.setNiveauFidelite(CustomerTier.BASIC);
        client.setTotalOrders(0);

        client = clientRepository.save(client);

        return clientMapper.toDTO(client);
    }

}