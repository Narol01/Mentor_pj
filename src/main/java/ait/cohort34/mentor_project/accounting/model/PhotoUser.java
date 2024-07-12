package ait.cohort34.mentor_project.accounting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PhotoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    @Lob
    @Column(name = "photos", columnDefinition = "LONGBLOB")
    private byte[] data;

    @OneToOne
    @JoinColumn(name ="user_id")
    private UserAccount userAccount;

    public PhotoUser(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public PhotoUser(byte[] data) {
        this.data = data;
    }
}
