package uz.pdp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Profile {

    @GeneratedValue
    @Id
    private String id;

    @Column
    private Integer step;

    @OneToOne
    private Users user;

    @OneToOne
    private Country country;
}
