package ru.butorin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.butorin")
@PropertySource("classpath:param.properties")
public class SpringConfiguration {
}
