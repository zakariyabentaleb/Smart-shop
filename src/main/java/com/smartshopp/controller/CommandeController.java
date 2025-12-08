package com.smartshopp.controller;

import com.smartshopp.dto.CommandeDTO;
import com.smartshopp.enums.Role;
import com.smartshopp.exception.AccessDeniedException;
import com.smartshopp.exception.ResourceNotFoundException;
import com.smartshopp.model.Client;
import com.smartshopp.repository.ClientRepository;
import com.smartshopp.service.CommandeService;
import com.smartshopp.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;
    private final ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<CommandeDTO> createOrder(
            @RequestBody CommandeDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        CommandeDTO responseDTO = commandeService.createOrder(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CommandeDTO>> getClientHistory(
            @PathVariable Long clientId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        if(session == null){
            throw new AccessDeniedException("Vous devez être connecté.");
        }

        Object userIdObj = session.getAttribute("USER_ID");
        Object userRoleObj = session.getAttribute("USER_ROLE");

        if (userIdObj == null) {
            throw new AccessDeniedException("Vous devez être connecté.");
        }

        // If the current user is a CLIENT, ensure they can only view their own history
        try {
            Role currentUserRole = userRoleObj == null ? null : Role.valueOf(userRoleObj.toString());
            if (currentUserRole == Role.CLIENT) {
                Client targetClient = clientRepository.findById(clientId)
                        .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

                Long currentUserId = Long.valueOf(userIdObj.toString());
                if (!targetClient.getUser().getId().equals(currentUserId)) {
                    throw new AccessDeniedException("Vous n'avez pas le droit de voir les commandes d'un autre client.");
                }
            }
        } catch (IllegalArgumentException ignored) {
            // If role value is invalid, deny access
            throw new AccessDeniedException("Vous n'êtes pas autorisé.");
        }

        List<CommandeDTO> history = commandeService.getOrdersByClient(clientId);

        if(history.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(history);
        }

        return ResponseEntity.ok(history);
    }
    @PutMapping("{id}/confirm")
    public ResponseEntity<CommandeDTO> confirmOrderAfterPayment(
            @PathVariable("id") Long id,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        CommandeDTO responseDTO = commandeService.confirmOrderAfterCompletingPayment(id);

        return ResponseEntity.ok(responseDTO);
    }
}