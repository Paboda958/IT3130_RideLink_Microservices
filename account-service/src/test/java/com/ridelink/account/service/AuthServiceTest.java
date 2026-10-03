package com.ridelink.account.service;

import com.ridelink.account.config.JwtTokenProvider;
import com.ridelink.account.dto.AuthResponse;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.UserProfileDto;
import com.ridelink.account.exception.BadRequestException;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.model.Role;
import com.ridelink.account.model.UserAccount;
import com.ridelink.account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("John Doe", "john@example.com", "password123", "+123456789", Role.ROLE_PASSENGER);
        userAccount = new UserAccount(1L, "John Doe", "john@example.com", "encoded_password", "+123456789", Role.ROLE_PASSENGER, AccountStatus.ACTIVE);
    }

    @Test
    void registerUser_Success() {
        when(sequenceGenerator.nextId("user_accounts")).thenReturn(1L);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(UserAccount.class))).thenReturn(userAccount);

        UserProfileDto result = authService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(Role.ROLE_PASSENGER, result.getRole());
    }

    @Test
    void registerUser_DuplicateEmail_ThrowsBadRequestException() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.registerUser(registerRequest));
    }

    @Test
    void loginUser_Success() {
        LoginRequest loginRequest = new LoginRequest("john@example.com", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userAccount));
        when(tokenProvider.generateToken(authentication)).thenReturn("jwt_token");

        AuthResponse response = authService.loginUser(loginRequest);

        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
        assertEquals("john@example.com", response.getEmail());
    }
}
