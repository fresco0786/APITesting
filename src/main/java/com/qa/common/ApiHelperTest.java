/*
package com.qa.common;



import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qa.util.Events;
import com.sun.xml.internal.ws.client.RequestContext;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import org.slf4j.MDC;


*/
/**
 * This class is used to call https apis
 * @author Administrator
 *//*

@SuppressWarnings("deprecation")
public class ApiHelperTest extends HttpEntityEnclosingRequestBase {
    protected int statusCode = -1;
    private String serviceURL = "";
    private String token = "";
    private ArrayList<org.apache.http.message.BasicHeader> headerList = new ArrayList<org.apache.http.message.BasicHeader>();
    List<BasicNameValuePair> formParameters = new ArrayList<BasicNameValuePair>();
    public  final String METHOD_NAME = "DELETE";
    CloseableHttpResponse response = null;
    CloseableHttpClient httpclient = null;
    private String uploadFilePath = "";
    private String uploadFileName = "";
    private InputStream in = null;
    private String textParameterName = null;
    private String textParameterValue = null;
    private String satusLine="";
    private boolean addProductUIDHeader=false;
    private RequestConfig requestConfig;

    */
/**
     * This method clears all the static data in this class. This mothod is
     * added as part of a workaround.	 *
     *//*

    public void cleanup() {
        statusCode = -1;
        serviceURL = "";
        token = "";
        headerList = new ArrayList<org.apache.http.message.BasicHeader>();
        formParameters = new ArrayList<BasicNameValuePair>();
        httpclient = null;
        response = null;
        uploadFilePath = "";
        uploadFileName = "";
        textParameterName = null;
        textParameterValue = null;
        in = null;
    }

    public String getMethod() {
        return METHOD_NAME;
    }

    public ApiHelperTest(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public ApiHelperTest(final URI uri) {
        super();
        setURI(uri);
    }

    public ApiHelperTest() {
        super();

    }

    */
/**
     * Get Status code
     *
     * @return
     *//*

    public int getStatusCode() {
        // Log.Message(String.valueOf(this.statusCode), LogLevel.INFO);
        return this.statusCode;
    }

    */
/**
     * This method adds the header values for rest api request
     *
     * @param headername
     *            - name of the attribute to be set
     * @param headervalue
     *            - value of the attribute to be set
     * @return nothing
     * @throws Exception
     *//*

    public void addHeader(String headername, String headervalue) {
        org.apache.http.message.BasicHeader header = new BasicHeader(
                headername, headervalue);
        headerList.add(header);
    }

    public void addFormParameter(String name, String value) {
        formParameters.add(new BasicNameValuePair(name, value));
    }

    */
/**
     * Set Service URL
     *
     * @param serviceURL
     * @throws Exception
     *//*

    public void setServiceURL(String serviceURL){
        this.serviceURL = serviceURL;
    }

    */
/**
     * Method gets the stream from the file and sets in the inputstream variable
     * "in"
     *
     * @param filePath
     *//*

    public void setStream(String filePath) {
        try {
            Events.info("getting file stream from file " + filePath);
            File file = new File(filePath);
            // We are using the canWrite() method to check whether we can
            // modified file content.
            if (file.canRead()) {
                Events.info("File is Readable!");
            } else {
                Events.info("File is NOT in read only mode!");
                file.setReadable(true);
                Events.info("Changed file to Readable!");
            }
            in = new FileInputStream(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    */
