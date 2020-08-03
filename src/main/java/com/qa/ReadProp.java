package com.qa;

import com.qa.common.Data;
import com.qa.util.Events;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadProp {
    public static  void main(String[] args) throws IOException {
        /*Properties prop = new Properties();
        FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir")+
                "/src/main/resources/Config/common.properties");
        prop.load(inputStream);*/
        String data = Data.getProperty("api_gateway_url");
        Events.info("Property  :"+ data,true);
    }
}
