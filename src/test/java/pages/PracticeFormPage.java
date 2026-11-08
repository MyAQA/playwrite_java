package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import models.User;
import utils.StatesCities;

import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static utils.reporting.ExtentSteps.step;

@Slf4j
public class PracticeFormPage extends BasePage {

    private static final String PRACTICE_FORM_HEADER = "Practice Form";
    public static final String PRACTICE_FORM_URL_PART = "automation-practice-form";

    private static final String FIRST_NAME_INPUT = "//input[@id='firstName']";
    private static final String LAST_NAME_INPUT = "//input[@id='lastName']";
    private static final String EMAIL_INPUT = "//input[@id='userEmail']";
    private static final String MALE_RADIO_BTN = "//input[@type='radio' and @value='Male']";
    private static final String FEMALE_RADIO_BTN = "//input[@type='radio' and @value='Female']";
    private static final String OTHER_RADIO_BTN = "//input[@type='radio' and @value='Other']";
    private static final String PHONE_INPUT = "//input[@id='userNumber']";
    private static final String DOB_INPUT = "//input[@id='dateOfBirthInput']";
    private static final String SUBJECT_INPUT = "//input[@id='subjectsInput']";
    private static final String ADDRESS_FIELD = "//textarea[@id='currentAddress']";
    private static final String SPORTS_CHECKBOX = "//label[text()='Sports']/../input";
    private static final String MUSIC_CHECKBOX = "//label[text()='Music']/../input";
    private static final String READING_CHECKBOX = "//label[text()='Reading']/../input";
    private static final String STATE_DROPDOWN = "//input[@id='react-select-3-input']";
    private static final String CITY_DROPDOWN = "//input[@id='react-select-4-input']";
    private static final String SUBMIT_BTN = "//button[@id='submit']";
    private static final String THANKS_SUBMITING_FORM_MODAL = ".modal-content";
    private static final String MODAL_WINDOW_HEADER = "Thanks for submitting the form";

    private static final String FORMS_WITH_ERRORS = ".form-control:invalid";

    public PracticeFormPage(Page page) {
        super(page);
    }

    public void selectState(StatesCities state) {
        step("Selecting State " + state.getName(), () -> {
                    super.click(STATE_DROPDOWN);
                    super.type(STATE_DROPDOWN, state.getName());
                    page.locator(STATE_DROPDOWN).fill(state.getName());
                    page.locator("div[id^='react-select-3-option']")
                            .filter(new Locator.FilterOptions().setHasText(state.getName()))
                            .click();
                }
        );
    }

    public void firstNameInput(String firstName) {
        step("Filling Firstname " + firstName, () -> {
                    type(FIRST_NAME_INPUT, firstName);
                }
        );
    }

    public void lastNameInput(String lastName) {
        step("Filling Lastname " + lastName, () -> {
                    type(LAST_NAME_INPUT, lastName);
                }
        );

    }

    public void emailInput(String email) {
        step("Filling Email " + email, () -> {
                    type(EMAIL_INPUT, email);
                }
        );
    }

    public void selectSexRadio(String sex) {
        step("Selecting gender " + sex, () -> {
                    if (sex.toLowerCase().equals("male")) {
                        click(MALE_RADIO_BTN);
                    } else if (sex.toLowerCase().equals("female")) {
                        click(FEMALE_RADIO_BTN);
                    } else {
                        click(OTHER_RADIO_BTN);
                    }
                }
        );

    }

    public void phoneInput(String phoneNumber) {
        step("Filling phone number " + phoneNumber, () -> {
                    type(PHONE_INPUT, phoneNumber);
                }
        );
    }

    public void selectDOB(String dob) {
        step("Selecting Date of Birth " + dob, () -> {
                    type(DOB_INPUT, dob);
                    page.keyboard().press("Enter");
                }
        );
    }

    public void selectSubject(String subject) {

        step("Filling subject text " + (subject.length() < 30 ? subject : ""), () -> {
                    type(SUBJECT_INPUT, subject);
                    page.locator(".subjects-auto-complete__option")
                            .filter(new Locator.FilterOptions().setHasText(subject))
                            .first()
                            .click();
                }
        );

    }

    public void inputAddress(String address) {
        step("Filling address " + address, () -> {
                    type(ADDRESS_FIELD, address);
                }
        );
    }


    public void selectHobbies(String... hobbies) {
        step("Selecting Hobbies " + hobbies, () -> {
            for (String hobby : hobbies) {
                if (hobby.toLowerCase().equals("sports")) {
                    click(SPORTS_CHECKBOX);
                    assertTrue(page.locator(SPORTS_CHECKBOX).isChecked(), "Sports checkbox was not selected");
                } else if (hobby.toLowerCase().equals("music")) {
                    click(MUSIC_CHECKBOX);
                    assertTrue(page.locator(MUSIC_CHECKBOX).isChecked(), "Music checkbox was not selected");
                } else if (hobby.toLowerCase().equals("reading")) {
                    click(READING_CHECKBOX);
                    assertTrue(page.locator(READING_CHECKBOX).isChecked(), "Reading checkbox was not selected");
                } else {
                    log.error("Unsupported hobby provided: {}", hobby);
                }

            }
        });
    }


    public void selectCity(StatesCities.City city) {

        step("Selecting City " + city.getName(), () -> {
            super.click(CITY_DROPDOWN);
            super.type(CITY_DROPDOWN, city.getName());
            page.locator(CITY_DROPDOWN).fill(city.getName());
            page.locator("#city div[id*='-option-']")
                    .filter(new Locator.FilterOptions().setHasText(city.getName()))
                    .first()
                    .click();
        });

    }

    public void clickSubmit() {
        step("Clicking Submit button ", () -> {
                    click(SUBMIT_BTN);
                }
        );
    }

    public void fillInUserFormWithGivenObject(User user) {
        firstNameInput(user.firstName());
        lastNameInput(user.lastName());
        emailInput(user.email());
        selectSexRadio(user.sex());
        phoneInput(user.phoneNumber());
        selectDOB(user.dob());
        selectSubject(user.subject().get(new Random().nextInt(user.subject().size())));
        inputAddress(user.getAddress());
        selectHobbies(user.hobby().getName());
        selectState(user.state());
        selectCity(user.city());
    }

    public Locator getFormsWithErrors() {
        return page.locator(FORMS_WITH_ERRORS);
    }

    public void checkIfThanksModalAppeared() {
        step("Verify 'Thanks for submitting the form' modal", () -> {
            Locator modalWindow = page.locator(THANKS_SUBMITING_FORM_MODAL);

            step("Modal is visible", () -> {
                waitForVisible(modalWindow);
                assertTrue(modalWindow.isVisible(), "Thanks modal is not visible");
            });

            step("Modal header is '" + MODAL_WINDOW_HEADER + "'", () ->
                    assertEquals(modalWindow.locator("[class='modal-header']").innerText(),
                            MODAL_WINDOW_HEADER, "Modal header"));
        });
    }

    public boolean isGenderRadioHasError() {
        boolean isMaleHasError = (boolean) page.locator(MALE_RADIO_BTN).evaluate("el => el.validity.valueMissing");
        boolean isFemaleHasError = (boolean) page.locator(FEMALE_RADIO_BTN).evaluate("el => el.validity.valueMissing");
        boolean isOtherHasError = (boolean) page.locator(OTHER_RADIO_BTN).evaluate("el => el.validity.valueMissing");
        return isMaleHasError && isFemaleHasError && isOtherHasError;
    }

}
