package com.jw.boardservice;

import com.jw.boardservice.likes.LikesMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.Lifecycle;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class BoardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardServiceApplication.class, args);
    }

    @Component
    @RequiredArgsConstructor
    public static class ApplicationLifeCycle implements Lifecycle {

        private final LikesMongoRepository likesMongoRepository;

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            likesMongoRepository.deleteAll();
        }

        @Override
        public boolean isRunning() {
            return true;
        }
    }
}
