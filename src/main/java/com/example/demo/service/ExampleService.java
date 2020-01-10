package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.domain.Example;

import java.util.List;

public interface ExampleService extends IService<Example> {

    /**
     * 测试 分布式事务
     *
     * @param examples
     * @return
     */
    List<Example> testTx(List<Example> examples);

}
