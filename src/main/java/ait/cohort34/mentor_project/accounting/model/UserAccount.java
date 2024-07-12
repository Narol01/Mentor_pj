package ait.cohort34.mentor_project.accounting.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "login")
@Table(name = "users")
public class UserAccount implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String login;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "userAccount")
    private PhotoUser image;
    private String skills;
    private String description;
    private String phoneNum;

    public UserAccount(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public UserAccount(String login, String email, String password, Set<Role> roles, String skills, String description, String phoneNum) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.skills = skills;
        this.description = description;
        this.phoneNum = phoneNum;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
