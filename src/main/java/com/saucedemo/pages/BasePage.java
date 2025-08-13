package com.saucedemo.pages;

import com.microsoft.playwright.Page;

import static com.saucedemo.config.ConfigurationManager.config;

public abstract class BasePage {

    protected Page page;

    public void setAndConfigurationPage(final Page page) {
        this.page = page;
        if (page != null && config() != null) {
            page.setDefaultTimeout(config().timeout());
        }
    }

    public void initComponents() {
    }
}
