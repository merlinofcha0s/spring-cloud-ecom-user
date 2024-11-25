package fr.plb.ecom_user.resource;

import fr.plb.ecom_user.entity.UserEntity;
import fr.plb.ecom_user.resource.dto.JWTTokenDTO;
import fr.plb.ecom_user.resource.dto.LoginDTO;
import fr.plb.ecom_user.resource.dto.RestUser;
import fr.plb.ecom_user.resource.dto.UserDTO;
import fr.plb.ecom_user.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class AuthenticationResource {

    private final JwtEncoder jwtEncoder;

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    public AuthenticationResource(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager,
                                  AuthenticationService authenticationService) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registerAccount(@Valid @RequestBody UserDTO newUser) {
        authenticationService.registerUser(newUser, newUser.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTTokenDTO> authorize(@Valid @RequestBody LoginDTO loginDTO) throws InterruptedException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));

        String jwt = createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
//        Thread.sleep(1000);
        return new ResponseEntity<>(new JWTTokenDTO(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<RestUser> getAuthenticatedUser() {
        Optional<UserEntity> authenticatedUser = authenticationService.getConnectedUser();
        RestUser restUser = RestUser.from(authenticatedUser.orElseThrow());
        return ResponseEntity.ok(restUser);
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity = now.plus(86400, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim(SecurityUtils.AUTHORITIES_KEY, authorities)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
