package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {

    private BrowserFactory() {}

    public static Browser createBrowser(Playwright playwright, BrowserEnum type, boolean headless) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);

        return switch (type) {
            case CHROMIUM -> playwright.chromium().launch(options);
            case FIREFOX -> playwright.firefox().launch(options);
            case EDGE -> playwright.chromium().launch(options.setChannel("msedge"));
        };

    }

}
