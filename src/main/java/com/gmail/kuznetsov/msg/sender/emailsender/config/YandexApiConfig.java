package com.gmail.kuznetsov.msg.sender.emailsender.config;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ProgressListener;
import com.yandex.disk.rest.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация взаимодействия с api Яндекса
 */
@Configuration
public class YandexApiConfig {
    @Value("${app.cloud.yandex.client_id}")
    private String clientId;

    @Value("${app.cloud.yandex.token}")
    private String token;

    /**
     * Слушатель прогресса загрузки/выгрузки файлов на ЯндексДиск
     * @return бин ProgressListener
     */
    @Bean
    public ProgressListener progressListener() {
        return new ProgressListener() {
            @Override
            public void updateProgress(long loaded, long total) {

            }

            @Override
            public boolean hasCancelled() {
                return false;
            }
        };
    }

    /**
     * @return бин для взаимодействия с api Яндекса
     */
    @Bean
    public RestClient restClient() {
        return new RestClient(new Credentials(clientId, token));
    }

}
