package io.github.kim1387.restcloud;

import io.github.kim1387.restcloud.storage.base.utils.PluginChecker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

@SpringBootApplication(excludeName = {"io.github.kim1387.restcloud.storage"})
public class RestCloudApplication {
    private static PluginChecker pluginChecker;
    private static ConfigurableApplicationContext ctx;


    @Autowired
    public RestCloudApplication(PluginChecker pluginChecker) {
        RestCloudApplication.pluginChecker = pluginChecker;
    }

    public static void main(String[] args) {
        ctx = SpringApplication.run(RestCloudApplication.class, args);

        pluginChecker.removeNotRegisteredPlugins(ctx);

    }


}
