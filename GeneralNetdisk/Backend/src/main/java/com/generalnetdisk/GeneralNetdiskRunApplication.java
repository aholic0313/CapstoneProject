package com.generalnetdisk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.generalnetdisk"})
@MapperScan(basePackages = {"com.generalnetdisk.mappers"})
@EnableTransactionManagement
@EnableScheduling
public class GeneralNetdiskRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneralNetdiskRunApplication.class, args);
    }
}
