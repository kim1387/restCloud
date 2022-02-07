package io.github.kim1387.restcloud.storage.base.utils;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
@NoArgsConstructor
public class PluginChecker {
    @Autowired
    private Environment environment;

    public static final String pluginPrefix = "cloud.plugins";

    public boolean isPluginNameExists(String name) {
        Optional<String> property = Optional.empty();

        property = Optional.ofNullable(environment.getProperty(String.format("%s.%s", pluginPrefix, name)));

        property.ifPresentOrElse((value) -> {
            System.out.println(String.format("property value: %s", value));
        }, () -> {
            System.out.println("property value not found");
        });

        return property.isPresent();
    }
}
