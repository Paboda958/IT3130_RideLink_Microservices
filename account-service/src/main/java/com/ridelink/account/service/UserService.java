package com.ridelink.account.service;

import com.ridelink.account.dto.StatusUpdateRequest;
import com.ridelink.account.dto.UserProfileDto;
import com.ridelink.account.exception.ResourceNotFoundException;
import com.ridelink.account.model.Role;
import com.ridelink.account.model.UserAccount;
import com.ridelink.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDto getUserById(Long id) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDto(user);
    }

    public UserProfileDto getUserByEmail(String email) {
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToDto(user);
    }

    public List<UserProfileDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserProfileDto> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserProfileDto updateProfile(Long id, UserProfileDto dto) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        user.setUpdatedAt(LocalDateTime.now());

        UserAccount updated = userRepository.save(user);
        return mapToDto(updated);
    }

    public UserProfileDto updateStatus(Long id, StatusUpdateRequest request) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setStatus(request.getStatus());
        user.setUpdatedAt(LocalDateTime.now());
        UserAccount updated = userRepository.save(user);
        return mapToDto(updated);
    }

    private UserProfileDto mapToDto(UserAccount user) {
        return new UserProfileDto(
                user.getId(), user.getFullName(), user.getEmail(),
                user.getPhone(), user.getRole(), user.getStatus(), user.getCreatedAt()
        );
    }
}
