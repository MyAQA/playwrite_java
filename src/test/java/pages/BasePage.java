package pages;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasePage {

    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void navigateTo(String url) {
        log.info("Navigating to url: " + url);
        page.navigate(url);
    }

    public void click(String locator) {
        try {
            page.locator(locator).click();
        } catch(Throwable error) {

        }
    }
}