/**
     * This method accepts input json and perform HTTP DELETE operation using
     * the message body. It returns the output response.
     *
     * @param inputJsonString
     * @return result - output response.
     * @throws Exception
     *//*

    public String deleteRequestWithBody(String inputJsonString)
            throws Exception {
        //String sessionId = "SAEP_UAT_" + CommonHelper.getRandomNumeric(10);
        String sessionId=getSessionId();
        try {
            boolean sessionIdReplaced = false;
            if (!headerList.isEmpty()) {
                for (org.apache.http.message.BasicHeader header : headerList) {
                    String headerName = header.getName();
                    if ("x-epmp-session-id".equals(headerName)) {
                        headerList.remove(header);
                        header = new BasicHeader("x-epmp-session-id", sessionId);
                        headerList.add(header);
                        sessionIdReplaced = true;
                        break;
                    }
                }
            }
            if (!headerList.isEmpty()) {
                if (!sessionIdReplaced) {
                    org.apache.http.message.BasicHeader header = new BasicHeader("x-epmp-session-id", sessionId);
                    headerList.add(header);
                }
            }

            Events.info("deleteRequestWithBody serviceURL: " + this.serviceURL);
            Events.info("deleteRequestWithBody headerList: " + this.headerList);
            Events.info("deleteRequestWithBody mediaType: " + ContentType.APPLICATION_JSON);
            Events.info("deleteRequestWithBody requestType: " + "delete");
            Events.info("deleteRequestWithBody inputString: " + inputJsonString);
            if (null == httpclient) {
                httpclient = buildHttpsClient();
            }
            Events.info("Getting service path:" + this.serviceURL);
            ApiHelperTest httpDelete = new ApiHelperTest(this.serviceURL);
            StringEntity input = new StringEntity(inputJsonString,
                    ContentType.APPLICATION_JSON);
            if (!this.token.isEmpty()) {
                httpDelete.setHeader("Authorization", this.token);
            }
            if (headerList != null) {
                for (org.apache.http.message.BasicHeader header : headerList) {
                    httpDelete.setHeader(header);
                }
            }
            httpDelete.setEntity(input);
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            String result = "";
            this.statusCode = response.getStatusLine().getStatusCode();
            if (response.getEntity() != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (response.getEntity().getContent())));

                String output = "";
                while ((output = br.readLine()) != null) {
                    result = result + output;
                }
            }
            Events.info("Status code for response is - " + this.statusCode);
            Events.info("Returning result -  " + result);
            if (!(this.statusCode==200 || this.statusCode==201 || this.statusCode==202 || this.statusCode==204))
            {
                MDC.put("serviceMethod","DELETE");
                String message = "url:" + this.serviceURL + "\n" +
                        //"method:" + "DELETE" + "\n" +
                        "statuscode:" + response.getStatusLine().getStatusCode() + "\n" +
                        "response:" + result ;
                throw new Exception(message);
            }
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            Events.info("Exception occured during the request: "+e);
            MDC.put("serviceMethod","DELETE");
            String message = "sessionId:" + sessionId + "\n"
                    + "url:" + this.serviceURL + "\n" +
                    //	"method:" + "DELETE" + "\n" +
                    "statuscode:" + response.getStatusLine().getStatusCode() + "\n" +
                    "response:" + e.getMessage() ;
            throw new Exception(message,e);

        }
    }


    */
