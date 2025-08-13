package com.saucedemo.tests.ui;

import com.saucedemo.models.ShipInfo;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;

public class ProductTests extends BaseTest {

    @Test
    public void successfulLogoutTest() {
        loginPage.login().clickOnLogout();
        assertThat(page).hasURL("https://www.saucedemo.com/");
    }

    @Test
    public void sortItemsTest() {
        AtomicReference<ProductsPage> productsPage = new AtomicReference<>();

        step("Авторизация", () -> productsPage.set(loginPage.login()));

        step("Проверка отображения имени первого товара", () ->
                assertThat(productsPage.get().getProductNames().first()).hasText("Sauce Labs Backpack"));

        step("Установка фильтра отображения товаров", () -> productsPage.get().setSortFilter("Name (Z to A)"));

        step("Проверка отображения товаров с примененный фильтром", () ->
                assertThat(productsPage.get().getProductNames().first()).hasText("Test.allTheThings() T-Shirt (Red)"));
    }

    @Test
    public void addItemToCartAndBuyTest() {
        AtomicReference<ProductsPage> productsPage = new AtomicReference<>();
        AtomicReference<CartPage> cartPage = new AtomicReference<>();

        step("Авторизация", () -> productsPage.set(loginPage.login()));

        step("Добавление тавара в корзину и проверка имени товара в корзине", () -> {
            String firstItemName = productsPage.get().getProductNames().first().textContent();
            cartPage.set(productsPage.get().addItemToCart(firstItemName).clickOnCart());
            assertThat(cartPage.get().getItems()).containsText(firstItemName);
        });

        step("Оформление заказа", () -> {
            ShipInfo shipInfo = ShipInfo.builder()
                    .firstName("Bobby")
                    .lastName("Winston")
                    .zip("123456")
                    .build();
            cartPage.get().clickOnCheckout().fillInfo(shipInfo).clickOnContinue().clickOnFinish();
            assertThat(cartPage.get().getCompleteHeader()).hasText("Thank you for your order!");
        });
    }

    @Test
    public void cleanCartTest() {
        AtomicReference<CartPage> cartPage = new AtomicReference<>();

        step("Логин и добавление товаров", () -> cartPage.set(loginPage
                .login()
                .addItemToCart("Sauce Labs Backpack")
                .addItemToCart("Sauce Labs Bike Light")
                .addItemToCart("Sauce Labs Bolt T-Shirt")
                .clickOnCart()));

        step("Проверка количества товаров", () -> assertThat(cartPage.get().getItems()).hasCount(3));

        step("Очистка корзины", () -> cartPage.get().cleanCart());

        step("Проверка пустой корзины", () -> assertThat(cartPage.get().getItems()).isHidden());
    }

    @DataProvider(name = "filters and prices")
    public Object[][] filtersAndPrices() {
        return new Object[][]{
                {"Price (low to high)", "$7.99"},
                {"Price (high to low)", "$49.99"}
        };
    }

    @Test(dataProvider = "filters and prices")
    public void sortItemsByPriceTest(String filter, String price) {
        AtomicReference<ProductsPage> productsPage = new AtomicReference<>();

        step("Авторизация", () -> productsPage.set(loginPage.login()));

        step("Установка фильта товаров '%s'".formatted(filter), () ->
                productsPage.get().setSortFilter(filter));

        step("Проверка цены первого товара", () ->
                assertThat(productsPage.get().getProductPrices().first()).hasText(price));
    }
}
