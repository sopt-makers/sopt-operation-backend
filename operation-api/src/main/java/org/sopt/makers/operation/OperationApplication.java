package org.sopt.makers.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackageClasses = {AuthRoot.class, CommonRoot.class, DomainRoot.class, ExternalRoot.class}
)
public class OperationApplication {


	public static void main(String[] args) {
		SpringApplication.run(OperationApplication.class, args);
	}
}
