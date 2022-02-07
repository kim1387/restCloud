package io.github.kim1387.restcloud.storage.base.utils;

import io.github.kim1387.restcloud.storage.base.annotation.ConditionalApply;
import io.github.kim1387.restcloud.storage.derived.controller.GoogleDriveController;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Stream;

@Component
@NoArgsConstructor
public class PluginChecker {
    @Autowired
    private Environment environment;
    @Autowired
    private DefaultListableBeanFactory beanFactory;
    @Autowired
    private RequestMappingHandlerMapping mapping;

    public static final String pluginPrefix = "cloud.plugins";

    public boolean isPluginNameExists(String name) {
        name = name.replace("Controller", "Plugin");

        Optional<String> property = Optional.empty();

        property = Optional.ofNullable(environment.getProperty(String.format("%s.%s", pluginPrefix, name)));

        property.ifPresentOrElse((value) -> {
            System.out.println(String.format("property value: %s", value));
        }, () -> {
            System.out.println("property value not found");
        });

        return property.isPresent();
    }


    public void removeNotRegisteredPlugins(ConfigurableApplicationContext ctx) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) ctx.getAutowireCapableBeanFactory();

        List<String> conditionalApplies = Arrays.stream(ctx.getBeanNamesForType(ConditionalApply.class)).toList();
        List<String> requestMappings = Arrays.stream(ctx.getBeanNamesForAnnotation(RequestMapping.class)).toList();

        List<String> intersections = conditionalApplies.stream().filter(requestMappings::contains).toList();

        for(String name : intersections) {
            System.out.println(String.format("bean %s found as plugin", name));

            Object bean = ctx.getBean(name);
            RequestMapping requestMapping = bean.getClass().getAnnotation(RequestMapping.class);

            List<String> paths = Arrays.stream(requestMapping.path()).toList();

            String className = bean.getClass().getSimpleName();

            boolean exists = isPluginNameExists(className);

            if(!exists) {
                Optional<RequestMappingInfo> info = mapping.getHandlerMethods()
                        .keySet()
                        .stream()
                        .filter(e -> {
                            Stream<String> handlerPaths = e.getDirectPaths().stream();

                            return paths.stream()
                                    .allMatch(path ->
                                            handlerPaths.anyMatch(
                                                    handlerPath -> handlerPath.contains(path)));
                        })
                        .findFirst();
                info.ifPresent(e -> {
                    mapping.unregisterMapping(e);
                    registry.removeBeanDefinition(name);
                    System.out.println(mapping.getPathPrefixes().containsKey(name));
                });
            }

            String onOff = exists ? "on" : "off";
            System.out.println(String.format("bean %s marked as %s application.yml", name, onOff));
        }
    }
}
