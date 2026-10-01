package pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;
import pages.PracticeFormPage;

@AllArgsConstructor
public class LeftNavigationMenu {

    private static final String FORM_PRACTICE_FORM = "//a[@href='/automation-practice-form']";

    private final Page page;

    private void expandGroup(MenuGroup menuPoint) {
        Locator loc = page.locator(menuPoint.xpath);
        loc.click();
    }

    public PracticeFormPage openPracticeForm() {
        Locator practiceForm = page.locator(FORM_PRACTICE_FORM);
        if (!practiceForm.isVisible()) {
            expandGroup(MenuGroup.FORMS);
        }
        page.locator(FORM_PRACTICE_FORM).click();
        return new PracticeFormPage(page);
    }

}
