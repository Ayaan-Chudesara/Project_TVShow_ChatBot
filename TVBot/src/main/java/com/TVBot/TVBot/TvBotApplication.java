package com.TVBot.TVBot;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TvBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TvBotApplication.class, args);

	}

	@Bean
	public ApplicationRunner printBeans(ApplicationContext ctx) {
		return args -> {
			for (String beanName : ctx.getBeanDefinitionNames()) {
				if (beanName.toLowerCase().contains("chat")) {
					System.out.println(beanName);
				}
			}
		};
	}

}
