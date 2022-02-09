package io.github.kim1387.restcloud.storage.base.interfaces;

public interface HasControllerClass<T> {
    Class<? extends T> getControllerClass();
}
