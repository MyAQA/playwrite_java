package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.*;

@Slf4j
public class BaseTest {
    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

    protected static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void setUpBrowser(@Optional("chromium") String browserName, @Optional("false") boolean headless) {
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        BrowserEnum browserSelected = BrowserEnum.valueOf(browserName.toUpperCase());
        Browser browser = BrowserFactory.createBrowser(playwright, browserSelected, headless);
        browserThreadLocal.set(browser);

        log.info("Browser [{}] launched (headless={}) on thread {}", browserName, headless, Thread.currentThread().getId());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpContext() {
        BrowserContext context = browserThreadLocal.get().newContext();
        contextThreadLocal.set(context);
        pageThreadLocal.set(context.newPage());
    }

    protected Page getPage() {
        return pageThreadLocal.get();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownContext(ITestResult result) {
        Page page = pageThreadLocal.get();

        if (result.getStatus() == ITestResult.FAILURE && page != null) {
            // might add screenshots here
            log.error("Test Scenario: {} failed.", result.getTestName());
        }

        if (page != null) {
            page.close();
            pageThreadLocal.remove();
        }

        BrowserContext context = contextThreadLocal.get();
        if(context != null) {
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
