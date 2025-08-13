package com.saucedemo.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import static com.saucedemo.config.ConfigurationManager.config;

public enum BrowserFactory {

    CHROMIUM {
        @Override
        public Browser createInstance(final Playwright pw) {
            return pw.chromium().launch(options());
        }
    },
    FIREFOX {
        @Override
        public Browser createInstance(final Playwright pw) {
            return pw.firefox().launch(options());
        }
    },
    WEBKIT {
        @Override
        public Browser createInstance(final Playwright pw) {
            return pw.webkit().launch(options());
        }
    };

    public BrowserType.LaunchOptions options() {
        return new BrowserType.LaunchOptions()
                .setHeadless(config().headless())
                .setSlowMo(config().slowMotion());
    }

    public abstract Browser createInstance(final Playwright pw);
}
