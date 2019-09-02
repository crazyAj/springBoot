package com.example.demo.utils.dsTools;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

/**
 * 在classpath下建一个jta.properties（或者transactions.properties）文件
 * 配置com.atomikos.icatch属性
 */
@Configuration
public class JtaTransactionManagerConfig {
    @Bean(name = "xaTX")
    public JtaTransactionManager regJtaTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }
}
