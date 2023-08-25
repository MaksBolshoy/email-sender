package com.gmail.kuznetsov.msg.sender.emailsender.config;

import com.gmail.kuznetsov.msg.sender.emailsender.config.init.PostgreSQLInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {
        PostgreSQLInitializer.Initializer.class
})
@Transactional
public abstract class IntegrationBaseTest {

    @BeforeAll
    static void init() {
        PostgreSQLInitializer.container.start();
    }
}
