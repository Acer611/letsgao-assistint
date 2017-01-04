/*
package com.umessage.letsgo.assistant.config;

import org.apache.catalina.connector.Connector;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;

*/
/**
 * Created by gaofei on 2016/12/21.
 *//*

@ConfigurationProperties(prefix = "custom.tomcat.https")
public class TomcatHttpsProperties {

    private int port;
    private boolean secure;
    private String scheme;
    private boolean ssl;
    private File keystore;
    private String keystorePassword;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public File getKeystore() {
        return keystore;
    }

    public void setKeystore(File keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }


    public void configureConnector(Connector connector) {

        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try{
            //connector.setPort(port);
            connector.setSecure(secure);
            connector.setScheme(scheme);
            protocol.setSSLEnabled(ssl);
            //File keystoreFile = null;
            ClassPathResource classPathResource = new ClassPathResource(".keystore");
            InputStream inputStream = classPathResource.getInputStream();
            File keystoreFile =  new File(".keystore");
            FileUtils.copyInputStreamToFile(inputStream, keystoreFile);
            protocol.setKeystoreFile( keystoreFile.getAbsolutePath());
            // keystoreFile =  new ClassPathResource(".keystore").getFile();
            // protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(keystorePassword);
            connector.setProperty("SSLEnabled", String.valueOf(ssl));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
*/
