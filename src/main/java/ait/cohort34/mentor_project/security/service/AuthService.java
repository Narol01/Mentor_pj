package ait.cohort34.mentor_project.security.service;

import ait.cohort34.mentor_project.accounting.dto.UserAuthDto;
import ait.cohort34.mentor_project.accounting.model.UserAccount;
import ait.cohort34.mentor_project.accounting.service.UserService;
import ait.cohort34.mentor_project.security.AuthInfo;
import ait.cohort34.mentor_project.security.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    final private UserService userService;
    final private TokenService tokenService;
    final private Map<String, String> refreshStorage;
    final private PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshStorage = new HashMap<>();
    }

    public TokenResponseDto login(@NonNull UserAuthDto inboundUser) throws AuthenticationException {
        String username = inboundUser.getLogin();
        UserAccount foundUser = (UserAccount) userService.loadUserByUsername(username);
        if (passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(username, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    public TokenResponseDto getAccessToken(String refreshToken) throws AuthenticationException {
        if (tokenService.validateAccessToken(refreshToken)) {
            Claims refreshClaims = tokenService.getRefreshClaims(refreshToken);
            String username = refreshClaims.getSubject();
            String savedRefreshToken = refreshStorage.get(username);
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                UserAccount foundUser = (UserAccount) userService.loadUserByUsername(username);
                String accessToken = tokenService.generateAccessToken(foundUser);
                return new TokenResponseDto(accessToken, null);
            }
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    public AuthInfo getAuthInfo() {
        return (AuthInfo) SecurityContextHolder.getContext().getAuthentication();
    }

}

