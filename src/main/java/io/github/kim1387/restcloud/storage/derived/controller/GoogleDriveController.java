package io.github.kim1387.restcloud.storage.derived.controller;

import io.github.kim1387.restcloud.storage.base.controller.BaseCloudStorageImplementation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "google-drive")
public class GoogleDriveController extends BaseCloudStorageImplementation {

    @GetMapping("/")
    public String root() {
        return "Hello CBU DarkMode";
    }
}
