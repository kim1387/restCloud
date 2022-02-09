package io.github.kim1387.restcloud.storage.derived.plugin;

import io.github.kim1387.restcloud.storage.base.BaseCloudStoragePlugin.BaseCloudStoragePlugin;
import io.github.kim1387.restcloud.storage.base.controller.BaseCloudStorageController;
import io.github.kim1387.restcloud.storage.derived.controller.GoogleDriveController;
import org.springframework.stereotype.Component;

@Component
public class GoogleDrivePlugin extends BaseCloudStoragePlugin {

    @Override
    public Class<? extends BaseCloudStorageController> getControllerClass() {
        return GoogleDriveController.class;
    }
}
