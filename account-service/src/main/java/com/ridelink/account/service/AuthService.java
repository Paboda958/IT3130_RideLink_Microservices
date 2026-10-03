package com.ridelink.account.service;

import com.ridelink.account.config.JwtTokenProvider;
import com.ridelink.account.dto.AuthResponse;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.UserProfileDto;
import com.ridelink.account.exception.BadRequestException;
import com.ridelink.account.model.AccountStatus;
import com.ridelink.account.model.UserAccount;
import com.ridelink.account.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final SequenceGenerator sequenceGenerator;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager,
                       SequenceGenerator sequenceGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.sequenceGenerator = sequenceGenerator;
    }

    public UserProfileDto registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered!");
        }

        UserAccount account = new UserAccount();
        account.setId(sequenceGenerator.nextId("user_accounts"));
        account.setFullName(request.getFullName());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setPhone(request.getPhone());
        account.setRole(request.getRole());
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(account.getCreatedAt());

        UserAccount saved = userRepository.save(account);
        return mapToDto(saved);
    }

    public AuthResponse loginUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserAccount user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid user details"));

        if (user.getStatus() == AccountStatus.SUSPENDED || user.getStatus() == AccountStatus.INACTIVE) {
            throw new BadRequestException("Account is " + user.getStatus());
        }

        String token = tokenProvider.generateToken(authentication);
        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }

    private UserProfileDto mapToDto(UserAccount user) {
        return new UserProfileDto(
                user.getId(), user.getFullName(), user.getEmail(),
                user.getPhone(), user.getRole(), user.getStatus(), user.getCreatedAt()
        );
    }
}
