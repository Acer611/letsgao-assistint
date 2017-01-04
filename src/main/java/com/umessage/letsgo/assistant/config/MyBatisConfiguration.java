package com.umessage.letsgo.assistant.config;

import com.github.pagehelper.PageHelper;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.util.Properties;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gaofei on 2016/12/19.
 */
@Configuration
public class MyBatisConfiguration {
    private static final Logger logger =  LoggerFactory.getLogger(MyBatisConfiguration.class);

    @Bean
    public PageHelper pageHelper() {
        logger.info("注册MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
