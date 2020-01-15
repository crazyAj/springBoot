package com.example.demo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -7017411287972434840L;

    private String code;
    private String msg;
}

