package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.saucedemo.utils.BasePageFactory;
import io.qameta.allure.Step;

import static com.saucedemo.config.ConfigurationManager.config;
import static com.saucedemo.config.ConfigurationManager.userConfig;

public final class LoginPage extends BasePage {

    @Step("Navigate to the login page")
    public LoginPage open() {
        page.navigate(config().baseUrl());
        return this;
    }

    @Step("Type [username] into 'Username' textbox")
    public LoginPage typeUsername(final String username) {
        page.fill("#user-name", username);
        return this;
    }

    @Step("Type [password] into 'Password' textbox")
    public LoginPage typePassword(final String password) {
        page.fill("#password", password);
        return this;
    }

    @Step("Get error message")
    public Locator getErrorMessage() {
        return page.locator(".error-message-container h3");
    }

    @Step("Click on the [Login] button")
    public ProductsPage submitLogin() {
        page.click("#login-button");
        return BasePageFactory.createInstance(page, ProductsPage.class);
    }

    @Step("Login attempt to swag Labs")
    public ProductsPage loginAs(final String username, final String password) {
        return open()
                .typeUsername(username)
                .typePassword(password)
                .submitLogin();
    }

    @Step("Login standard user")
    public ProductsPage login() {
        return open()
                .typeUsername(userConfig().standardUsername())
                .typePassword(userConfig().standardPassword())
                .submitLogin();
    }
}
