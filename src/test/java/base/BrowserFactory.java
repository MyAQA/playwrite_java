package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import java.util.List;

public class BrowserFactory {

    private BrowserFactory() {
    }

    public static Browser createBrowser(Playwright playwright, BrowserEnum type, boolean headless) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);

        if (isMaximizable(type, headless)) {
            options.setArgs(List.of("--start-maximized"));
        }

        return switch (type) {
            case CHROMIUM -> playwright.chromium().launch(options);
            case FIREFOX -> playwright.firefox().launch(options);
            case EDGE -> playwright.chromium().launch(options.setChannel("msedge"));
        };

    }

    public static boolean isMaximizable(BrowserEnum type, boolean headless) {
        return !headless && type != BrowserEnum.FIREFOX;
    }

}
