package fr.plb.ecom_user.configuration;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class LoggingConfiguration {

    public LoggingConfiguration(
            @Value("${spring.application.name}") String appName,
            @Value("${server.port}") String serverPort,
            @Value("${application.logstash.host}") String host,
            @Value("${application.logstash.port}") int port,
            ObjectMapper mapper
    ) throws JsonProcessingException, UnknownHostException {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        map.put("app_host", InetAddress.getLocalHost().getHostAddress());
        String customFields = mapper.writeValueAsString(map);

        ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);

        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setThrowableConverter(throwableConverter);
        logstashEncoder.setCustomFields(customFields);

        LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
        logstashAppender.addDestinations(new InetSocketAddress(host, port));
        logstashAppender.setIncludeCallerData(true);
        logstashAppender.setContext(context);
        logstashAppender.setEncoder(logstashEncoder);
        logstashAppender.setName("ASYNC_LOGSTASH");
        logstashAppender.setRingBufferSize(512);
        logstashAppender.start();
        context.getLogger("ROOT").addAppender(logstashAppender);
    }

}
