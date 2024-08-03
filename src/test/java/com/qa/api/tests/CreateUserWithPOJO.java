package com.qa.api.tests;

import Utils.BaseTest;
import Utils.PlaywrightWrapper;
import com.api.data.UserWithPOJO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;


public class CreateUserWithPOJO extends PlaywrightWrapper {

    @Test
    public void createUserTest() throws IOException {

        //Create user object;
        UserWithPOJO user = new UserWithPOJO("AutoTest", getRandomEmail(), "male", "active");

        //Post call: Create a user
        APIResponse apiPostResponse = requestContext.post("https://gorest.co.in//public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json; charset=utf-8")
                        .setHeader("Authorization", "Bearer 376a47e79e7c2df00651cce3e5a5b1c98a190a84bb931a25eacdd441e0c94382")
                        .setData(user));
        System.out.println(apiPostResponse.status());
        Assert.assertEquals(apiPostResponse.status(), 201);
        Assert.assertEquals(apiPostResponse.statusText(), "Created");

        String responseText = apiPostResponse.text();
        System.out.println(responseText);

        //convert response text/json to POJO -- deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        UserWithPOJO actUser = objectMapper.readValue(responseText, UserWithPOJO.class);
        System.out.println(actUser);

        Assert.assertEquals(actUser.getName(),user.getName());
        Assert.assertEquals(actUser.getEmail(),user.getEmail());
        Assert.assertEquals(actUser.getGender(),user.getGender());
        Assert.assertEquals(actUser.getStatus(),user.getStatus());
        Assert.assertNotNull(actUser.getId());
    }

}
