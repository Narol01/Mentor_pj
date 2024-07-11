package ait.cohort34.mentor_project.accounting.controller;

import ait.cohort34.mentor_project.accounting.dto.RegisterDto;
import ait.cohort34.mentor_project.accounting.dto.UserDto;
import ait.cohort34.mentor_project.accounting.dto.exception.UserExistsException;
import ait.cohort34.mentor_project.accounting.service.UserAccountService;
import ait.cohort34.mentor_project.security.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class UserAccountController {
final UserAccountService userAccountService;
    final AuthService authService;
    final ObjectMapper objectMapper;
    @PostMapping
    public ResponseEntity<String> register(
            @RequestPart("registerDto") String registerDto) throws IOException {
        try {
        RegisterDto userRegisterDto = objectMapper.readValue(registerDto, RegisterDto.class);
        userAccountService.register(userRegisterDto);
            return ResponseEntity.ok("User registered successfully.");
        } catch (UserExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userAccountService.getUsers();
    }

    @GetMapping
    public UserDto getUser() {
        return userAccountService.getUser((String) authService.getAuthInfo().getPrincipal());
    }
    @DeleteMapping("/user/{id}")
    public UserDto removeUser(@PathVariable Long id) {
        return userAccountService.removeUser(id);
    }

    @PutMapping("/user/{id}/role")
    public boolean changeRole(@PathVariable Long id) {
        return userAccountService.changeRole(id);
    }
}
