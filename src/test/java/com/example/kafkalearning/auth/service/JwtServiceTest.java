package com.example.kafkalearning.auth.service;

import com.example.kafkalearning.auth.domain.AppUser;
import com.example.kafkalearning.auth.domain.UserRole;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    @Test
    void generateToken_shouldContainExpectedClaims() throws Exception {
        JwtService jwtService = new JwtService("12345678901234567890123456789012", 15);

        AppUser user = new AppUser();
        user.setUsername("naveen");
        user.setEmail("naveen@example.com");
        user.setRole(UserRole.ADMIN);

        String token = jwtService.generateToken(user);

        assertNotNull(token);

        SignedJWT parsed = SignedJWT.parse(token);

        assertEquals("naveen", parsed.getJWTClaimsSet().getSubject());
        assertEquals("naveen@example.com", parsed.getJWTClaimsSet().getStringClaim("email"));
        assertEquals("ADMIN", parsed.getJWTClaimsSet().getStringListClaim("roles").get(0));
        assertTrue(parsed.getJWTClaimsSet().getExpirationTime().after(parsed.getJWTClaimsSet().getIssueTime()));
    }
}

