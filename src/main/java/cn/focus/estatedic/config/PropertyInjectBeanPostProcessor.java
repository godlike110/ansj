package cn.focus.estatedic.config;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class PropertyInjectBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private ConfigBean configBean;

    private SimpleTypeConverter typeConverter = new SimpleTypeConverter();

    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        if ("appConstants".equals(beanName)) {
            findPropertyAutowiringMetadata(bean);
        }
        return true;
    }

    private void findPropertyAutowiringMetadata(final Object bean) {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) throws IllegalAccessException {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                if (annotation != null) {
                    Object strValue = configBean.getProperty(annotation.value());
                    if (null != strValue) {
                        Object value = typeConverter.convertIfNecessary(strValue, field.getType());
                        ReflectionUtils.makeAccessible(field);
                        field.set(null, value);
                    }
                }
            }
        });
    }
    
    
}