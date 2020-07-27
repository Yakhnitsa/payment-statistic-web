package com.yurets_y.payment_statistic_web.service.mail_service;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.yurets_y.payment_statistic_web.service.TempListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Properties properties;
    private TempListService tempListService;

    private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @PostConstruct
    private void init() {
        properties = new Properties();

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

    }

    @Override
    public void scanFromMailToTempDb(Date lastUpdate) {
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

            for (int i = messages.length - 1; i >= 0; i--) {
                Message msg = messages[i];
                Date messageDate = msg.getSentDate();
                if(messageDate.before(lastUpdate)) {
                    logger.info("Сканирование почты завершено...");
                    break;
                }
                InternetAddress[] fromAddress = (InternetAddress[]) msg.getFrom();
                String from = fromAddress[0].getAddress();
                if (from.equals(sourceEmail)) {
                    writePart(msg);
                }

            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            logger.error("No provider for protocol: " + protocol, ex);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            logger.error("Could not connect to the message store", ex);
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writePart(Part p) throws Exception {
        if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                writePart(mp.getBodyPart(i));
        }

        else if (p.isMimeType("application/octet-stream")) {
            Object o = p.getContent();
            String fileName = getFileNameFromContentType(p.getContentType());
            //Проверка индекса файла и разрешения.
            if(testFileName(fileName) && o instanceof InputStream){
                InputStream is = (InputStream) o;
                MultipartFile multipartFile = getMultipartFileFromInputStream(is,fileName);


                tempListService.putToTempDB(multipartFile);
                logger.info("New paymentList found: " + fileName);
            }
        }
    }

    private boolean testFileName(String fileName) {
        return fileName.startsWith("per_") && fileName.endsWith(".xml");
    }

    /*
     * Конвертация в Multipart для дальнейшего сохранения в TempListService
     */
    private MultipartFile getMultipartFileFromInputStream(InputStream stream, String filename) throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("file", filename, "text/xml", stream);
        return multipartFile;
    }

    private static String getFileNameFromContentType(String contentType) {
        Pattern pattern = Pattern.compile(".+name=(.+)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(contentType);
        if (matcher.matches()) {
            return matcher.group(1);

        }
        return "unknown_file";
    }

    @Autowired
    public void setTempListService(TempListService tempListService) {
        this.tempListService = tempListService;
    }
}
