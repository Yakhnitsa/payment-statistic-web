package com.yurets_y.payment_statistic_web.service.mail_service;

import java.util.Date;

public interface MailService {


    void scanFromMailToTempDb(Date lastUpdate);
}
