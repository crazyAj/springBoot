package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.ExampleMapper;
import com.example.demo.domain.Example;
import com.example.demo.service.ExampleService;
import com.example.demo.utils.ds.DataSource;
import com.example.demo.utils.ds.DataSourceEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataSource(DataSourceEnum.MASTER)
@Service
@Transactional
public class ExampleServiceImpl extends ServiceImpl<ExampleMapper, Example> implements ExampleService {

}
