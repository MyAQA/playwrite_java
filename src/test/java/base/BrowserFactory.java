package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

    private BrowserFactory() {}

    public static Browser createBrowser(BrowserEnum type, boolean headless) {
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);

        Browser browser = switch (type) {
            case CHROMIUM -> playwright.chromium().launch(options);
            case FIREFOX -> playwright.firefox().launch(options);
            case EDGE -> playwright.chromium().launch(options.setChannel("msedge"));
        };

        browserThreadLocal.set(browser);
        return browser;
    }

    public static Browser getBrowser() {
        return browserThreadLocal.get();
    }

    public static void closeBrowser() {
        Browser browser = browserThreadLocal.get();
        if (browser != null) {
            browser.close();
            browserThreadLocal.remove();
        }

        Playwright playwright = playwrightThreadLocal.get();
        if(playwright != null) {
            playwright.close();
            playwrightThreadLocal.remove();
        }

    }

}
