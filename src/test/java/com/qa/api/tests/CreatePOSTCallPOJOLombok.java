package com.qa.api.tests;

import Utils.Log;
import Utils.PlaywrightWrapper;
import com.api.data.Users;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreatePOSTCallPOJOLombok extends PlaywrightWrapper {

    @Test
    public void createUserTest() throws IOException {

        // Create users object using builder pattern
        Users users = Users.builder()
                .name("Test Automation")
                .email(getRandomEmail())
                .gender("male")
                .status("active")
                .build();

        // Post request to create a user
        APIResponse apiPostResponse = postRequest("https://gorest.co.in//public/v2/users", users);

        // Assert POST response
        assertResponseStatus(apiPostResponse, 201);
        Assert.assertEquals(apiPostResponse.statusText(), "Created");
        assertResponseBody(apiPostResponse, apiPostResponse.text());


        // Deserialize response to Users object
        Users actUser = deserializeResponse(apiPostResponse.text(), Users.class);

        // Assert user details
        Assert.assertEquals(actUser.getName(), users.getName());
        Assert.assertEquals(actUser.getEmail(), users.getEmail());
        Assert.assertEquals(actUser.getGender(), users.getGender());
        Assert.assertEquals(actUser.getStatus(), users.getStatus());
        Assert.assertNotNull(actUser.getId());

        // Capture id from the post JSON response
        String userID = actUser.getId();
        Log.info("user id : " + userID);

        // Validate userID format (ensure it's not null or empty)
        Assert.assertNotNull(userID, "User ID should not be null");
        Assert.assertFalse(userID.trim().isEmpty(), "User ID should not be empty");

        // GET request to fetch the same user by id
        String getUserUrl = "https://gorest.co.in/public/v2/users/" + userID;
        Log.info("Fetching user with URL: " + getUserUrl);
        APIResponse apiGetResponse = getRequest(getUserUrl);

//        // GET request to fetch the same user by id
//        APIResponse apiGetResponse = getRequest("https://gorest.co.in//public/v2/users/"+ userID);

        // Assert GET response
        assertResponseStatus(apiGetResponse, 200);
        Assert.assertEquals(apiGetResponse.statusText(), "OK");
        assertResponseBody(apiPostResponse, apiPostResponse.text());

//        // Get GET response text
//        String getResponseText = apiPostResponse.text();
//
//        // Log the GET response for debugging
//        Log.info("GET Response Text: " + getResponseText);

        assertResponseBodyContains(apiPostResponse, userID);
        assertResponseBodyContains(apiPostResponse, users.getName());
//        Assert.assertTrue(getResponseText.contains(users.getName()));


        // Assert response contains userID and name
//        assertResponseText(apiGetResponse, userID);
//        assertResponseText(apiGetResponse, users.getName());
    }
}
