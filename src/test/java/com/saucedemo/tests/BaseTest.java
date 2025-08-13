package com.saucedemo.tests;

import com.google.common.collect.ImmutableMap;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.saucedemo.pages.BasePage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.BasePageFactory;
import com.saucedemo.utils.BrowserManager;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static com.saucedemo.config.ConfigurationManager.config;

@Slf4j
public abstract class BaseTest {

    protected Playwright pw;
    protected Browser browser;
    protected BrowserContext browserContext;
    protected Page page;
    protected LoginPage loginPage;
    private boolean needVideo;

    @BeforeClass
    public void initBrowser() {
        log.info("[Initializing browser...]");
        pw = Playwright.create();
        browser = BrowserManager.getBrowser(pw);

        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("[Platform]", System.getProperty("os.name"))
                        .put("[Version]", System.getProperty("os.version"))
                        .put("[Java Version]", System.getProperty("java.version"))
                        .put("[Browser]", config().browser().toUpperCase())
                        .put("[Headless]", String.valueOf(config().headless()))
                        .put("[Slow Motion]", String.valueOf(config().slowMotion()))
                        .put("[Timeout]", String.valueOf(config().timeout()))
                        .put("[Base URL]", config().baseUrl())
                        .build(),
                config().allureResultsDir());
    }

    @BeforeMethod
    public void createContext() {
        log.info("[Creating new browser context...]");
        if (config().video()) {
            browserContext = browser.newContext(new Browser.NewContextOptions()
                    .setRecordVideoDir(Paths.get(config().baseTestVideoPath())));
        } else {
            browserContext = browser.newContext();
        }

        if (browserContext == null) {
            throw new RuntimeException("[Browser context is not initialized!]");
        }

        page = browserContext.newPage();
        page.setViewportSize(1920, 1080);

        if (page == null) {
            throw new RuntimeException("[Page is not initialized!]");
        }

        loginPage = createInstance(LoginPage.class);
    }

    @AfterMethod
    public void attach(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshotOnFailure();
            needVideo = false;
        }
        browserContext.close();
        if (config().video() && needVideo) {
            captureVideo();
        }
        needVideo = false;
    }

    @AfterClass
    public void close() {
        log.info("[Browser CLOSE]");
        browser.close();
        pw.close();
    }

    @Attachment(value = "Failed Test Case Screenshot", type = "image/png")
    private byte[] captureScreenshotOnFailure() {
        return page.screenshot();
    }

    @Attachment(value = "Test Video", type = "video/webm")
    private byte[] captureVideo() {
        try {
            return Files.readAllBytes(page.video().path());
        } catch (Exception e) {
            return new byte[0];
        }
    }

    protected <T extends BasePage> T createInstance(Class<T> basePage) {
        return BasePageFactory.createInstance(page, basePage);
    }
}
