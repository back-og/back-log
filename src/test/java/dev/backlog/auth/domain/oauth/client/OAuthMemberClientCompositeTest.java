package dev.backlog.auth.domain.oauth.client;

import dev.backlog.auth.domain.oauth.OAuthProvider;
import dev.backlog.auth.domain.oauth.dto.OAuthInfoResponse;
import dev.backlog.common.exception.InvalidAuthException;
import dev.backlog.user.domain.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthMemberClientCompositeTest {

    @Mock
    private OAuthMemberClient client;

    @Mock
    private Set<OAuthMemberClient> clients;

    @InjectMocks
    private OAuthMemberClientComposite composite;

    @Test
    void fetch() {
        String authCode = "authCode";
        OAuthProvider oAuthProvider = OAuthProvider.KAKAO;
        OAuthInfoResponse response = new OAuthInfoResponse(
                "nickname",
                "profileImage",
                new Email("test123@gmail.com"),
                OAuthProvider.KAKAO,
                "123L"
        );

        when(client.oAuthProvider()).thenReturn(OAuthProvider.KAKAO);
        when(client.fetch(authCode)).thenReturn(response);

        clients = new HashSet<>(Collections.singletonList(client));
        composite = new OAuthMemberClientComposite(clients);

        OAuthInfoResponse expectedResponse = composite.fetch(oAuthProvider, authCode);

        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void getClientFail() {
        String authCode = "authCode";
        when(client.oAuthProvider()).thenReturn(OAuthProvider.KAKAO);

        clients = new HashSet<>(Collections.singletonList(client));
        composite = new OAuthMemberClientComposite(clients);

        assertThatThrownBy(() -> composite.fetch(OAuthProvider.GITHUB, authCode))
                .isInstanceOf(InvalidAuthException.class);
    }

}
