package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConfigurationService {

    @Inject
    private com.typesafe.config.Config config;

    public String getString(String path) {
        if (config.hasPath(path)) {
            return config.getString(path);
        }
        return null;
    }

    public Integer getInt(String path) {
        if (config.hasPath(path)) {
            return config.getInt(path);
        }
        return null;
    }
}