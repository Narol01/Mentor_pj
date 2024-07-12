package ait.cohort34.mentor_project.accounting.dto;

import ait.cohort34.mentor_project.accounting.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    Long id;
    String login;
    String email;
    private String skills;
    private String description;
    private String PhoneNum;
    private String photoUrls;
    private Set<Role> role;
}
