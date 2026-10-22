package utils.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentManager {
    private static ExtentReports extentReports;

    public static ExtentReports createInstance(String filePath) {
        Path reportPath = Paths.get(filePath);
        try {
            if (reportPath.getParent() != null) {
                Files.createDirectories(reportPath.getParent());
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create reports directory for " + filePath, e);
        }

        String reportName = reportPath.getFileName().toString();
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath.toString());

        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle(reportName);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName(reportName);

        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);

        return extentReports;
    }
}
