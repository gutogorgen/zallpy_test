package com.evaluation.webproject.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseTransfer {
    private Integer status;
    private String message;
    private Double value;
}
