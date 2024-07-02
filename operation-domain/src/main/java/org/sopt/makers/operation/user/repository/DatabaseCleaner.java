package org.sopt.makers.operation.user.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("domain-unit")
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> {
                    String snakeName = convertToSnake(e.getName());
                    if (snakeName.equals("user")) {
                        return "users";
                    }
                    return snakeName;
                })
                .collect(Collectors.toList());
        System.out.println(Arrays.deepToString(tableNames.toArray()));
    }

    private String convertToSnake(String camel) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (char alphabet : camel.toCharArray()) {
            if (Character.isUpperCase(alphabet)) {
                if (index == 0) {
                    builder.append(Character.toLowerCase(alphabet));
                } else {
                    builder.append("_").append(Character.toLowerCase(alphabet));
                }
            } else {
                builder.append(alphabet);
            }
            index++;
        }
        return builder.toString();
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        for (String name : tableNames) {
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", name)).executeUpdate();
            entityManager.createNativeQuery(String.format("ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1", name)).executeUpdate();
        }
    }
}
