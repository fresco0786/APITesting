package com.qa.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.qa.common.ApiHelper;
import com.qa.common.Data;
import com.qa.common.UrlConstants;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class APITests {
    String uri;
    String apiUrl;
    String url;

    @BeforeMethod
    public void setUp() {
        uri = Data.getProperty("api_gateway_uri");
        apiUrl = UrlConstants.service_users_url;
        url = uri + apiUrl;

    }
    @Test
    public void getUsersList() throws IOException {
        ApiHelper.get(url);
    }
    @Test
    public void validateSingleUser() throws IOException {
        url=url+"/25";
        ApiHelper.get(url);
    }

}
