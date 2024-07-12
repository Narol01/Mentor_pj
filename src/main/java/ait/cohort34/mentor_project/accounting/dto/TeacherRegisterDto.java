package ait.cohort34.mentor_project.accounting.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherRegisterDto {
    private String skills;
    private String description;
    private String phoneNum;
    @JsonCreator
    public TeacherRegisterDto(@JsonProperty("skills") String skills,
                              @JsonProperty("description") String description,
                              @JsonProperty("phoneNum") String phoneNum) {
        this.skills = skills;
        this.description = description;
        this.phoneNum = phoneNum;
    }
}
