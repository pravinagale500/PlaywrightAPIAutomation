package com.qa.api.tests;

import Utils.PlaywrightWrapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class APIResponseHeaderTest extends PlaywrightWrapper {

    @Test
    public void getHeaders() {
        //Request 1
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiResponse.status();
        System.out.println("response status code: " + statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(apiResponse.ok(), true);
//Using Map:
        Map<String, String> headersMap = apiResponse.headers();
        headersMap.forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println("Total response headers: " + headersMap.size());
        Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");
        Assert.assertEquals(headersMap.get("connection"), "close");

//Using list:
        List<HttpHeader> headerlist = apiResponse.headersArray();
        for (HttpHeader e : headerlist) {
            System.out.println(e.name + " : " + e.value);
        }
    }

}
