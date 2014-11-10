package cn.focus.estatedic.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于将配置文件中的字段值标注在配置类AppConstants的字段上<br>
 * AppConstans的字段值将自动装配对应配置文件中的值
 * 
 * @author huimingtao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ConfigProperty {
    String value() default "";
}
