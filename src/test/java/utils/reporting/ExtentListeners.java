package utils.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentListeners implements ITestListener, ISuiteListener {
    private static final String REPORTS_DIR = "reports";
    private static final String fileName = "Report_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".html";

    private static final ExtentReports extentRep =
            ExtentManager.createInstance(Paths.get(REPORTS_DIR, fileName).toString());

    // One ExtentTest per thread, because the suite runs classes in parallel.
    private static final ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    public static ExtentTest getExtent() {
        return testReport.get();
    }

    @Override
    public void onTestStart(ITestResult testResult) {
        testReport.set(createTest(testResult));
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " PASSED" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        getOrCreateTest(testResult).pass(m);
        testReport.remove();
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " FAILED" + "</b>";
        ExtentTest test = getOrCreateTest(testResult);

        if (testResult.getThrowable() != null) {
            test.fail(testResult.getThrowable());
        }

        Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
        test.log(Status.FAIL, m);
        testReport.remove();
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        String methodName = testResult.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " SKIP" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        getOrCreateTest(testResult).skip(m);
        testReport.remove();
    }

    @Override
    public void onFinish(ISuite suite) {
        extentRep.flush();
    }

    private static ExtentTest createTest(ITestResult testResult) {
        return extentRep.createTest(testResult.getTestClass().getName()
                + " @TestCase : " + testResult.getMethod().getMethodName());
    }

    // A test skipped because of a failed configuration method never gets onTestStart.
    private static ExtentTest getOrCreateTest(ITestResult testResult) {
        ExtentTest test = testReport.get();
        return test != null ? test : createTest(testResult);
    }
}
