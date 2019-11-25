package com.evaluation.webproject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    public static final Integer MARRIED = 0;
    public static final Integer SINGLE = 1;
    public static final Integer DIVORCED = 2;
    public static final Integer WIDOWER = 3;

    @Id
    private Long cpf;

    private String name;

    private Integer age;

    private Integer gender;

    private Integer maritalStatus;

    private Double income;

    private Double creditLimit;

    private Integer dependents;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "clientCpf")
    private List<Proposal> proposalList;
}
