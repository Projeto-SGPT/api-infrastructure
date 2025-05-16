import com.br.sgpt.domain.entity.User;
import com.br.sgpt.domain.repository.UserRepository;
import com.br.sgpt.domain.services.AuthService;
import com.br.sgpt.infrastructure.service.AuthServiceImpl;
import com.br.sgpt.infrastructure.usecase.AuthUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthUseCaseImplTest {

    private UserRepository userRepository;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;
    private AuthUseCaseImpl authUseCase;

    @BeforeEach
    void setUp() {
        this.userRepository = mock(UserRepository.class);

        this.authService = new AuthServiceImpl();
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.authUseCase = new AuthUseCaseImpl(this.userRepository, this.authService, this.passwordEncoder);
    }

    @Test
    void testLoginAndCredentials() {
        var username = "user";
        var password = "password";
        var email = "teste@teste.com";
        var role = "user";

        var encodedPassword = this.passwordEncoder.encode(password);

        var user = new User(UUID.randomUUID(), username, encodedPassword, email, role);

        when(this.userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        var token = this.authUseCase.login(username, password);

        var userFromToken = this.authService.extractUser(token);

        assertEquals(user.getId(), userFromToken.getId());
    }

    @Test
    void testLoginWithInvalidUsername() {
        when(this.userRepository.findByUsername("invalid")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                this.authUseCase.login("invalid", "password"));

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void testLoginWithInvalidPassword() {
        var username = "user";
        var password = "password";
        var invalidPassword = "invalidPasswrod";
        var email = "teste@teste.com";
        var role = "user";

        var encodedPassword = this.passwordEncoder.encode(password);

        var user = new User(UUID.randomUUID(), username, encodedPassword, email, role);

        when(this.userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                this.authUseCase.login(username, invalidPassword));

        assertEquals("Senha inválida", exception.getMessage());
    }

    @Test
    void testRegisterSavesUser() {
        var username = "user";
        var password = "password";
        var email = "test@test.com";
        var role = "user";

        var encodedPassword = this.passwordEncoder.encode(password);

        this.authUseCase.register(username, password, email, role);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(this.userRepository).save(captor.capture());

        var captorUse = captor.getValue();

        assertEquals(username, captorUse.getUsername());
        assertEquals(email, captorUse.getEmail());
        assertEquals(role, captorUse.getRole());
        assertNotNull(captorUse.getId());
    }
}