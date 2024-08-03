package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExtentReportListener extends BaseTest implements ITestListener {

    private static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "./test-output/";
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
    // Get the current date and time in milliseconds
    static long currentTimeMillis = System.currentTimeMillis();
    // Format the date and time
    static SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmssSSS");
    static String formattedDateTime = dateFormat.format(new Date(currentTimeMillis));
    private static final String FILE_NAME = "extentReport_" + formattedDateTime + ".html";
    private static ExtentReports extent = init();

    public static String Concatenate = ".";

    private static ExtentReports init() {
        Path path = Paths.get(OUTPUT_FOLDER);
        // if directory exists?
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                // fail to create directory
                e.printStackTrace();
            }
        }
        extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
        reporter.config().setReportName("Playwright API Automation");
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("System", "Default");
        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Author", "API");
        extentReports.setSystemInfo("Team", "API Automation Team");
        extentReports.setSystemInfo("Customer Name", "GO REST");
        return extentReports;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        Log.info("Test Suite started!");

    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String qualifiedName = result.getMethod().getQualifiedName();
        int last = qualifiedName.lastIndexOf(".");
        int mid = qualifiedName.substring(0, last).lastIndexOf(".");
        String className = qualifiedName.substring(mid + 1, last);
        Log.info(methodName + " started!");
        extentTest = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        extentTest.assignCategory(result.getTestContext().getSuite().getName());
        /*
         * methodName = StringUtils.capitalize(StringUtils.join(StringUtils.
         * splitByCharacterTypeCamelCase(methodName), StringUtils.SPACE));
         */
        extentTest.assignCategory(className);
        test.set(extentTest);
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    public synchronized void onTestSuccess(ITestResult result) {
        Log.info((result.getMethod().getMethodName() + " passed!"));
        test.get().pass("Test passed");
        test.get().pass(result.getMethod().getMethodName());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

//    public String passFailScreenshot(){
//        String screenshotName = Concatenate + takeScreenshot();
//        return screenshotName;
//    }

    public synchronized void onTestFailure(ITestResult result) {
        Log.info((result.getMethod().getMethodName() + " failed!"));
        test.get().fail(result.getMethod().getMethodName());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    public synchronized void onTestSkipped(ITestResult result) {
        Log.info((result.getMethod().getMethodName() + " skipped!"));
        test.get().skip(result.getMethod().getMethodName());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Log.info(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

//    public void reportStep(String desc, String status) {
//        Log.info(desc);
//
//    }

//    public void attachScreenShot() {
//        synchronized (test) {
//            test.get().info(MediaEntityBuilder.createScreenCaptureFromPath(passFailScreenshot()).build());
//        }
//    }


    public void reportStep(String desc, String status) {
        synchronized (test) {
            // Log step description and status on Extent Report
            if (status.equalsIgnoreCase("pass")) {
                Log.info(desc);
                test.get().pass(desc);
            } else if (status.equalsIgnoreCase("fail")) {
                Log.info(desc);
                test.get().fail(desc);
                throw new RuntimeException("See the reporter for details.");
            } else if (status.equalsIgnoreCase("warning")) {
                Log.info(desc);
                test.get().warning(desc);
            } else if (status.equalsIgnoreCase("skipped")) {
                Log.info(desc);
                test.get().skip(desc);
            } else if (status.equalsIgnoreCase("INFO")) {
                Log.info(desc);
                test.get().info(desc);
            }
        }
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        File reportfile = new File(OUTPUT_FOLDER + FILE_NAME);
        Log.info(("Test Suite is ending!"));

        // Calculate total execution time
        long totalTimeMillis = System.currentTimeMillis() - context.getStartDate().getTime();
        long totalTimeSeconds = totalTimeMillis / 1000;
        long hours = totalTimeSeconds / 3600;
        long minutes = (totalTimeSeconds % 3600) / 60;
        long seconds = totalTimeSeconds % 60;
        String totalTimeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        // Add total execution time to the Extent Report
        extent.setReportUsesManualConfiguration(true); // enable manual configuration
        extent.addTestRunnerOutput("Total Execution Time:  ");
        extent.addTestRunnerOutput(totalTimeFormatted);

        extent.flush();
        try {
            Desktop.getDesktop().browse(reportfile.toURI());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        test.remove();
    }
}

