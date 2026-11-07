package pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import pages.components.LeftNavigationMenu;
import utils.reporting.ExtentListeners;
import utils.reporting.ScreenshotsUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class BasePage {

    protected final Page page;
    private static final double DEFAULT_TIMEOUT_MS = 10_000;
    public LeftNavigationMenu leftMenu;

    private static final Path DOWNLOAD_DIR = Paths.get("target", "downloads");

    public BasePage(Page page) {
        this.page = page;

        if (leftMenu == null) {
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
        } catch (Throwable error) {
            log.error("Failed to click on element [{}]: {}", locator, error.getMessage());
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

    public void refreshPage() {
        page.reload();
    }

    public String getTitle() {
        return page.title();
    }

    public String getCurrentUrl() {
        return page.url();
    }

    public boolean isLocatorVisible(String locator) {
        return page.locator(locator).isVisible();
    }

    public void takeScreenshot(String name) {
        byte[] png = ScreenshotsUtil.capture(page, name);
        ExtentListeners.attachScreenshot(png, name);
    }

    protected void waitForAttached(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(DEFAULT_TIMEOUT_MS));
    }

    public void uploadFile(String locator, Path file) {
        try {
            Locator input = page.locator(locator);
            waitForAttached(input);
            input.setInputFiles(file);
            log.debug("Uploaded file [{}] into: {}", file.toAbsolutePath(), locator);
        } catch(Throwable error) {
            log.error("Failed to upload file [{}] into [{}]: {}", file, locator, error.getMessage());
            throw error;
        }
    }

    public Path downloadFile(String locator) {
        Download download = page.waitForDownload(() -> click(locator));

        String failure = download.failure();
        if(failure != null) {
            log.error("Download from [{}] failed: {}", locator, failure);
            throw new IllegalStateException("Download failed: " + failure);
        }

        Path savedFile = DOWNLOAD_DIR.resolve(download.suggestedFilename());
        download.saveAs(savedFile);
        log.debug("Downloaded file saved to: {}", savedFile.toAbsolutePath());
        return savedFile;
    }
}
