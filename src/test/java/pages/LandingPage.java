package pages;

import com.microsoft.playwright.Page;

public class LandingPage extends BasePage {
    private static final String CATEGORY_FORMS = "//div[@class='category-cards']/a[@href='/forms']";


    public LandingPage(Page page) {
        super(page);
    }

    public void openFormsPage() {
        click(CATEGORY_FORMS);
    }
}
