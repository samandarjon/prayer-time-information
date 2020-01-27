package uz.pdp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Users implements Serializable {
    @Id
    private String id;

    @Column(nullable = false)
    private Long chat_id;

    @Column
    private Integer user_id;

    @Column(nullable = false)
    private String name;

    @Column
    private String username;


}
