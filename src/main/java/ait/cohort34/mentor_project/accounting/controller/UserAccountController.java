package ait.cohort34.mentor_project.accounting.controller;

import ait.cohort34.mentor_project.accounting.dto.RegisterDto;
import ait.cohort34.mentor_project.accounting.dto.TeacherRegisterDto;
import ait.cohort34.mentor_project.accounting.dto.UserDto;
import ait.cohort34.mentor_project.accounting.dto.exception.UserExistsException;
import ait.cohort34.mentor_project.accounting.service.UserAccountService;
import ait.cohort34.mentor_project.security.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public ResponseEntity<UserDto> register(
            @RequestPart("registerDto") String registerDto) throws IOException {
        RegisterDto userRegisterDto = objectMapper.readValue(registerDto, RegisterDto.class);
        UserDto userDto =userAccountService.register(userRegisterDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
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
    @GetMapping("/photos/{id}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Long id) {
        byte[] photoData = userAccountService.getPhotoById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=photo.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoData);
    }

    @PutMapping("/user/{id}/role")
    public ResponseEntity<UserDto> changeRole(@PathVariable Long id, @RequestPart("teacherDto") String teacherDto,
                              @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        TeacherRegisterDto teacherRegisterDto = objectMapper.readValue(teacherDto, TeacherRegisterDto.class);
        UserDto userDto = userAccountService.changeRole(id,teacherRegisterDto,image);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
