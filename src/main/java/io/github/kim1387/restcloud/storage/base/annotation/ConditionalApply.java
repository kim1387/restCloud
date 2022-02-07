package io.github.kim1387.restcloud.storage.base.annotation;

import io.github.kim1387.restcloud.storage.base.utils.PluginChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public interface ConditionalApply extends ConditionalOnExpression {
    PluginChecker pluginChecker = new PluginChecker();

    @Override
    default String value() {
        String name = this.getClass().getSimpleName();
        return Boolean.valueOf(pluginChecker.isPluginNameExists(name)).toString();
    }

    @Override
    public default Class<? extends Annotation> annotationType() {
        return null;
    }
}
