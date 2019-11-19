package com.example.demo.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Example implements Serializable{
    private static final long serialVersionUID = 5862677660031485811L;
    private String id;
    private String empKey;
    private String empValue;
}
