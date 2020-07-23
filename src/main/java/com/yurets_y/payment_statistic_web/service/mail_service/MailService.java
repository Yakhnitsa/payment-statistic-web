package com.yurets_y.payment_statistic_web.service.mail_service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MailService {
    List<MultipartFile> readFromMail();
}
