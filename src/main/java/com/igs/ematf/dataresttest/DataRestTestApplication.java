package com.igs.ematf.dataresttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

@SpringBootApplication
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableLoadTimeWeaving
public class DataRestTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataRestTestApplication.class, args);
    }

}
