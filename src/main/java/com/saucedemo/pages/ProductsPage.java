package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.saucedemo.components.Header;
import com.saucedemo.components.SideNavMenu;
import com.saucedemo.utils.BasePageFactory;
import io.qameta.allure.Step;

public final class ProductsPage extends BasePage {

    private Header header;
    private SideNavMenu sideNavMenu;

    @Override
    public void initComponents() {
        header = new Header(page);
        sideNavMenu = new SideNavMenu(page);
    }

    @Step("Get title on the 'Product' page")
    public Locator getTitle() {
        return page.locator(".title");
    }

    @Step("Get all products names")
    public Locator getProductNames() {
        return page.locator("//div[@data-test='inventory-item-name']");
    }

    @Step("Set [sortName] into 'Sort Filter'")
    public ProductsPage setSortFilter(String sortName) {
        page.locator("//select[@data-test='product-sort-container']").selectOption(sortName);
        return this;
    }

    @Step("Add to cart item with name [itemName]")
    public ProductsPage addItemToCart(String itemName) {
        page.locator("//div[text()='%s']//following::button[1]".formatted(itemName)).click();
        return this;
    }

    @Step("Click on Cart button")
    public CartPage clickOnCart() {
        header.clickOnCart();
        return BasePageFactory.createInstance(page, CartPage.class);
    }

    @Step("Click on [Logout] button from side navigation menu")
    public LoginPage clickOnLogout() {
        header.clickOnHamburgerIcon();
        sideNavMenu.clickOnLogout();
        return BasePageFactory.createInstance(page, LoginPage.class);
    }

    @Step("Get product price")
    public Locator getProductPrices() {
        return page.locator(".inventory_item_price");
    }
}
