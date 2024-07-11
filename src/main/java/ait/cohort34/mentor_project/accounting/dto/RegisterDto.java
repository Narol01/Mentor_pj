package ait.cohort34.mentor_project.accounting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    private String login;
    private String email;
    private String password;
    private String rePassword;
}
