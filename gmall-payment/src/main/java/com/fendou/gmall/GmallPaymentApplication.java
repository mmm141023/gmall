package com.fendou.gmall;

import com.fendou.gmall.config.WebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GmallPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPaymentApplication.class, args);
    }

    @Bean
    public WebMvcConfiguration webMvcConfiguration() {
        return new WebMvcConfiguration();
    }
}
