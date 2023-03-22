package com.teamapp.gospy.services;

import com.teamapp.gospy.helperobjects.Role;
import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
public class AuthServiceRedisToken {

    private static Logger logger = LoggerFactory.getLogger(AuthServiceRedisToken.class);

    @Autowired
    UserRepository userRepo;


    private static final String TOKEN_PREFIX = "ACCESSTOKEN=";

    public Optional<Authentication> authenticate(HttpServletRequest request) {
        return extractTokenFromHeader(request).flatMap(this::lookup);
    }

    private Optional<Authentication> lookup(String token) {

        if (token.compareTo("") == 0 || token.compareTo("loggedout") == 0) return Optional.empty();

        try {
            Optional<User> user = this.userRepo.findOneByToken(token);
            if (!user.isEmpty()) {
                if (user.get().getAuthorities() != null) {
                    List<Role> roles =  user.get().getAuthorities().stream()
                            .map(authority -> authority.getRole())
                            .collect(toList());
                    Authentication authentication = createAuthentication(user.get().getId(), roles );
                    return Optional.of(authentication);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.warn("Unknown error while trying to look up Redis token", e);
            return Optional.empty();
        }
    }

    private static Optional<String> extractTokenFromHeader(@NonNull HttpServletRequest request) {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            //debug
            logger.info("Http Authorization Header = " + authorization);
            if (nonNull(authorization)) {
                if (authorization.startsWith(TOKEN_PREFIX)) {
                    String token = authorization.substring(TOKEN_PREFIX.length()).trim();
                    if (!token.isBlank()) {
                        return Optional.of(token);
                    }
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("An unknown error occurred while trying to extract access token", e);
            return Optional.empty();
        }
    }

    private static Authentication createAuthentication(String actor, @NonNull List<Role> roles) {
        // The difference between `hasAuthority` and `hasRole` is that the latter uses the `ROLE_` prefix
        List<GrantedAuthority> authorities = roles.stream()
                .distinct()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(toList());
        return new UsernamePasswordAuthenticationToken(nonNull(actor) ? actor : "N/A", "N/A", authorities);
    }


}
