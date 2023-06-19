package kw.soft.gourmet.application.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import kw.soft.gourmet.application.dto.auth.TokenPayload;
import kw.soft.gourmet.domain.auth.AccessToken;
import kw.soft.gourmet.domain.auth.AuthTokens;
import kw.soft.gourmet.domain.auth.RefreshToken;
import kw.soft.gourmet.domain.auth.TokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("jwt")
public class JwtTokenProvider implements TokenProvider {
    private final SecretKey key;
    private final long accessTokenValidExpirationInMilliSeconds;
    private final long refreshTokenValidExpirationInMilliSeconds;

    public JwtTokenProvider(@Value("${security.jwt.secret-key}")final String secret,
                            @Value("${security.jwt.access-expiration-time}")final long accessTokenValidExpirationInMilliSeconds,
                            @Value("${security.jwt.refresh-expiration-time}")final long refreshTokenValidExpirationInMilliSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidExpirationInMilliSeconds = accessTokenValidExpirationInMilliSeconds;
        this.refreshTokenValidExpirationInMilliSeconds = refreshTokenValidExpirationInMilliSeconds;
    }

    public AuthTokens createAuthTokens(TokenPayload payload) {
        return new AuthTokens(
                createAccessToken(payload),
                createRefreshToken(payload)
        );
    }

    public AccessToken createAccessToken(final TokenPayload payload) {
        return new AccessToken(createToken(payload, accessTokenValidExpirationInMilliSeconds));
    }

    public RefreshToken createRefreshToken(final TokenPayload payload) {
        return new RefreshToken(createToken(payload, refreshTokenValidExpirationInMilliSeconds));
    }

    private String createToken(final TokenPayload payload, final long expirationTimeInMilliSeconds) {
        Date now = new Date();
        Date expiryTime = new Date(now.getTime() + expirationTimeInMilliSeconds);

        return Jwts.builder()
                .setSubject(String.valueOf(payload.id()))
                .setClaims(createClaims(payload))
                .setIssuedAt(now)
                .setExpiration(expiryTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims createClaims(final TokenPayload payload) {
        Claims claims = Jwts.claims();
        claims.put("email", payload.email().getValue());
        claims.put("authority", payload.authority());

        return claims;
    }
}
