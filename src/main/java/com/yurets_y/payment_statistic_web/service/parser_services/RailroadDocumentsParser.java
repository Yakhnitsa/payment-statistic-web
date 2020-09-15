package com.yurets_y.payment_statistic_web.service.parser_services;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public interface RailroadDocumentsParser {
    RailroadDocument parseFromFile(File file) throws IOException, ParseException;
}
