package ua.artcode;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Properties;

@SpringBootApplication(scanBasePackages = {"ua.artcode"})
@EnableSwagger2 // http://localhost:8080/swagger-ui.html
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${email.host}")
    private String mailHost;
    @Value("${email.port}")
    private int mailPort;
    @Value("${email.user}")
    private String mailUser;
    @Value("${email.pass}")
    private String mailPass;
    @Value("${email.properties.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${email.properties.smtp.starttls.enable}")
    private String mailSmtpStartTLS;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.artcode.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUser);
        mailSender.setPassword(mailPass);

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", mailSmtpAuth);
        props.setProperty("mail.smtp.starttls.enable", mailSmtpStartTLS);

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    @Bean
    public InvocationRequest invocationRequest(){
        return new DefaultInvocationRequest();
    }

    @Bean
    public Invoker invoker(){
        return new DefaultInvoker();
    }


}
