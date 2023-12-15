package com.medbay.config;

import lombok.SneakyThrows;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class IdGenerator implements IdentifierGenerator, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Map<String, Long> lastGeneratedIds = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object){
        String className = object.getClass().getSimpleName();
        String repositoryName = className + "Repository";
        Class<?> repositoryClass = Class.forName("com.medbay.repository." + repositoryName);
        Object repositoryInstance = applicationContext.getBean(repositoryClass);

        Long lastGeneratedId = lastGeneratedIds.get(className);
        if (lastGeneratedId == null) {
            Method method = repositoryClass.getMethod("findByMaxId");
            Object result = method.invoke(repositoryInstance);
            lastGeneratedId = result == null ? 0L : (Long) result;
        }

        lastGeneratedId++;
        lastGeneratedIds.put(className, lastGeneratedId);

        return lastGeneratedId;
    }
}



