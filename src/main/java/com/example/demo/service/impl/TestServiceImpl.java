package com.example.demo.service.impl;

import com.example.demo.dao.hikari.HikariExampleDao;
import com.example.demo.domain.Example;
import com.example.demo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 测试编程式事务
 *      1. TransactionManager
 *      2. TransactionTemplate
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private HikariExampleDao hikariExampleDao;
    @Autowired
    @Qualifier("transactionManager_hikari")
    private DataSourceTransactionManager transactionManager;
    @Autowired
    @Qualifier("transactionTemplate_hikari")
    private TransactionTemplate transactionTemplate;

    /**
     * TransactionManager
     */
    @Override
    public Boolean testManaulTransaction(boolean flag) {
        Boolean result = true;
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Example example = new Example();
            example.setId("111111");
            example.setEmpKey("111111");
            example.setEmpValue("111111");
            hikariExampleDao.insertSelective(example);
            if (flag) {
                String s = null;
                s.length();
            }
            transactionManager.commit(status);
            log.info("--- 事务已提交 SUCCESS ---");
        } catch (Exception e) {
            result = false;
            transactionManager.rollback(status);
            log.info("--- 事务已回滚 FAILD --- {}", e);
        }
        return result;
    }

    /**
     * TransactionTemplate
     */
    @Override
    public Boolean testManaulTransaction2(boolean flag) {
        return transactionTemplate.execute(transactionStatus -> {
            Boolean result = true;
            try {
                Example example = new Example();
                example.setId("222222");
                example.setEmpKey("222222");
                example.setEmpValue("222222");
                hikariExampleDao.insertSelective(example);
                if (flag) {
                    String s = null;
                    s.length();
                }
                log.info("--- 事务已提交 SUCCESS ---");
            } catch (Exception e) {
                result = false;
                transactionStatus.setRollbackOnly();
                log.info("--- 事务已回滚 FAILD --- {}", e);
            }
            return result;
        });
    }

}
