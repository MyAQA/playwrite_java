package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    protected static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("chromium") String browserName, @Optional("false") boolean headless) {

        BrowserEnum browserSelected = BrowserEnum.valueOf(browserName.toUpperCase());
        Browser browser = BrowserFactory.createBrowser(browserSelected, headless);

        BrowserContext context = browser.newContext();
        contextThreadLocal.set(context);

        Page page = context.newPage();
        pageThreadLocal.set(page);
    }

    protected Page getPage() {
        return pageThreadLocal.get();
    }

    @AfterMethod
    public void tearDown() {
        BrowserContext context = contextThreadLocal.get();

        if(context != null) {
            context.close();
            contextThreadLocal.remove();
        }
        pageThreadLocal.remove();

        BrowserFactory.closeBrowser();
    }

}
