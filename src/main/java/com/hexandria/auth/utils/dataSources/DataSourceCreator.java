package com.hexandria.auth.utils.dataSources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;


/**
 * Created by root on 01.04.17.
 */
@Configuration
public class DataSourceCreator {

    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource(){
        return DataSourceBuilder.
                create()
                .password(environment.getProperty("db.password"))
                .username(environment.getProperty("db.user"))
                .driverClassName(environment.getProperty("db.driver"))
                .url(environment.getProperty("db.url"))
                .build();
    }
}