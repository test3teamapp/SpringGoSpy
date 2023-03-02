package com.teamapp.gospy.services;

import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
public class AuthServiceRedisUser {

    private static Logger logger = LoggerFactory.getLogger(AuthServiceRedisUser.class);
    private static final PasswordEncoder BCRYPT = new BCryptPasswordEncoder();
    private static final Base64.Decoder B64_DECODER = Base64.getDecoder();
    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    UserRepository userRepo;

    public Optional<Authentication> authenticate(HttpServletRequest request) {
        return extractUserAuthHeader(request).flatMap(this::check);
    }

    private Optional<Authentication> check(Credentials credentials) {
        try {
            logger.warn("checking credentials " + credentials.getUsername() + " " + credentials.password);
            Optional<User> user = this.userRepo.findByName(credentials.getUsername());
            if (user.isPresent()) {
                if (credentials.getPassword().compareTo(user.get().getPass()) == 0) {
                    Authentication authentication = createAuthentication(credentials.getUsername(), Role.ADMIN);
                    return Optional.of(authentication);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.warn("Unknown error while trying to check User Auth credentials", e);
            return Optional.empty();
        }
    }

    private static Optional<Credentials> extractUserAuthHeader(@NonNull HttpServletRequest request) {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            //debug
            logger.info("Http Authorization Header = " + authorization);
            if (nonNull(authorization)) {
                if (authorization.startsWith(BASIC_PREFIX)) {
                    String encodedCredentials = authorization.substring(BASIC_PREFIX.length());
                    String decodedCredentials = new String(B64_DECODER.decode(encodedCredentials), UTF_8);
                    logger.info("Http credentials decoded = " + decodedCredentials);
                    if (decodedCredentials.contains(":")) {
                        String[] split = decodedCredentials.split(":", 2);
                        Credentials credentials = new Credentials(split[0], split[1]);
                        return Optional.of(credentials);
                    }
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("An unknown error occurred while trying to extract user credentials", e);
            return Optional.empty();
        }
    }

    private static Authentication createAuthentication(String actor, @NonNull Role... roles) {
        // The difference between `hasAuthority` and `hasRole` is that the latter uses the `ROLE_` prefix
        List<GrantedAuthority> authorities = Stream.of(roles)
                .distinct()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(toList());
        return new UsernamePasswordAuthenticationToken(nonNull(actor) ? actor : "N/A", "N/A", authorities);
    }


    protected static class Credentials {
        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    private enum Role {
        USER,
        ADMIN,
        SYSTEM,
    }

}
