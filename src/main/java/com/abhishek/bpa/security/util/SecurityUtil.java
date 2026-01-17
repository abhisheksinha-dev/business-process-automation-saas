package com.abhishek.bpa.security.util;

import com.abhishek.bpa.security.AuthenticatedUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    private SecurityUtil(){}

    /**
     * Check if request is authenticated (JWT present & valid)
     */
    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

    /**
     * Get logged-in user (safe)
     */
    public static Optional<AuthenticatedUser> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof AuthenticatedUser user) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    /**
     * Get current user's role
     */
    public static Optional<String> getCurrentUserRole() {
        return getCurrentUser().map(AuthenticatedUser::getRole);
    }

    /**
     * Get current organization (tenant)
     */
    public static Optional<String> getCurrentOrganizationId() {
        return getCurrentUser().map(u -> u.getOrganizationId().toString());
    }
}
