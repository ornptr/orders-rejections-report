package org.ornarowicz.refuseReport.service.emailService;

import org.ornarowicz.refuseReport.service.configuration.EmailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

@Component
public class EmailConnection {

    @Autowired
    private EmailConfiguration emailConfig;

    Folder connect() throws MessagingException {
        String protocol = emailConfig.getProtocol();
        String host = emailConfig.getHost();
        String port = emailConfig.getPort();
        Session session = Session.getInstance(getServerProperties(protocol, host, port));
        // connects to the message store
        Store store = session.getStore(protocol);
        store.connect(emailConfig.getUserName(), emailConfig.getPassword());
        // opens the inbox folder
        Folder inbox = store.getFolder(emailConfig.getInbox());
        inbox.open(Folder.READ_ONLY);
        return inbox;
    }

    void disconnect(Folder inboxP) throws MessagingException {
        if (inboxP != null) {
            inboxP.close();
            inboxP.getStore().close();
        }
    }

    private Properties getServerProperties(String protocol, String host, String port) {
        Properties properties = new Properties();
        // server setting
        properties.put(String.format("mail.%s.host", protocol), protocol + "." + host);
        properties.put(String.format("mail.%s.port", protocol), port);
        // SSL setting
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));
        return properties;
    }

}