package com.qa.common;

import com.qa.util.Events;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ApiHelper {
    CloseableHttpResponse response = null;
    CloseableHttpClient httpClient = null;

    //1.GET Method
    public static JSONObject get(String url) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();//It creates one client connection and returns CloseableHttpClient Class Object
        HttpGet httpGet = new HttpGet(url);//Get Call
        CloseableHttpResponse response = httpClient.execute(httpGet);//Hit the http get request
        int statusCode = response.getStatusLine().getStatusCode();//to get status code
        Events.info("Status Code ---->" + statusCode,true);

        String output =  EntityUtils.toString(response.getEntity(),"UTF-8");
        Events.info("String output :" + output,true);

        JSONObject jsonResponse = new JSONObject(output);// to convert to json
        Events.info("Json response from API ----> " + output,true);

        Header[] headersArray = response.getAllHeaders();// to get the headers

        HashMap<String,String> allHeaders = new HashMap<>();
        for (Header header : headersArray){
            allHeaders.put(header.getName(),header.getValue());
        }
        Events.info("Headers Array from API ----> " + allHeaders,true);

        return jsonResponse;

    }


}
