package com.teamapp.gospy.services;

import com.teamapp.gospy.helperobjects.Credentials;
import com.teamapp.gospy.helperobjects.Role;
import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
public class AuthServiceRedisUserWebLogin {

    private static Logger logger = LoggerFactory.getLogger(AuthServiceRedisUserWebLogin.class);
    private static final PasswordEncoder BCRYPT = new BCryptPasswordEncoder();
    private static final Base64.Decoder B64_DECODER = Base64.getDecoder();
    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    UserRepository userRepo;

    public Optional<Authentication> authenticate(HttpServletRequest request) {
        return extractUserAuthCredentials(request).flatMap(this::check);
    }

    private Optional<Authentication> check(Credentials credentials) {
        try {
            logger.warn("checking credentials " + credentials.getUsername() + " " + credentials.getPassword());
            Optional<User> user = this.userRepo.findOneByUsername(credentials.getUsername());
            if (user.isPresent()) {
                if (credentials.getPassword().compareTo(user.get().getPassword()) == 0) {
                    Authentication authentication = createAuthentication(credentials.getUsername(), credentials.getPassword(), Role.ADMIN);
                    return Optional.of(authentication);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.warn("Unknown error while trying to check User Auth credentials", e);
            return Optional.empty();
        }
    }

    private static Optional<Credentials> extractUserAuthCredentials(@NonNull HttpServletRequest request) {
        try {
            //System.out.println("AuthServiceRedisUserWebLogin : Request type : " + request.getMethod() + " " + request.getAuthType() + " " + request.getRequestURI());

            if (request.getMethod().compareTo(HttpMethod.POST.name()) == 0
                    && request.getRequestURI().compareTo("/login") == 0) {
                for (Cookie cookie : request.getCookies()) {
                    //System.out.println("Cookie " + cookie.getName() + " : " + cookie.getValue());
                }
                //System.out.println("username " + request.getParameter("username"));
                //System.out.println("password " + request.getParameter("password"));


                Credentials credentials = new Credentials(request.getParameter("username"), request.getParameter("password"));
                return Optional.of(credentials);
            }

            return Optional.empty();
        } catch (Exception e) {
            logger.error("An unknown error occurred while trying to extract user credentials", e);
            return Optional.empty();
        }
    }

    private static Authentication createAuthentication(String actor, String password, @NonNull Role... roles) {
        // The difference between `hasAuthority` and `hasRole` is that the latter uses the `ROLE_` prefix
        List<GrantedAuthority> authorities = Stream.of(roles)
                .distinct()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(toList());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(nonNull(actor) ? actor : "N/A", nonNull(password) ? password : "N/A", authorities);
        return authToken;
    }

}
