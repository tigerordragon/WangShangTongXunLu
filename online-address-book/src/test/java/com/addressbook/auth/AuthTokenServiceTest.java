package com.addressbook.auth;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AuthTokenServiceTest {
    private final Clock clock = Clock.fixed(Instant.parse("2026-06-02T10:00:00Z"), ZoneOffset.UTC);

    @Test
    public void issueTokenPairReturnsVerifiableAccessTokenAndStoredRefreshToken() {
        InMemoryRefreshTokenStore store = new InMemoryRefreshTokenStore();
        AuthTokenService service = new AuthTokenService("test-secret", store, clock);

        AuthTokenPair pair = service.issueTokenPair(7L);

        assertNotNull(pair.getAccessToken());
        assertNotNull(pair.getRefreshToken());
        assertEquals(Long.valueOf(7L), service.verifyAccessToken(pair.getAccessToken()).get().getStudentId());
        assertTrue(store.find(pair.getRefreshToken()).isPresent());
    }

    @Test
    public void refreshAccessTokenReturnsNewAccessTokenForValidRefreshToken() {
        InMemoryRefreshTokenStore store = new InMemoryRefreshTokenStore();
        AuthTokenService service = new AuthTokenService("test-secret", store, clock);
        AuthTokenPair pair = service.issueTokenPair(9L);

        Optional<String> refreshed = service.refreshAccessToken(pair.getRefreshToken());

        assertTrue(refreshed.isPresent());
        assertEquals(Long.valueOf(9L), service.verifyAccessToken(refreshed.get()).get().getStudentId());
    }

    @Test
    public void refreshAccessTokenRejectsRevokedRefreshToken() {
        InMemoryRefreshTokenStore store = new InMemoryRefreshTokenStore();
        AuthTokenService service = new AuthTokenService("test-secret", store, clock);
        AuthTokenPair pair = service.issueTokenPair(11L);

        service.revokeRefreshToken(pair.getRefreshToken());

        assertFalse(service.refreshAccessToken(pair.getRefreshToken()).isPresent());
    }
}
