package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.ExampleMapper;
import com.example.demo.domain.base.BaseModel;
import com.example.demo.domain.Example;
import com.example.demo.service.ExampleService;
import com.example.demo.common.dataSource.DataSource;
import com.example.demo.common.dataSource.DataSourceContextHolder;
import com.example.demo.common.dataSource.DataSourceEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataSource(DataSourceEnum.MASTER)
@Service
@Transactional
public class ExampleServiceImpl extends ServiceImpl<ExampleMapper, Example> implements ExampleService {

    /**
     * 测试 分布式事务
     *
     * @param examples
     * @return
     */
    @Override
    public List<Example> testTx(List<Example> examples) {
        DataSourceContextHolder.setDataSource(examples.get(0).getExKey());
        this.save(examples.get(0));

        DataSourceContextHolder.setDataSource(examples.get(0).getExVal());
        this.saveBatch(examples.subList(1, examples.size()));
        List<Example> res = this.list(
                Wrappers.<Example>lambdaQuery()
                        .select(Example::getUnid, Example::getExKey, Example::getExVal, BaseModel::getCreateTime, BaseModel::getLastUpdateTime)
                        .ge(BaseModel::getCreateTime, 0)
        );

//        String str = null;
//        str.length();
        return res;
    }
}
