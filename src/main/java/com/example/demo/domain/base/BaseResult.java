package com.example.demo.domain.base;

import lombok.*;

/**
 * 返回结果
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseResult {
    public String code;
    public String message;
    public Object data;

    public BaseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
