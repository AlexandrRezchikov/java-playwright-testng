package com.saucedemo.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;

import static com.saucedemo.config.ConfigurationManager.config;

public class BrowserManager {

    public static Browser getBrowser(final Playwright pw) {
        return BrowserFactory.valueOf(config().browser().toUpperCase()).createInstance(pw);
    }
}
