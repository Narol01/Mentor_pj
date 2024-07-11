package ait.cohort34.mentor_project.accounting.dto;

import ait.cohort34.mentor_project.accounting.model.Role;

import java.util.Set;

public class UserDto {
    String login;
    String email;
    private Set<Role> role;
}
