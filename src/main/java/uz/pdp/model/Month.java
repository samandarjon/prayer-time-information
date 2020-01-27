package uz.pdp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Month {
    @GeneratedValue
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;
}
