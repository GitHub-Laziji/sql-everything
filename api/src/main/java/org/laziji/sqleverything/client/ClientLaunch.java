package org.laziji.sqleverything.client;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientLaunch {

    private static final Logger logger = LoggerFactory.getLogger(ClientLaunch.class);

    @Value("${server.port:8080}")
    private int port;

    @PostConstruct
    private void init() {
        logger.info("http://localhost:{}", port);
    }
}
