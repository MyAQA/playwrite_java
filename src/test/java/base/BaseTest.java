package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.reporting.ExtentListeners;
import utils.reporting.ExtentSteps;
import utils.reporting.ScreenshotsUtil;

@Slf4j
public class BaseTest {
    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

    protected static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    private boolean maximized;

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void setUpBrowser(@Optional("chromium") String browserName, @Optional("false") boolean headless) {
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        BrowserEnum browserSelected = BrowserEnum.valueOf(browserName.toUpperCase());
        Browser browser = BrowserFactory.createBrowser(playwright, browserSelected, headless);
        browserThreadLocal.set(browser);
        maximized = BrowserFactory.isMaximizable(browserSelected, headless);

        log.info("Browser [{}] launched (headless={}) on thread {}", browserName, headless, Thread.currentThread().getId());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpContext() {
        Browser.NewContextOptions options = new Browser.NewContextOptions();
        if (maximized) {
            options.setViewportSize(null);
        } else {
            options.setViewportSize(1920, 1080);
        }
        BrowserContext context = browserThreadLocal.get().newContext(options);
        contextThreadLocal.set(context);

        Page page = context.newPage();
        pageThreadLocal.set(page);
        ExtentSteps.setPage(page);
    }

    protected Page getPage() {
        return pageThreadLocal.get();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownContext(ITestResult result) {
        Page page = pageThreadLocal.get();

        boolean failed = result.getStatus() == ITestResult.FAILURE;

        if (failed && page != null && !ExtentSteps.isReported(result.getThrowable())) {
            String testName = result.getMethod().getMethodName();
            log.error("Test Scenario: {} failed.", testName);
            try {
                byte[] png = ScreenshotsUtil.capture(page, testName);
                ExtentListeners.attachScreenshot(png, "Screenshot on failure");
            } catch (Exception e) {
                log.warn("Could not take screenshot for {}: {}", testName, e.getMessage());
            }
        }

        if (page != null) {
            page.close();
            pageThreadLocal.remove();
        }

        BrowserContext context = contextThreadLocal.get();
        if (context != null) {
            context.close();
            contextThreadLocal.remove();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownBrowser() {
        Browser browser = browserThreadLocal.get();
        if (browser != null) {
            browser.close();
            browserThreadLocal.remove();
        }

        Playwright playwright = playwrightThreadLocal.get();
        if (playwright != null) {
            playwright.close();
            playwrightThreadLocal.remove();
        }
    }

}
