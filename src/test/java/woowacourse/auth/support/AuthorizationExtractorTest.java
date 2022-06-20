package woowacourse.auth.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import woowacourse.auth.exception.InvalidTokenException;

class AuthorizationExtractorTest {

    private static final String SECRET_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 360000L);

    @Test
    @DisplayName("Authorization 헤더가 없으면, 예외를 발생한다.")
    void doesNotIncludeAuthorizationHeaderException() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(null);

        assertThatThrownBy(() -> AuthorizationExtractor.extract(request))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰이 Bearer로 시작하지 않으면, 예외를 발생한다.")
    void doesNotStartWithBearerException() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final String type = "Basic ";
        final String token = type + jwtTokenProvider.createToken("username");

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(token);

        assertThatThrownBy(() -> AuthorizationExtractor.extract(request))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰이 올바르다면, 토큰을 추출한다.")
    void extractToken() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final String type = "Bearer ";
        final String token = type + jwtTokenProvider.createToken("username");

        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(token);

        assertThat(type + AuthorizationExtractor.extract(request)).isEqualTo(token);
    }
}
