package uz.pdp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Country {
    @GeneratedValue
    @Id
    private String id;

    @Column
    private String name;

    @Column

    private Long difference;
}
