package io.github.kim1387.restcloud.storage.base.BaseCloudStoragePlugin;

import io.github.kim1387.restcloud.storage.base.controller.BaseCloudStorageController;
import io.github.kim1387.restcloud.storage.base.interfaces.Plugin;
import io.github.kim1387.restcloud.storage.base.utils.SingletonManager;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;

// 상속시 @Component
public abstract class BaseCloudStoragePlugin implements Plugin<BaseCloudStorageController> {
    private static SingletonManager<? extends BaseCloudStoragePlugin> singletonManager = null;

    private static SingletonManager<? extends BaseCloudStoragePlugin> initSingletonManager() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return new SingletonManager(
                MethodHandles.lookup().lookupClass());
    }

    public static SingletonManager<? extends BaseCloudStoragePlugin> getSingletonManager() {
        if(singletonManager == null) {
            try {
                singletonManager = initSingletonManager();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException("initSingletonManager() failed");
            }
        }

        return singletonManager;
    }


    public abstract Class<? extends BaseCloudStorageController> getControllerClass();
}
