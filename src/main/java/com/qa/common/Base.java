package com.qa.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Base {

    public Properties prop;

    public Base(){
        try {
            prop = new Properties();
            FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir")+
                    "/src/main/resources/Config/common.properties");
            prop.load(inputStream);

        }catch (FileNotFoundException e){

        }catch (IOException e){

        }
    }
}
