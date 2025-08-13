package com.saucedemo.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:config.properties"})
public interface UserConfig extends Config {

    @Key("username.standard")
    String standardUsername();

    @Key("password.standard")
    String standardPassword();
}
