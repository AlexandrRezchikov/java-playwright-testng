package com.saucedemo.config;

import org.aeonbits.owner.ConfigCache;

public class ConfigurationManager {

    public static Configuration config() {
        return ConfigCache.getOrCreate(Configuration.class);
    }

    public static UserConfig userConfig() {
        return ConfigCache.getOrCreate(UserConfig.class);
    }
}
