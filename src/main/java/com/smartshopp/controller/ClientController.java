package com.smartshopp.controller;

import com.smartshopp.dto.ClientDTO;
import com.smartshopp.service.ClientService;
import com.smartshopp.utils.AdminChecker;
import com.smartshopp.exception.AccessDeniedException;
import com.smartshopp.exception.ResourceNotFoundException;
import com.smartshopp.enums.Role;
import com.smartshopp.model.Client;
import com.smartshopp.repository.ClientRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(
            @RequestBody @Valid ClientDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        ClientDTO newClient = clientService.createClient(dto);

        return ResponseEntity.ok(newClient);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable("id") Long id,
            @RequestBody ClientDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        ClientDTO responseDTO = clientService.updateClient(id, dto);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable("id") Long id,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        clientService.deleteClient(id);

        return ResponseEntity.ok().build();
    }
    @GetMapping("/profile")
    public ResponseEntity<ClientDTO> ClientProfile(
            HttpServletRequest request
    ){

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AccessDeniedException("You must be logged in.");
        }

        Object userIdObj = session.getAttribute("USER_ID");
        if (userIdObj == null) {
            throw new AccessDeniedException("You must be logged in.");
        }

        Long userIdLong;
        try {
            userIdLong = Long.valueOf(userIdObj.toString());
        } catch (NumberFormatException ex) {
            throw new AccessDeniedException("Invalid user id in session.");
        }

        Object roleObj = session.getAttribute("USER_ROLE");
        Role user_role = null;
        try {
            if (roleObj != null) user_role = Role.valueOf(roleObj.toString());
        } catch (IllegalArgumentException ignored) {
            // ignore invalid role value
        }

        if (user_role == Role.ADMIN) {
            throw new AccessDeniedException("Just client who can see profiles!");
        }

        Client client = clientRepository.findByUser_Id(userIdLong);
        if (client == null) {
            throw new ResourceNotFoundException("Client introuvable pour l'utilisateur: " + userIdLong);
        }

        ClientDTO responseDTO = clientService.clientProfile(client.getId());

        return ResponseEntity.ok(responseDTO);
    }
}
