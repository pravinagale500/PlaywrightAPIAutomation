package Utils;

import com.api.data.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;

public class PlaywrightWrapper extends ExtentReportListener {
    public static String emailId;
    private APIResponse apiResponse;
    private ObjectMapper objectMapper;

    public PlaywrightWrapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String getRandomEmail() {
        emailId = "TestAuto" + System.currentTimeMillis() + "@gmail.com";
        return emailId;
    }

    public APIResponse postRequest(String url, Users data) {
        try {
            // Convert Users object to JSON string
            String jsonData = objectMapper.writeValueAsString(data);

            // Perform the POST request
            apiResponse = requestContext.post(url,
                    RequestOptions.create()
                            .setHeader("Content-Type", "application/json; charset=utf-8")
                            .setHeader("Authorization", "Bearer 376a47e79e7c2df00651cce3e5a5b1c98a190a84bb931a25eacdd441e0c94382")
                            .setData(jsonData));

            reportStep("The Post url is : " + url, "pass");

            // Log and return the response
//            Log.info("POST Response Status: " + apiResponse.status());
//            Log.info("POST Response Status Text: " + apiResponse.statusText());
//            Log.info("POST Response Body: " + apiResponse.text());
            reportStep("The Post Response Status is : " + apiResponse.status(), "pass");
            reportStep("The Post Response Status Text is : " + apiResponse.statusText(), "pass");
            reportStep("The Post Response Body is : " + apiResponse.text(), "pass");
            return apiResponse;
        } catch (Exception e) {
            reportStep("Exception occurred: " + e.getMessage(), "fail");
            throw new RuntimeException(e);
        }
    }

    public APIResponse getRequest(String url) {
        try {
            apiResponse = requestContext.get(url,
                    RequestOptions.create()
                            .setHeader("Content-Type", "application/json; charset=utf-8")
                            .setHeader("Authorization", "Bearer 376a47e79e7c2df00651cce3e5a5b1c98a190a84bb931a25eacdd441e0c94382"));
            reportStep("The GET url is : " + url, "pass");
//            Log.info("GET Response Status: " + apiResponse.status());
//            Log.info("GET Response Status Text: " + apiResponse.statusText());
//            Log.info("GET Response Body: " + apiResponse.text());
            reportStep("The GET Response Status is : " + apiResponse.status(), "pass");
            reportStep("The GET Response Status Text is : " + apiResponse.statusText(), "pass");
            reportStep("The GET Response Body is : " + apiResponse.text(), "pass");
            return apiResponse;
        } catch (PlaywrightException e) {
            reportStep("PlaywrightException: \n" + e.getMessage(), "fail");
            throw e;
        }
    }

    public Users deserializeResponse(String responseText, Class<Users> clazz) {
        try {
            return objectMapper.readValue(responseText, clazz);
        } catch (Exception e) {
            Log.info("Failed to deserialize response: " + e.getMessage());
            Log.info("Response: " + responseText);
            throw new RuntimeException(e);
        }
    }

    public void assertResponseStatus(APIResponse response, int expectedStatus) {
        Assert.assertEquals(response.status(), expectedStatus);
//        Log.info("Assertion passed: Expected status " + expectedStatus + " matches actual status.");
        reportStep("Assertion passed: Expected status " + expectedStatus + " matches actual status.", "pass");
    }

    public void assertResponseText(APIResponse response, String expectedText) {
        Assert.assertEquals(response.text(), expectedText);
//        Log.info("Assertion passed: Response contains expected text: " + expectedText);
        reportStep("Assertion passed: Response contains expected text: " + expectedText, "pass");
    }


    public void assertResponseBody(APIResponse response, String expectedText) {
        Assert.assertEquals(response.text(), expectedText);
        reportStep("Assertion passed: Response contains expected body: " + expectedText, "pass");
    }

    public void assertResponseBodyContains(APIResponse response, String expectedText) {
        Assert.assertTrue(response.text().contains(expectedText));
        reportStep("Assertion passed: Response contains expected body: " + expectedText, "pass");
    }
}
