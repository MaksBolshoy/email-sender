package com.gmail.kuznetsov.msg.sender.emailsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync(proxyTargetClass=true)
@EnableScheduling
@SpringBootApplication
public class EmailSenderApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmailSenderApplication.class, args);
	}
}
