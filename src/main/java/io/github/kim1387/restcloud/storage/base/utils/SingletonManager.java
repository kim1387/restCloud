package io.github.kim1387.restcloud.storage.base.utils;

import java.lang.reflect.InvocationTargetException;

public class SingletonManager<T> {
    private T instance;

    public SingletonManager(Class<T> parameterClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        instance = parameterClass.getDeclaredConstructor().newInstance();
    }

    public T getInstance() {
        return instance;
    }
}