/**
     * This method will create a httpsClient which will ignore certificate & it
     * will not cause SSLHandshakeException while requesting to a https url.
     *
     * @return
     *//*

    public CloseableHttpClient buildHttpsClient() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    return true;
                }
            });
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build(),
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    */
/**
     * This method gets the response for request types such as get, post, put,
     * delete and head *
     *
     * @param mediaType
     *            - Application/Json or
     * @param requestType
     *            - get, post, put, delete,head
     * @param inputString
     *            - input string in Json format
     * @param mediaType
     * @param requestType
     * @param inputString
     * @return
     * @throws Exception
     *//*

    public String getResponse(String mediaType, String requestType,
                              String inputString) throws Exception {

        //String sessionId = "SAEP_UAT_"+CommonHelper.getRandomNumeric(10);
        int restStatusCode = 0;
        String sessionId = getSessionId();
        boolean sessionIdReplaced=false;
        if (!headerList.isEmpty()) {
            for (org.apache.http.message.BasicHeader header : headerList) {
                String headerName = header.getName();
                if("x-epmp-session-id".equals(headerName)){
                    headerList.remove(header);
                    header=new BasicHeader("x-epmp-session-id", sessionId);
                    headerList.add(header);
                    sessionIdReplaced=true;
                    break;
                }
            }
        }
        if (!headerList.isEmpty()) {
            if(!sessionIdReplaced){
                org.apache.http.message.BasicHeader header=new BasicHeader("x-epmp-session-id", sessionId);
                headerList.add(header);
            }
        }
        Events.info("Request serviceURL: " + serviceURL);
        Events.info("Request headerList: " + headerList);
        Events.info("Request mediaType: " + mediaType);
        Events.info("Request requestType: " + requestType);
        if (!serviceURL.contains("response_type=token"))
            Events.info("Request body: " + inputString);

        String result = "";
        try {
            if (null == httpclient) {
                httpclient = buildHttpsClient();
            }

            switch (requestType.toLowerCase()) {
                case "get":
                    HttpGet getRequest = new HttpGet(this.serviceURL);
                    if (!this.token.isEmpty()) {
                        getRequest.setHeader("Authorization", this.token);
                    }
                    if (!headerList.isEmpty()) {
                        for (org.apache.http.message.BasicHeader header : headerList) {
                            getRequest.setHeader(header);
                        }
                    }
                    getRequest.setHeader("Content-Type", mediaType);

                    response = httpclient.execute(getRequest);
                    break;

                case "put":
                    HttpPut putRequest = new HttpPut(this.serviceURL);

                    requestConfig = RequestConfig.custom()
                            .setSocketTimeout(60*1000)
                            .setConnectTimeout(60*1000)
                            .setConnectionRequestTimeout(60*1000)
                            .build();
                    putRequest.setConfig(requestConfig);
                    if (!this.token.isEmpty()) {
                        putRequest.setHeader("Authorization", this.token);
                    }
                    putRequest.setHeader("Content-Type", mediaType);
                    if (!headerList.isEmpty()) {
                        for (org.apache.http.message.BasicHeader header : headerList) {
                            putRequest.setHeader(header);
                        }
                    }
                    if (!inputString.isEmpty()) {
                        StringEntity input = new StringEntity(inputString);
                        putRequest.setEntity(input);
                    }

                    response = httpclient.execute(putRequest);

                    break;

                case "post":
                    HttpPost postRequest = new HttpPost(this.serviceURL);
                    requestConfig = RequestConfig.custom()
                            .setSocketTimeout(90*1000)
                            .setConnectTimeout(90*1000)
                            .setConnectionRequestTimeout(90*1000)
                            .build();
                    postRequest.setConfig(requestConfig);
                    if (!this.token.isEmpty()) {
                        postRequest.setHeader("Authorization", this.token);
                    }
                    if (!headerList.isEmpty()) {
                        for (org.apache.http.message.BasicHeader header : headerList) {
                            postRequest.setHeader(header);
                        }
                    }
                    if (this.formParameters.isEmpty()
                            && (this.uploadFileName.equals("") && this.uploadFilePath
                            .equals(""))) {
                        postRequest.setHeader("Content-Type", mediaType);
                        StringEntity input = new StringEntity(inputString, "UTF-8");
                        postRequest.setEntity(input);
                    } else {
                        postRequest.setEntity(new UrlEncodedFormEntity(
                                this.formParameters, "UTF-8"));
                    }
                    if (!(this.uploadFileName.equals("") && this.uploadFilePath
                            .equals(""))) {
                        Events.info("Setting uploadFileName:" + uploadFileName);
                        MultipartEntityBuilder builder = MultipartEntityBuilder
                                .create();
                        builder.addBinaryBody(this.uploadFileName, new File(
                                uploadFilePath));
                        if (this.textParameterName != null)
                        {
                            Events.info("Setting " + this.textParameterName+":" + this.textParameterValue);
                            builder.addTextBody(this.textParameterName, this.textParameterValue);
                        }
                        HttpEntity httpEntity = builder.build();
                        postRequest.setEntity(httpEntity);
                        Events.info("executing request "
                                + postRequest.getRequestLine());
                        Events.info("Setting uploadFilePath:" + this.uploadFilePath);
                    }
                    if (in != null) {
                        postRequest.setHeader("Accept-Language", "en");
                        ContentType contentType = ContentType
                                .create("application/octet-stream");
                        InputStreamEntity httpEntity = new InputStreamEntity(in,
                                in.available(), contentType);
                        httpEntity.setContentType(contentType.getMimeType());
                        postRequest.setEntity(httpEntity);
                        Events.info("executing request "
                                + postRequest.getRequestLine());
                    }
                    response = httpclient.execute(postRequest);
                    Events.info("Response of the request:"
                            + response.getStatusLine());
                    break;
                case "delete":
                    HttpDelete deleteRequest = new HttpDelete(this.serviceURL);
                    if (!this.token.isEmpty()) {
                        deleteRequest.setHeader("Authorization", this.token);
                    }
                    if (!headerList.isEmpty()) {
                        for (org.apache.http.message.BasicHeader header : headerList) {
                            deleteRequest.setHeader(header);
                        }
                    }
                    deleteRequest.setHeader("Content-Type", mediaType);
                    response = httpclient.execute(deleteRequest);
                    break;
            }

            HttpEntity entity = response.getEntity();
            restStatusCode = response.getStatusLine().getStatusCode();
            this.statusCode = response.getStatusLine().getStatusCode();
            this.satusLine = response.getStatusLine().getReasonPhrase();
            if(entity==null){
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (entity.getContent())));

            String output = "";
            while ((output = br.readLine()) != null) {
                result = result + output;
            }
            //Events.info("getResponse result: " + result);
            Events.info("Request statusCode: " + this.statusCode);
            MDC.put("serviceMethod",requestType.toUpperCase());
            if (!(this.statusCode==200 || this.statusCode==201 || this.statusCode==202 || this.statusCode==204))
            {
                //String message = "sessionId:" + sessionId + "\n"
                String message = "url:" + this.serviceURL + "\n" +
                        //"method:" + requestType.toUpperCase() + "\n" +
                        "statuscode:" + restStatusCode + "\n" +
                        "response:" + result ;
                throw new Exception(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Events.info("Exception occured during the request: "+e);
            MDC.put("serviceMethod",requestType.toUpperCase());
            String message = "url:" + this.serviceURL + "\n" +
                    //"method:" + requestType.toUpperCase() + "\n" +
                    "statuscode:" + restStatusCode + "\n" +
                    "response:" + e.getMessage() ;
            throw new Exception(message,e);

        } finally {
            if (in != null)
                try {
                    in.close();
                    in=null;
                } catch (IOException e) {
                    e.printStackTrace();
                    Events.info("Exception occured while closing the stream in...");
                }
            if (httpclient != null)
                try {
                    httpclient.close();
                    httpclient=null;
                } catch (IOException e) {
                    e.printStackTrace();
                    Events.info("Exception occured while closing the httpclient...");
                }
        }
        Events.info("result: "+result);
        return result;
    }

    public void uploadFileInRequest(String uploadFileName, String uploadFilePath) {
        this.uploadFileName = uploadFileName;
        this.uploadFilePath = uploadFilePath;
    }

    public void addtextParameter(String name, String value) {
        textParameterName = name;
        textParameterValue = value;
    }

    */
