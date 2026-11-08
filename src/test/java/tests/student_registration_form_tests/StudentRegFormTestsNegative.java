package tests.student_registration_form_tests;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import lombok.extern.slf4j.Slf4j;
import models.User;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.PracticeFormPage;

import java.util.Random;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static utils.Constants.baseUrl;
import static utils.reporting.ExtentSteps.step;

@Slf4j
public class StudentRegFormTestsNegative extends BaseTest {


    @DataProvider(name = "userWithMissingRequiredField")
    public static Object[][] userWithMissingRequiredField() {
        return new Object[][]{
                {"First Name", User.withFirstName("")},
                {"Last Name", User.withLastName("")},
                {"Mobile Number", User.withPhoneNumber("")}
        };
    }

    @Test(dataProvider = "userWithMissingRequiredField",
            description = "Student form validation empty required fields Test",
            groups = {"student_form", "smoke", "negative"})
    public void addStudentInFormEmptyRequiredFields(String caseName, User user) {
        log.info("Start of the test: user without " + caseName);
        getPage().navigate(baseUrl);

        LandingPage landingPage = new LandingPage(getPage());

        landingPage.openFormsPage();
        PracticeFormPage practiceFormPage = landingPage.leftMenu.openPracticeForm();
        practiceFormPage.waitForUrlContains(practiceFormPage.PRACTICE_FORM_URL_PART);
        practiceFormPage.refreshPage();


        log.debug("Created test user for testing form: {}", user);
        practiceFormPage.fillInUserFormWithGivenObject(user);
        practiceFormPage.clickSubmit();

        Locator fieldWithErrors = practiceFormPage.getFormsWithErrors();
        step("Verify validation error for empty required field", () -> assertEquals(fieldWithErrors.count(), 1));
        step("Verify validated field name", () -> assertEquals(fieldWithErrors.getAttribute("placeholder"), caseName));
    }

    @Test(description = "Student form validation no Gender selected",
            groups = {"student_form", "smoke", "negative"})
    public void addStudentInFormEmptyGender() {
        log.info("Start of the test: user without Gender");
        getPage().navigate(baseUrl);

        LandingPage landingPage = new LandingPage(getPage());

        landingPage.openFormsPage();
        PracticeFormPage practiceFormPage = landingPage.leftMenu.openPracticeForm();
        practiceFormPage.waitForUrlContains(practiceFormPage.PRACTICE_FORM_URL_PART);
        practiceFormPage.refreshPage();

        User noGenderUser = User.withGender("");
        log.debug("Created test user for testing form: {}", noGenderUser);
        practiceFormPage.firstNameInput(noGenderUser.firstName());
        practiceFormPage.lastNameInput(noGenderUser.lastName());
        practiceFormPage.emailInput(noGenderUser.email());
        practiceFormPage.phoneInput(noGenderUser.phoneNumber());
        practiceFormPage.selectDOB(noGenderUser.dob());
        practiceFormPage.selectSubject(noGenderUser.subject().get(new Random().nextInt(noGenderUser.subject().size())));
        practiceFormPage.inputAddress(noGenderUser.getAddress());
        practiceFormPage.selectHobbies(noGenderUser.hobby().getName());
        practiceFormPage.selectState(noGenderUser.state());
        practiceFormPage.selectCity(noGenderUser.city());
        practiceFormPage.clickSubmit();

        step("Verify gender radio buttons highlighted", () -> assertTrue(practiceFormPage.isGenderRadioHasError()));
    }

}
