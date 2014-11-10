package cn.focus.estatedic.config;

import java.util.Arrays;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ConfigBean implements InitializingBean, FactoryBean<Object> {
    
    public static final String APP_ENV_KEY = "spring.profiles.active";

    private static Log log = LogFactory.getLog(ConfigBean.class);

    private CompositeConfiguration configuration;

    private Configuration[] configurations;

    public ConfigBean() {
        
    }

    public ConfigBean(Configuration configuration) {
        this.configuration = new CompositeConfiguration(configuration);
    }

    public void afterPropertiesSet() {
        if (configuration == null && (configurations == null || configurations.length == 0)) {
            throw new IllegalArgumentException("no configuration object or location specified");
        }
        if (configuration == null) {
            configuration = new CompositeConfiguration();
        }
        if (configurations != null) {
            
            // 获取应用运行的环境 本地 测试服务器 还是正式服务器
            String envType = System.getProperty(APP_ENV_KEY, "local");
            
            log.info("应用运行环境变量appEnv:" + APP_ENV_KEY + " ============ " + "运行环境envType:" + envType);

            int beginIndex = 0;
            
            // 测试服务器
            if ("test".equals(envType)) {
                beginIndex = 1;
            }
            
            // SCE测试服务器
            if ("test_sce".equals(envType)) {
                beginIndex = 2;
            }
            
            // 生产环境正式服务器
            if ("product".equals(envType)) {
                beginIndex = 3;
            }
            
            for (int i = beginIndex; i < configurations.length; i++) {
                configuration.addConfiguration(configurations[i]);
            }
        }
    }

    public void setConfigurations(Configuration[] cfgs) {
        if (cfgs == null) {
            this.configurations = new Configuration[0];
        } else {
            this.configurations = Arrays.copyOf(cfgs, cfgs.length);
        }
    }

    public Object getObject() {
        return (configuration != null) ? ConfigurationConverter.getProperties(configuration) : null;
    }

    public Class<?> getObjectType() {
        return java.util.Properties.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getProperty(String key) {
        // TODO List属性的获取可能会有问题，有待改进
        return configuration.getString(key);
    }
 

}
