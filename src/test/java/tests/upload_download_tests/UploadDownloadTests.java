package tests.upload_download_tests;

import base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.UploadDownloadPage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static utils.Constants.baseUrl;
import static utils.reporting.ExtentSteps.step;

@Slf4j
public class UploadDownloadTests extends BaseTest {

    @Test(description = "Download sample file Test",
            groups = {"smoke", "files"})
    public void downloadSampleFile() throws IOException {
        log.info("Download sample file Test START");
        getPage().navigate(baseUrl);

        LandingPage landingPage = new LandingPage(getPage());
        landingPage.openFormsPage();

        UploadDownloadPage upDowPage = landingPage.leftMenu.openUploadDownloadPage();
        upDowPage.waitForUrlContains(upDowPage.UPLOAD_DOWNLOAD_URL_PART);
        upDowPage.refreshPage();

        Path downloadedFile = upDowPage.clickDownload();

        step("Verify downloaded file name: " + downloadedFile.getFileName().toString(),
                () -> assertEquals(downloadedFile.getFileName().toString(), upDowPage.EXPECTED_DOWNLOAD_FILE_NAME));
        long size = Files.size(downloadedFile);
        step("Verify downloaded file size is greater than 0: " + size,
                () -> assertTrue(size > 0));
    }

    @Test(description = "Upload file Test",
            groups = {"smoke", "files"})
    public void uploadFileFromResources() throws URISyntaxException {
        log.info("Start of the test.");
        getPage().navigate(baseUrl);

        LandingPage landingPage = new LandingPage(getPage());
        landingPage.openFormsPage();

        UploadDownloadPage upDowPage = landingPage.leftMenu.openUploadDownloadPage();
        upDowPage.waitForUrlContains(upDowPage.UPLOAD_DOWNLOAD_URL_PART);

        Path file = Paths.get(getClass().getClassLoader().getResource("files/upload-test.jpg").toURI());
        upDowPage.uploadFile(file);

        String expectedPath = upDowPage.getExpectedUploadFilePath("upload-test.jpg");
        step("Verify uploaded file path", () -> assertEquals(upDowPage.getUploadedFilePath(), expectedPath));
    }
}
