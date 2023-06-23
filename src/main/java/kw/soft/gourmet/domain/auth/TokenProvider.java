package kw.soft.gourmet.domain.auth;

import kw.soft.gourmet.application.dto.auth.TokenPayload;

public interface TokenProvider {
    public AuthTokens createAuthTokens(TokenPayload payload);

    public AccessToken createAccessToken(final TokenPayload payload);

    public RefreshToken createRefreshToken(final TokenPayload payload);
}
