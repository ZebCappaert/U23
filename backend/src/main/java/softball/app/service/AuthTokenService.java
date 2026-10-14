
package softball.app.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    private static final long TOKEN_LIFETIME_SECONDS = 8 * 60 * 60;

    private final byte[] secret;

    public AuthTokenService(
            @Value("${app.auth.token-secret}") String secret) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);

        if (this.secret.length < 32) {
            throw new IllegalArgumentException(
                    "AUTH_TOKEN_SECRET moet minstens 32 bytes lang zijn.");
        }
    }

    public String createToken(Long userId) {
        long expiresAt = Instant.now().getEpochSecond()
                + TOKEN_LIFETIME_SECONDS;

        String payload = "v1." + userId + "." + expiresAt;
        return payload + "." + sign(payload);
    }

    public Long validateToken(String token) {
        if (token == null) {
            return null;
        }

        try {
            String[] parts = token.split("\\.", -1);

            if (parts.length != 4 || !"v1".equals(parts[0])) {
                return null;
            }

            String payload = parts[0] + "." + parts[1] + "." + parts[2];
            byte[] expected = sign(payload)
                    .getBytes(StandardCharsets.US_ASCII);
            byte[] supplied = parts[3]
                    .getBytes(StandardCharsets.US_ASCII);

            if (!MessageDigest.isEqual(expected, supplied)) {
                return null;
            }

            long userId = Long.parseLong(parts[1]);
            long expiresAt = Long.parseLong(parts[2]);

            if (userId <= 0 || Instant.now().getEpochSecond() >= expiresAt) {
                return null;
            }

            return userId;
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private String sign(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));

            byte[] signature = mac.doFinal(
                    payload.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(signature);
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Token kon niet ondertekend worden.", ex);
        }
    }
}
