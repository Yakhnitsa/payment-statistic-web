package com.yurets_y.payment_statistic_web.service.parser_services;



import com.yurets_y.payment_statistic_web.entity.PaymentList;

import java.io.File;
import java.io.IOException;

public interface DocParser {

    PaymentList parseFromFile(File file) throws IOException;

}
