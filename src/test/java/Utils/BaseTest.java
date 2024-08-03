package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners(ExtentReportListener.class)
public class BaseTest {

    public Playwright playwright;
    public APIRequestContext requestContext;
    public APIRequest request;
    public static ExtentReports extentReports;
    public static ExtentTest extentTest;

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
    }

    @AfterClass
    public void tearDown() {
        playwright.close();
    }
}
