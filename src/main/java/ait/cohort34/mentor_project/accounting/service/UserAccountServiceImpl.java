package ait.cohort34.mentor_project.accounting.service;

import ait.cohort34.mentor_project.accounting.dao.RoleRepository;
import ait.cohort34.mentor_project.accounting.dao.UserAccountRepository;
import ait.cohort34.mentor_project.accounting.dao.UserPhotoRepository;
import ait.cohort34.mentor_project.accounting.dto.TeacherRegisterDto;
import ait.cohort34.mentor_project.accounting.dto.exception.PhotoNotFoundException;
import ait.cohort34.mentor_project.accounting.dto.exception.UserExistsException;
import ait.cohort34.mentor_project.accounting.dto.exception.UserNotFoundException;
import ait.cohort34.mentor_project.accounting.dto.RegisterDto;
import ait.cohort34.mentor_project.accounting.dto.UserDto;
import ait.cohort34.mentor_project.accounting.model.PhotoUser;
import ait.cohort34.mentor_project.accounting.model.Role;
import ait.cohort34.mentor_project.accounting.model.UserAccount;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService{
    @Autowired
    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;
    final RoleRepository roleRepository;
    final UserPhotoRepository photoRepository;

    @Override
    @Transactional
    public UserDto register(RegisterDto registerDto) {
        if (userAccountRepository.existsByLogin(registerDto.getLogin())) {
            throw new UserExistsException("A user with this login already exists.");
        }

        UserAccount userAccount = modelMapper.map(registerDto, UserAccount.class);
        String password = passwordEncoder.encode(registerDto.getPassword());
        userAccount.setPassword(password);

        Role userRole = roleRepository.findByTitle("ROLE_STUDENT");
        if (userRole == null) {
            userRole = new Role("ROLE_STUDENT");
            roleRepository.save(userRole);
        }

        userAccount.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers() {
        Iterable<UserAccount> userAccounts = userAccountRepository.findAll();
        List<UserDto> users = new ArrayList<>();

        userAccounts.forEach(userAccount -> {
            if (!"admin".equals(userAccount.getLogin())) {
                UserDto userDto = modelMapper.map(userAccount, UserDto.class);
                users.add(userDto);
            }
        });

        return users;
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }
    @Transactional
    @Override
    public UserDto removeUser(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(userAccount);

        return modelMapper.map(userAccount, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto changeRole(Long id, TeacherRegisterDto teacherRegisterDto, MultipartFile image) throws IOException {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        List<Role> roles = entityManager.createQuery("SELECT r FROM Role r WHERE r.title = :title", Role.class)
                .setParameter("title", "ROLE_TEACHER")
                .getResultList();
        if (!roles.isEmpty()) {
            Role userRole = roles.get(0);
            roleRepository.save(userRole);
            userAccount.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }
        if (image != null && !image.isEmpty()) {
            PhotoUser photo = new PhotoUser();
            photo.setName(image.getOriginalFilename());
            photo.setType(image.getContentType());
            photo.setData(image.getBytes());

            userAccount.setImage(photo);
            photo.setUserAccount(userAccount);

            userAccountRepository.save(userAccount);
        } else {
            userAccountRepository.save(userAccount);
        }
        UserDto userDto = modelMapper.map(userAccount, UserDto.class);
        if (userAccount.getImage() != null) {
            String photoUrl = "/api/account/photos/" + userAccount.getImage().getId();
            userDto.setPhotoUrls(photoUrl);
        }

        userAccount.setSkills(teacherRegisterDto.getSkills());
        userAccount.setDescription(teacherRegisterDto.getDescription());
        userAccount.setPhoneNum(teacherRegisterDto.getPhoneNum());

        userAccountRepository.save(userAccount);
        return userDto;
    }

    @Override
    public byte[] getPhotoById(Long id) {
        PhotoUser photoUser = photoRepository.findById(id).orElseThrow(PhotoNotFoundException::new);
        return photoUser.getData();
    }

}
