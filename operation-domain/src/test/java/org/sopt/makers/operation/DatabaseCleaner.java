package org.sopt.makers.operation;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("test")
public class DatabaseCleaner implements InitializingBean {

    private static final String TRUNCATE_TABLE_QUERY_FORMAT = "TRUNCATE TABLE %s";
    private static final String ALTER_TABLE_ID_COLUMN_TO_START_FROM_ONE_QUERY_FORMAT = "ALTER TABLE %s ALTER COLUMN id RESTART WITH 1";

    @PersistenceContext
    private EntityManager entityManager;

    private final List<String> tableNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        findAllTableNames();
    }

    private  void findAllTableNames() {
        List<String> names = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> {
                    String snakeName = convertToSnake(e.getName());
                    if (snakeName.equals("user")) {
                        return "users";
                    }
                    if (snakeName.equals("banner")) {
                        return "banners";
                    }
                    return snakeName;
                })
                .toList();
        tableNames.addAll(names);
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
        entityManager.clear();
        truncate();
    }

    private void truncate() {
        for (String name : tableNames) {
            entityManager.createNativeQuery(String.format(TRUNCATE_TABLE_QUERY_FORMAT, name))
                    .executeUpdate();
            entityManager.createNativeQuery(String.format(ALTER_TABLE_ID_COLUMN_TO_START_FROM_ONE_QUERY_FORMAT, name))
                    .executeUpdate();
        }
    }
}
