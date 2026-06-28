package travel.winwin.authapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import travel.winwin.authapi.domain.user.User;
import travel.winwin.authapi.domain.user.UserRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        extractToken(request)
                .filter(jwtService::isValid)
                .ifPresent(token -> authenticate(token, request));

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(header.substring(BEARER_PREFIX.length()));
    }

    private void authenticate(String token, HttpServletRequest request) {
        UUID userId = jwtService.extractUserId(token);

        userRepository.findById(userId).ifPresent(user -> {
            setAuthentication(user, request);
        });
    }

    private void setAuthentication(User user, HttpServletRequest request) {
        UserPrincipal principal = new UserPrincipal(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}