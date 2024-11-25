package fr.plb.ecom_user.configuration.security;

import fr.plb.ecom_user.entity.AuthorityEntity;
import fr.plb.ecom_user.entity.UserEntity;
import fr.plb.ecom_user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Authenticating {}", email);

        if(email.isEmpty()) {
            throw new UsernameNotFoundException("User with email " + email + " was not found in the database");
        }

        return userRepository.findOneByEmailIgnoreCase(email)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(UserEntity user) {
        List<SimpleGrantedAuthority> grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(AuthorityEntity::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
