package kw.soft.gourmet.application.auth;

import static org.assertj.core.api.Assertions.assertThat;

import kw.soft.gourmet.application.dto.auth.TokenPayload;
import kw.soft.gourmet.common.fixtures.AuthFixtures;
import kw.soft.gourmet.domain.auth.AccessToken;
import kw.soft.gourmet.domain.auth.AuthTokens;
import kw.soft.gourmet.domain.auth.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtTokenProviderTest {
    private static final String secretKey = "S".repeat(32);
    private static final long accessTokenValidExpirationInMilliSeconds = 3600;
    private static final long refreshTokenValidExpirationInMilliSeconds = 3600;

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            secretKey,
            accessTokenValidExpirationInMilliSeconds,
            refreshTokenValidExpirationInMilliSeconds);

    @Test
    @DisplayName("AuthToken을 생성한다.")
    public void createAuthTokens() throws Exception{
        //given
        TokenPayload payload = TokenPayload.from(AuthFixtures.MEMBER_WITH_ID);
        AuthTokens expect = new AuthTokens(AuthFixtures.ACCESS_TOKEN, AuthFixtures.REFRESH_TOKEN);

        //when
        AuthTokens result = jwtTokenProvider.createAuthTokens(payload);

        //then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("accessToken을 생성한다.")
    public void createAccessToken() throws Exception {
        //given
        TokenPayload payload = TokenPayload.from(AuthFixtures.MEMBER_WITH_ID);

        //when
        AccessToken atk = jwtTokenProvider.createAccessToken(payload);

        //then
        assertThat(atk.value().split("\\.").length).isEqualTo(3);
    }

    @Test
    @DisplayName("refreshToken을 생성한다.")
    public void createRefreshToken() throws Exception {
        //given
        TokenPayload payload = TokenPayload.from(AuthFixtures.MEMBER_WITH_ID);

        //when
        RefreshToken rtk = jwtTokenProvider.createRefreshToken(payload);

        //then
        assertThat(rtk.value().split("\\.").length).isEqualTo(3);
    }
}
