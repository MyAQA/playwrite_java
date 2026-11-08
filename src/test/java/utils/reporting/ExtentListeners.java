package utils.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.*;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ExtentListeners implements ITestListener, ISuiteListener {
    private static final String REPORTS_DIR = "reports";
    private static final String fileName = "Report_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".html";

    private static final ExtentReports extentRep =
            ExtentManager.createInstance(Paths.get(REPORTS_DIR, fileName).toString());

    private static final ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    public static ExtentTest getExtent() {
        return testReport.get();
    }

    @Override
    public void onTestStart(ITestResult testResult) {
        ExtentTest test = createTest(testResult);
        test.assignCategory(testResult.getMethod().getGroups());
        testReport.set(test);
        ExtentSteps.startTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " PASSED" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        getOrCreateTest(testResult).pass(m);
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " FAILED" + "</b>";
        ExtentTest test = getOrCreateTest(testResult);

        Throwable t = testResult.getThrowable();
        if (t != null && !ExtentSteps.isReported(t)) {
            test.fail(t);
        }

        Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
        test.log(Status.FAIL, m);
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " SKIP" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        getOrCreateTest(testResult).skip(m);
    }

    @Override
    public void onFinish(ISuite suite) {
        extentRep.flush();
    }

    private static ExtentTest createTest(ITestResult testResult) {
        ITestNGMethod method = testResult.getMethod();
        String name = (method.getDescription() != null && !method.getDescription().isBlank())
                ? method.getDescription()
                : method.getMethodName();
        return extentRep.createTest(" @TestCase : " + name);
    }

    private static ExtentTest getOrCreateTest(ITestResult testResult) {
        ExtentTest test = testReport.get();
        return test != null ? test : createTest(testResult);
    }

    public static void attachScreenshot(byte[] png, String title) {
        ExtentTest test = testReport.get();
        if (test == null) {
            return;
        }
        String base64 = Base64.getEncoder().encodeToString(png);
        test.info(title, MediaEntityBuilder.createScreenCaptureFromBase64String(base64, title).build());
    }
}
