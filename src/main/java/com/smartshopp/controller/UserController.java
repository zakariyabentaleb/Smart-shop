package com.smartshopp.controller;

import com.smartshopp.dto.ClientDTO;
import com.smartshopp.dto.UserDTO;
import com.smartshopp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(
            @RequestBody @Valid UserDTO loginRequest,
            HttpServletRequest request
    ) {
        UserDTO user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        HttpSession session = request.getSession();
        session.setAttribute("USER_ID", user.getId());
        session.setAttribute("USER_ROLE", user.getRole());

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<ClientDTO> register(
            @RequestBody @Valid ClientDTO clientRequest,
            HttpServletRequest request
    ) {
        ClientDTO saved = userService.register(clientRequest);

        // set session using created user info if present
        if (saved != null && saved.getUser() != null) {
            HttpSession session = request.getSession();
            session.setAttribute("USER_ID", saved.getUser().getId());
            session.setAttribute("USER_ROLE", saved.getUser().getRole());
        }

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }
}