/**
     * Download a file	 *
     * @param downloadUrl
     * @param destinationFile
     * @throws IOException
     *//*

    public static void downloadFile(String downloadUrl, String destinationFile)
            throws IOException {
        URL url = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        FileOutputStream out = new FileOutputStream(destinationFile);
        byte[] buf = new byte[1024];
        int n = in.read(buf);
        while (n >= 0) {
            out.write(buf, 0, n);
            n = in.read(buf);
        }
        out.flush();
        out.close();
    }

    public void setHeaderList(ArrayList<BasicHeader> headerList2) {
        headerList=headerList2;
        if (addProductUIDHeader)
        {

            BasicHeader header = new BasicHeader("x-epmp-product-uid", StringConstants.TEST);
            headerList.add(header);
        }
    }

    public JsonNode getResponseNode(String contentType, String requestType, final ObjectNode requestJson) throws Exception, JsonProcessingException{
        String response = getResponse(contentType, requestType, requestJson.toString());
        ObjectMapper mapper=new ObjectMapper();
        JsonNode responseNode = mapper.createObjectNode();
        ((ObjectNode)responseNode).put(StringConstants.STATUS, getStatusCode());
        if(response!=null&&!response.equals("")){
            JsonNode repponse = JsonHelper.getJsonNodeFromString(response);
            ((ObjectNode)responseNode).put(StringConstants.RETURN, repponse);
        }
        return responseNode;
    }

    */
/**
     * Get Request with no parameters
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     *//*

    public JsonNode httpRequestGet() throws Exception {
        String response = getResponse(StringConstants.CONTENT_TYPE, StringConstants.REQUEST_TYPE_GET, "");
        return getResponse(response);
    }


    public JsonNode httpRequestPost(String payload) throws Exception {
        String response = getResponse(StringConstants.CONTENT_TYPE, StringConstants.REQUEST_TYPE_POST, payload);
        return getResponse(response);
    }


    public JsonNode httpRequestPut(String payload) throws Exception {
        String response = getResponse(StringConstants.CONTENT_TYPE, StringConstants.REQUEST_TYPE_PUT, payload);
        return getResponse(response);
    }


    public JsonNode httpRequestDelete() throws Exception {
        String response = getResponse(StringConstants.CONTENT_TYPE, StringConstants.REQUEST_TYPE_DELETE,"");
        return getResponse(response);
    }

    */
/**
     * This method return response json and status code as jsonnode.
     * e.g. response : {"status":200,"return":""}
     * @param contentType
     * @param requestType
     * @param requestJson
     * @return
     * @throws JsonProcessingException
     * @throws IOException
     *//*

    public JsonNode httpRequest(String contentType, String requestType, ObjectNode requestJson) throws Exception {
        String requestJsonStr;
        if(requestJson==null){
            requestJsonStr="";
        }else{
            requestJsonStr=requestJson.toString();
        }
        String response = getResponse(contentType, requestType, requestJsonStr);
        return getResponse(response);
    }

    */
/**
     * Get the Status line from response status
     * @return
     *//*

    public String getResponseSatusLine()
    {
        return this.satusLine;
    }

    private JsonNode getResponse(String response) {
        ObjectMapper mapper=new ObjectMapper();
        JsonNode responseNode = mapper.createObjectNode();
        ((ObjectNode)responseNode).put(StringConstants.STATUS, getStatusCode());
        if(response!=null&&!response.equals("")){
            JsonNode jsonNode;
            try {
                jsonNode = JsonHelper.getJsonNodeFromString(response);
                ((ObjectNode) responseNode).put(StringConstants.RETURN, jsonNode);
            } catch (IOException e) {
                ((ObjectNode) responseNode).put(StringConstants.RETURN, response);
            }
        }
        return responseNode;
    }

    private String getSessionId()
    {
        */
/*RequestContext loggedInCustomerContext = RequestContextHolder.getContext();
        if (loggedInCustomerContext != null)
            return loggedInCustomerContext.getSessionId();
        else*//*

            return "Test_" + CommonHelper.getRandomNumeric(10);
    }
}*/
