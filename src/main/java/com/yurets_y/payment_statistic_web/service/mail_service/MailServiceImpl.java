package com.yurets_y.payment_statistic_web.service.mail_service;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {


    @Value("${service.mail.protocol}")
    private String protocol;
    @Value("${service.mail.host}")
    private String host;
    @Value("${service.mail.port}")
    private String port;
    @Value("${service.mail.userName}")
    private String userName;
    @Value("${service.mail.password}")
    private String password;
    @Value("${service.mail.sourceEmail}")
    private String sourceEmail;


    @Override
    public List<MultipartFile> readFromMail() {
        return null;
    }

    private Properties getServerProperties() {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));

        return properties;
    }

    public void downloadEmails() {
        Properties properties = getServerProperties();
        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();

            for (int i = messages.length -1 ; i >= 0; i--) {
                Message msg = messages[i];



                InternetAddress[] fromAddress = (InternetAddress[]) msg.getFrom();
                String from = fromAddress[0].getAddress();
                if(from.equals(sourceEmail)){
//                    writePart(msg);
//                    TODO Реализовать выгрузку сообщений, содержащих перечни
                }
//                String subject = msg.getSubject();
//                String toList = parseAddresses(msg
//                        .getRecipients(RecipientType.TO));
//                String ccList = parseAddresses(msg
//                        .getRecipients(RecipientType.CC));
//                String sentDate = msg.getSentDate().toString();
//
//                String contentType = msg.getContentType();
//                String messageContent = "";
//
//                if (contentType.contains("text/plain")
//                        || contentType.contains("text/html")) {
//                    try {
//                        Object content = msg.getContent();
//                        if (content != null) {
//                            messageContent = content.toString();
//                        }
//                    } catch (Exception ex) {
//                        messageContent = "[Error downloading content]";
//                        ex.printStackTrace();
//                    }
//                }
//
//
//                // print out details of each message
//                System.out.println("Message #" + (i + 1) + ":");
//                System.out.println("\t From: " + from);
//                System.out.println("\t To: " + toList);
//                System.out.println("\t CC: " + ccList);
//                System.out.println("\t Subject: " + subject);
//                System.out.println("\t Sent Date: " + sentDate);
//                System.out.println("\t Message: " + messageContent);
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
