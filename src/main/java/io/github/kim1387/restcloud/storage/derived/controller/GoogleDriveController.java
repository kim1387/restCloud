package io.github.kim1387.restcloud.storage.derived.controller;

import io.github.kim1387.restcloud.storage.base.controller.BaseCloudStorageController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "google-drive")
public class GoogleDriveController extends BaseCloudStorageController {

    @GetMapping("")
    public String root() {
        return "Hello CBU DarkMode";
    }
}
