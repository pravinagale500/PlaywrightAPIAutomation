package com.qa.api.tests;

import Utils.PlaywrightWrapper;
import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class APIDisposeTest extends PlaywrightWrapper {

    @Test
    public void disposeResponseText(){
        //Request 1
        APIResponse apiResponse1 = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiResponse1.status();
        System.out.println("response status code: " + statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(apiResponse1.ok(), true);

        String statusResText = apiResponse1.statusText();
        System.out.println(statusResText);

        System.out.println("---print api response with plain text---");
        System.out.println(apiResponse1.text());

        apiResponse1.dispose();//will dispose only response body but status code, url, status text will remain same
        System.out.println("---print api response after dispose with plain text---");

        try {
            System.out.println(apiResponse1.text());
        }catch (PlaywrightException e){
            System.out.println("api response body is disposed");
        }


        int statusCode1 = apiResponse1.status();
        System.out.println("response status code: " + statusCode1);

        String statusResText1 = apiResponse1.statusText();
        System.out.println(statusResText1);

        System.out.println("response url:" +apiResponse1.url());

        //Request 2
        APIResponse apiResponse2 = requestContext.get("https://reqres.in/api/users/2");
        System.out.println("get responsebody for 2nd request:");
        System.out.println("status code:" + apiResponse2.status());
        System.out.println("response body:" + apiResponse2.text());
        //request context dispose

        requestContext.dispose();
        try {
            System.out.println(apiResponse1.text());
        }catch (PlaywrightException e){
            System.out.println("api response1 body is disposed");
        }
        try {
            System.out.println(apiResponse2.text());
        }catch (PlaywrightException e){
            System.out.println("api response2 body is disposed");
        }

    }

}
