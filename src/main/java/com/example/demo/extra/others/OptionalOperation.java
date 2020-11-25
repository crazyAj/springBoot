package com.example.demo.extra.others;

import com.example.demo.domain.base.BaseResult;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OptionalOperation {

    @Test
    public void object() {
        BaseResult<String> res = BaseResult.<String>builder().code("1000").build();
        System.out.println(Optional.ofNullable(res)
                .map(BaseResult::getCode)
                .orElse("err"));
    }

    @Test
    public void list() {
        List<BaseResult<String>> res = Arrays.asList(BaseResult.<String>builder().code("1000").build());
        Optional.of(res)
                .orElse(Collections.emptyList())
                .stream()
                .map(BaseResult::getCode)
                .forEach(System.out::println);
    }

}