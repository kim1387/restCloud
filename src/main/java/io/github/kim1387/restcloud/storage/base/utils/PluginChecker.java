package io.github.kim1387.restcloud.storage.base.utils;

import io.github.kim1387.restcloud.storage.base.BaseCloudStoragePlugin.BaseCloudStoragePlugin;
import io.github.kim1387.restcloud.storage.base.controller.BaseCloudStorageController;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
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
    public static final String derivedPluginPackage = "io.github.kim1387.restcloud.storage.derived.plugin";

    public boolean checkIsPluginNameExistsInProperty(String name) {
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

        Reflections reflections = new Reflections();
        Set<Class<? extends BaseCloudStoragePlugin>> plugins = reflections.getSubTypesOf(BaseCloudStoragePlugin.class);

        for(Class<? extends BaseCloudStoragePlugin> pluginClass : plugins) {
            String className = pluginClass.getSimpleName();

            BaseCloudStoragePlugin plugin = ctx.getBean(pluginClass);
            List<String> beanNames = Arrays.stream(ctx.getBeanNamesForType(pluginClass)).toList();

            Class<? extends BaseCloudStorageController> controllerClass = plugin.getControllerClass();
            BaseCloudStorageController controller = ctx.getBean(controllerClass);

            System.out.println(String.format("class %s found as plugin", className));

            RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);

            List<String> paths = Arrays.stream(requestMapping.path())
                    .map(path -> {
                        if(!path.startsWith("/")) {
                            path = "/" + path;
                        }
                        return path;
                    })
                    .toList();

            boolean isPluginNameExistsInProperty = checkIsPluginNameExistsInProperty(className);

            if(!isPluginNameExistsInProperty) {
                Optional<RequestMappingInfo> info = mapping.getHandlerMethods()
                        .keySet()
                        .stream()
                        .filter(e -> {
                            Stream<String> handlerPaths = e.getDirectPaths().stream();

                            return paths.stream()
                                    .allMatch(path ->
                                            handlerPaths.anyMatch(
                                                    handlerPath -> handlerPath.startsWith(path)));
                        })
                        .findFirst();
                info.ifPresent(e -> {
                    mapping.unregisterMapping(e);

                    beanNames.forEach(name -> {
                        System.out.println(String.format("unregistering bean %s...", name));

                        registry.removeBeanDefinition(name);

                        String notOr = mapping.getPathPrefixes().containsKey(name) ? "" : " not";
                        System.out.println(String.format("after unregister, mapping%s contains key %s", notOr, name));
                    });
                });
            }

            String onOff = isPluginNameExistsInProperty ? "on" : "off";
            System.out.println(String.format("plguin %s marked as %s application.yml", className, onOff));
        }
    }
}
