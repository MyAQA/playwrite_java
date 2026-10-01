package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import pages.components.LeftNavigationMenu;

@Slf4j
public class BasePage {

    protected final Page page;
    private static final double DEFAULT_TIMEOUT_MS = 10_000;
    public LeftNavigationMenu leftMenu;

    public BasePage(Page page) {
        this.page = page;

        if (leftMenu == null ) {
           leftMenu = new LeftNavigationMenu(page);
        }
    }

    public void navigateTo(String url) {
        log.info("Navigating to url: " + url);
        page.navigate(url);
        page.waitForLoadState();
    }

    public void click(String locator) {
        try {
            Locator element = page.locator(locator);
            waitForVisible(element);
            page.locator(locator).click();
            log.debug("Clicked {}", locator);
        } catch(Throwable error) {
            log.error("Failed to click on element [{}]: {}",locator, error.getMessage());
            throw error;
        }
    }

    public void type(String locator, String text) {
        Locator element = page.locator(locator);
        waitForVisible(element);
        element.fill(text);
        log.debug("Typed '{}' into: {}", text, locator);
    }

    public String getText(String locator) {
        Locator element = page.locator(locator);
        waitForVisible(element);
        return element.textContent().trim();
    }

    protected void waitForVisible(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(DEFAULT_TIMEOUT_MS));
    }

    public void waitForUrlContains(String fragment) {
        page.waitForURL(url -> url.contains(fragment));
    }

    public String getTitle() {
        return page.title();
    }

    public String getCurrentUrl() {
        return page.url();
    }
}
