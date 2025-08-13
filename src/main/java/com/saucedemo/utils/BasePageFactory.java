package com.saucedemo.utils;

import com.microsoft.playwright.Page;
import com.saucedemo.pages.BasePage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BasePageFactory {

    public static <T extends BasePage> T createInstance(final Page page, final Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            instance.setAndConfigurationPage(page);
            instance.initComponents();
            return instance;
        } catch (Exception e) {
            log.error("BasePageFactory::createInstance", e);
        }

        throw new NullPointerException("Page class instantiation failed.");
    }
}
