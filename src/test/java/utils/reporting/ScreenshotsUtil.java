package utils.reporting;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ScreenshotsUtil {
    private static final Path SCREENSHOTS_DIR = Paths.get("reports", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private ScreenshotsUtil() {
    }

    public static byte[] capture(Page page, String name) {
        Path file = SCREENSHOTS_DIR.resolve(name + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png");
        byte[] png = page.screenshot(new Page.ScreenshotOptions()
                .setPath(file)
                .setFullPage(true));
        log.info("Screenshot saved: {}", file.toAbsolutePath());
        return png;
    }
}
