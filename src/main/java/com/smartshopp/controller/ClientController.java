package com.smartshopp.controller;

import com.smartshopp.dto.ClientDTO;
import com.smartshopp.service.ClientService;
import com.smartshopp.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

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
}
