package pages;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static utils.reporting.ExtentSteps.step;

import java.nio.file.Path;

@Slf4j
public class UploadDownloadPage extends BasePage {

    public static final String UPLOAD_DOWNLOAD_URL_PART = "upload-download";
    private static final String FAKE_PATH_PREFIX = "C:\\fakepath\\";
    public static final String EXPECTED_DOWNLOAD_FILE_NAME = "sampleFile.jpeg";

    public static final String DOWNLOAD_BTN = "//a[@id='downloadButton']";
    public static final String UPLOAD_BTN = "//input[@id='uploadFile']";
    private static final String UPLOADED_FILE_PATH = "//p[@id='uploadedFilePath']";

    public UploadDownloadPage(Page page) {
        super(page);
    }

    public Path clickDownload() {
        log.info("Downloading sample file");
        return step("Click on 'Download' button and starting downloading a file", () -> downloadFile(DOWNLOAD_BTN));
    }

    public void uploadFile(Path file) {
        log.info("Uploading file: {}", file.getFileName());
        step("Uploading file: " + file.getFileName(), () -> uploadFile(UPLOAD_BTN, file));
    }

    public String getUploadedFilePath() {
        return step("Reading uploaded file path", () -> getText(UPLOADED_FILE_PATH));
    }

    public String getExpectedUploadFilePath(String fileName) {
        return FAKE_PATH_PREFIX + fileName;
    }
}