package tests.student_registration_form_tests;

import base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import models.User;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.PracticeFormPage;

import static org.testng.AssertJUnit.assertEquals;
import static utils.Constants.baseUrl;

@Slf4j
public class StudentRegFormTestsPositive extends BaseTest {

    @Test
    public void addStudentInFormFullData() throws InterruptedException {
        getPage().navigate(baseUrl);

        LandingPage landingPage = new LandingPage(getPage());

        landingPage.openFormsPage();
        PracticeFormPage practiceFormPage = landingPage.leftMenu.openPracticeForm();
        practiceFormPage.waitForUrlContains(practiceFormPage.PRACTICE_FORM_URL_PART);

        User user1 = User.getGeneratedUser();
        log.debug("Created test user for testing form: {}", user1);
        practiceFormPage.fillInUserFormWithGivenObject(user1);
        practiceFormPage.clickSubmit();

        assertEquals(practiceFormPage.getFormsWithErrors().count(), 0);

    }

}
