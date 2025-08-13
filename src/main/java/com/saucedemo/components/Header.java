package com.saucedemo.components;

import com.microsoft.playwright.Page;

public final class Header extends BaseComponent {

    public Header(final Page page) {
        super(page);
    }

    public void clickOnHamburgerIcon() {
        page.click("#react-burger-menu-btn");
    }

    public void clickOnCart() {
        page.locator(".shopping_cart_link").click();
    }
}
