package br.com.spring.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.spring.controller.TestRestController;


@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses={TestRestController.class})
@Import({ SecurityConfig.class })
public class WebAppConfig {

}
