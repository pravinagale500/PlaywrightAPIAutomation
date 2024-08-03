package com.qa.api.tests;

import Utils.BaseTest;
import Utils.PlaywrightWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CreateUserWithJsonFile extends PlaywrightWrapper {

    @Test
    public void createUserTest() throws IOException {

        //get json file
        byte[] fileBytes = null;
        File file = new File("./src/test/datafiles/user.json");
        fileBytes = Files.readAllBytes(file.toPath());

        //Post call: Create a user
        APIResponse apiPostResponse = requestContext.post("https://gorest.co.in//public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json; charset=utf-8")
                        .setHeader("Authorization", "Bearer 376a47e79e7c2df00651cce3e5a5b1c98a190a84bb931a25eacdd441e0c94382")
                        .setData(fileBytes));
        System.out.println(apiPostResponse.status());
        Assert.assertEquals(apiPostResponse.status(), 201);
        Assert.assertEquals(apiPostResponse.statusText(), "Created");
        System.out.println(apiPostResponse.text());

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode postJsonResponse = objectMapper.readTree(apiPostResponse.body());
        System.out.println(postJsonResponse.toPrettyString());

        //Capture id from the post json response
        String userID = postJsonResponse.get("id").asText();
        System.out.println("user id : " + userID);


        //GET Call: Fetch the same user by id:
        System.out.println("==============get call response=================");

        APIResponse apiGetResponse = requestContext.get("https://gorest.co.in//public/v2/users/" + userID,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 376a47e79e7c2df00651cce3e5a5b1c98a190a84bb931a25eacdd441e0c94382"));

        Assert.assertEquals(apiGetResponse.status(), 200);
        Assert.assertEquals(apiGetResponse.statusText(), "OK");

        System.out.println(apiGetResponse.text());

        Assert.assertTrue(apiGetResponse.text().contains(userID));
        Assert.assertTrue(apiGetResponse.text().contains("testingPravin"));


//        Assert.assertTrue(apiGetResponse.text().contains(emailId));

    }

}
