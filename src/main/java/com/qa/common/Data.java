package com.qa.common;

import com.qa.util.Events;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

public class Data {
    public static Properties properties;

    static{
        initialize();
    }

    public static void initialize(){
        try{
            properties= new Properties();
            InputStream inputStream = new FileInputStream(System.getProperty("user.dir")+
                    "/src/main/resources/Config/common.properties");
            properties.load(inputStream);
            //		properties=getResourcePropertiesFile(ResourceFileNames.COMMON_PROPERTIES);
            //System.out.println("Properties: "+properties);
            Events.info("Data class is initialized");
            PrintWriter writer = new PrintWriter(System.out);
            properties.list(writer);


        }catch(Throwable e){
            System.out.println("Properties: "+properties);
            e.printStackTrace();
        }
    }

//	@PostConstruct
//	public void setVaultProperties() {
//		Data.properties.setProperty("system_password",system_password);
//
//		byte[] uatUserSecretsArray = Base64.decodeBase64(Data.getProperty(DataKeys.SYSTEM_PASSWORD).getBytes());
//		Data.uatUserSecrets= new String(uatUserSecretsArray);
//	}

    public static String getProperty(String dataKey) {
        return properties.getProperty(dataKey);
    }

    public static String getProperty(String dataKey, String defaultValue) {
        return properties.getProperty(dataKey, defaultValue);
    }

    public static Properties getResourcePropertiesFile(final String filename) {
        System.out.println("getResourcePropertiesFile bigins");

        try {
            Data data=new Data();
            InputStream resourceFile = data.getClass().getResourceAsStream(filename);
            System.out.println("resourceFile: "+resourceFile);

            Properties prop=new Properties();
            prop.load(resourceFile);
            Object userId = prop.get("");
            System.out.println("userId: "+userId);

            return prop;
        } catch (final IOException e) {
            System.out.println("getResourcePropertiesFile Exception: "+e);
            Events.error(e.getMessage());
            return null;
        }
    }

    //	public static void main(String args[]){
    //		String r3URL = Data.getProperty(DataKeys.EPMP_BASE_URL);
    //		System.out.println("r3URL: "+r3URL);
    //	}

}

