import com.br.sgpt.domain.entity.User;
import com.br.sgpt.domain.services.AuthService;
import com.br.sgpt.infrastructure.service.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private AuthService jwtService;

    @BeforeEach
    void setUp() {
        this.jwtService = new AuthServiceImpl();
    }

    @Test
    void shouldGenerateAndExtractUserFromToken() {
        UUID userId = UUID.randomUUID();

        User originalUser = new User(
                userId,
                "usuario",
                "senha",
                "teste@teste.com",
                "admin"
        );

        String token = this.jwtService.generateToken(originalUser);
        User extractedUser = this.jwtService.extractUser(token);

        assertEquals(originalUser.getUsername(), extractedUser.getUsername());
        assertEquals(originalUser.getEmail(), extractedUser.getEmail());
        assertEquals(originalUser.getRole(), extractedUser.getRole());
        assertEquals(originalUser.getId(), extractedUser.getId());
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        String invalidToken = "token-invalido";

        assertThrows(Exception.class, () -> jwtService.extractUser(invalidToken));
    }
}