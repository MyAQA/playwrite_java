import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaceholderMain {

    public static void main(String[] args) throws InterruptedException {
        Playwright pw = Playwright.create();

        Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page pg = browser.newPage();
        pg.navigate("https://demoqa.com");
        System.out.println(pg.title());
        Thread.sleep(2000);
    }
}
