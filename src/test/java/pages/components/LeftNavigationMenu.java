package pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AllArgsConstructor;
import pages.PracticeFormPage;
import pages.UploadDownloadPage;

import static utils.reporting.ExtentSteps.step;

@AllArgsConstructor
public class LeftNavigationMenu {

    private static final String FORM_PRACTICE_FORM = "//a[@href='/automation-practice-form']";
    private static final String UPLOAD_DOWNLOAD_EL = "//a[@href='/upload-download']";

    private final Page page;

    private void expandGroup(MenuGroup menuPoint) {
        Locator loc = page.locator(menuPoint.xpath);
        loc.click();
    }

    public PracticeFormPage openPracticeForm() {
        step("Opening Practice Form", () -> {
            Locator practiceForm = page.locator(FORM_PRACTICE_FORM);
            if (!practiceForm.isVisible()) {
                expandGroup(MenuGroup.FORMS);
            }
            practiceForm.click();
        });

        return new PracticeFormPage(page);
    }

    public UploadDownloadPage openUploadDownloadPage() {
        step("Opening Upload Download Form", () -> {
            Locator uploadDownloadEl = page.locator(UPLOAD_DOWNLOAD_EL);
            if (!uploadDownloadEl.isVisible()) {
                expandGroup(MenuGroup.ELEMENTS);
            }
            uploadDownloadEl.click();
        });

        return new UploadDownloadPage(page);
    }

}
