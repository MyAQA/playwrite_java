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

@Slf4j
public class UploadDownloadTests extends BaseTest {

    @Test
    public void downloadSampleFile() throws IOException {
        log.info("Start of the test.");
        getPage().navigate(baseUrl);

        LandingPage landingPage = new LandingPage(getPage());
        landingPage.openFormsPage();

        UploadDownloadPage upDowPage = landingPage.leftMenu.openUploadDownloadPage();
        upDowPage.waitForUrlContains(upDowPage.UPLOAD_DOWNLOAD_URL_PART);
        upDowPage.refreshPage();

        Path downloadedFile = upDowPage.clickDownload();
        assertEquals(downloadedFile.getFileName().toString(), upDowPage.EXPECTED_DOWNLOAD_FILE_NAME);
        assertTrue(Files.size(downloadedFile) > 0);
    }

    @Test public void uploadFileFromResources() throws URISyntaxException {
        log.info("Start of the test.");
        getPage().navigate(baseUrl);

        LandingPage landingPage = new LandingPage(getPage());
        landingPage.openFormsPage();

        UploadDownloadPage upDowPage = landingPage.leftMenu.openUploadDownloadPage();
        upDowPage.waitForUrlContains(upDowPage.UPLOAD_DOWNLOAD_URL_PART);

        Path file = Paths.get(getClass().getClassLoader().getResource("files/upload-test.jpg").toURI());
        upDowPage.uploadFile(file);

        assertEquals(upDowPage.getUploadedFilePath(), upDowPage.getExpectedUploadFilePath("upload-test.jpg"));
    }
}
