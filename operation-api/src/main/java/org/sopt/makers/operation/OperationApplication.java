package org.sopt.makers.operation;

import org.sopt.makers.operation.client.auth.AuthClientProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AuthClientProperty.class)
@SpringBootApplication(
		scanBasePackageClasses = {AuthRoot.class, CommonRoot.class, DomainRoot.class, ExternalRoot.class}
)
public class OperationApplication {


	public static void main(String[] args) {
		SpringApplication.run(OperationApplication.class, args);
	}
}
