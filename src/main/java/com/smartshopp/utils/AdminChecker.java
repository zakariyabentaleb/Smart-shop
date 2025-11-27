package com.smartshopp.utils;

import com.smartshopp.enums.Role;
import com.smartshopp.exception.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AdminChecker {

    public static boolean isAdmin(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        String roleStr = (String) session.getAttribute("USER_ROLE");
        if (roleStr == null) return false;

        Role userRole = Role.valueOf(roleStr); // conversion String → Enum
        return userRole == Role.ADMIN;
    }

    public static void checkAdminAccess(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AccessDeniedException("You must be logged in.");
        }

        String roleStr = (String) session.getAttribute("USER_ROLE");
        if (roleStr == null) {
            throw new AccessDeniedException("Access Denied: No role assigned.");
        }

        Role role = Role.valueOf(roleStr); // conversion String → Enum
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Access Denied: Only Admins can manage products.");
        }
    }
}


