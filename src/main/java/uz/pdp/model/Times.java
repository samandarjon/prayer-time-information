package uz.pdp.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Data

public class Times {
    @GeneratedValue
    @Id
    private String id;

    @Column
    private Long sana;
    @Column
    private String hafta_kuni;

    @Column
    private Time tong;

    @Column
    private Time quyosh;

    @Column
    private Time peshin;

    @Column
    private Time asr;

    @Column
    private Time shom;

    @Column
    private Time hufton;

    @ManyToOne
    private Month month;

}