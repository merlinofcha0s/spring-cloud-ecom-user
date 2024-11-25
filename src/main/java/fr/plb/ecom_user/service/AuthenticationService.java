package fr.plb.ecom_user.service;

import fr.plb.ecom_user.entity.AuthorityEntity;
import fr.plb.ecom_user.entity.UserEntity;
import fr.plb.ecom_user.exceptions.EmailAlreadyUsedException;
import fr.plb.ecom_user.repository.AuthorityRepository;
import fr.plb.ecom_user.repository.UserRepository;
import fr.plb.ecom_user.resource.dto.UserDTO;
import fr.plb.ecom_user.shared.authentication.application.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static fr.plb.ecom_user.configuration.security.SecurityUtils.USER;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity registerUser(UserDTO userDTO, String password) {
        Optional<UserEntity> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyUsedException();
        }

        UserEntity newUser = new UserEntity();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageURL(userDTO.getImageUrl());
        newUser.setPublicId(UUID.randomUUID());

        Set<AuthorityEntity> authorities = new HashSet<>();
        authorityRepository.findById(USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        UserEntity savedUser = userRepository.save(newUser);
        log.info("Created Information for User: {}", savedUser);
        return savedUser;
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getConnectedUser() {
        return userRepository.findOneByEmailIgnoreCase(AuthenticatedUser.username().get());
    }
}
