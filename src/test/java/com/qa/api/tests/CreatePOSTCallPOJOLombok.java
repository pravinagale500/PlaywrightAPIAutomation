package com.qa.api.tests;

import Utils.BaseTest;
import Utils.PlaywrightWrapper;
import com.api.data.UserWithPOJO;
import com.api.data.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

public class CreatePOSTCallPOJOLombok extends PlaywrightWrapper {

    @Test
    public void createUserTest() throws IOException {

        //Create users object: using builder pattern:
       Users users = Users.builder().name("Test Automation")
                .email(getRandomEmail())
                .gender("male")
                .status("active").build();

        //Post call: Create a user
        APIResponse apiPostResponse = requestContext.post("https://gorest.co.in//public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json; charset=utf-8")
                        .setHeader("Authorization", "Bearer 376a47e79e7c2df00651cce3e5a5b1c98a190a84bb931a25eacdd441e0c94382")
                        .setData(users));
        System.out.println("==============get post response=================");
        System.out.println(apiPostResponse.status());
        Assert.assertEquals(apiPostResponse.status(), 201);
        System.out.println(apiPostResponse.statusText());
        Assert.assertEquals(apiPostResponse.statusText(), "Created");

        String responseText = apiPostResponse.text();
        System.out.println(responseText);

        //convert response text/json to POJO -- deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        UserWithPOJO actUser = objectMapper.readValue(responseText, UserWithPOJO.class);
        System.out.println(actUser);

        Assert.assertEquals(actUser.getName(),users.getName());
        Assert.assertEquals(actUser.getEmail(),users.getEmail());
        Assert.assertEquals(actUser.getGender(),users.getGender());
        Assert.assertEquals(actUser.getStatus(),users.getStatus());
        Assert.assertNotNull(actUser.getId());

        //Capture id from the post json response
        String userID = actUser.getId();
        System.out.println("user id : " + userID);

        //GET Call: Fetch the same user by id:
        System.out.println("==============get call response=================");
        APIResponse apiGetResponse = requestContext.get("https://gorest.co.in//public/v2/users/" + userID,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 376a47e79e7c2df00651cce3e5a5b1c98a190a84bb931a25eacdd441e0c94382"));

        System.out.println(apiGetResponse.status());
        Assert.assertEquals(apiGetResponse.status(), 200);
        System.out.println(apiGetResponse.statusText());
        Assert.assertEquals(apiGetResponse.statusText(), "OK");

        System.out.println(apiGetResponse.text());

        Assert.assertTrue(apiGetResponse.text().contains(userID));
        Assert.assertTrue(apiGetResponse.text().contains(users.getName()));

    }

}
