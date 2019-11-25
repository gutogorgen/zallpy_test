package com.evaluation.webproject.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
public class Proposal {

    public static final Integer NOT_PROCESSED = 0;
    public static final Integer PROCESSING = 1;
    public static final Integer APPROVED = 2;
    public static final Integer REJECTED = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date dateOfSubmission;

    private Date dateOfProcessing;

    private Integer status;

    private String resultText;

    private Double value;

    private Long clientCpf;
}
