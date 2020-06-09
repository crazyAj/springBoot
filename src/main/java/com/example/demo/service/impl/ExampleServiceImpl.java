package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.AppConstant;
import com.example.demo.common.dataSource.DataSource;
import com.example.demo.common.dataSource.DataSourceContextHolder;
import com.example.demo.common.dataSource.DataSourceEnum;
import com.example.demo.common.exception.CustomException;
import com.example.demo.dao.ExampleMapper;
import com.example.demo.domain.Example;
import com.example.demo.domain.base.BaseModel;
import com.example.demo.service.ExampleService;
import com.example.demo.utils.fileUtils.FileFunc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
//        List<Example> list = this.list(Wrappers.<Example>lambdaQuery().eq(BaseModel::getDeleteFlag, 0));
        this.save(examples.get(0));

        DataSourceContextHolder.setDataSource(examples.get(0).getExVal());
        this.saveBatch(examples.subList(1, examples.size()));

        List<Example> res = this.list(
                Wrappers.<Example>lambdaQuery()
                        .select(Example::getUnid,
                                Example::getExKey,
                                Example::getExVal,
                                BaseModel::getCreateTime,
                                BaseModel::getLastUpdateTime)
                        .eq(BaseModel::getDeleteFlag, 0)
        );
//        log.info("test res {}", JSONObject.toJSONString(res));

        log.info("test Exception code = {}", Boolean.valueOf(FileFunc.getPropValue(AppConstant.EXCEPTION_CODE_PATH, "flag")));
        if (Boolean.valueOf(FileFunc.getPropValue(AppConstant.EXCEPTION_CODE_PATH, "flag"))) {
            throw CustomException.builder().code("BI0002").msg("测试不存在").build();
        }
        return res;
    }
}
