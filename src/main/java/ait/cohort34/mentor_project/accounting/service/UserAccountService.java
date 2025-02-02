package ait.cohort34.mentor_project.accounting.service;

import ait.cohort34.mentor_project.accounting.dto.RegisterDto;
import ait.cohort34.mentor_project.accounting.dto.TeacherRegisterDto;
import ait.cohort34.mentor_project.accounting.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserAccountService {
    UserDto register(RegisterDto registerDto) throws IOException;
    List<UserDto> getUsers();

    UserDto getUser(String login);
    UserDto removeUser(Long id);
    UserDto changeRole(Long id, TeacherRegisterDto teacherRegisterDto, MultipartFile image) throws IOException;
    byte[] getPhotoById(Long id);
}
