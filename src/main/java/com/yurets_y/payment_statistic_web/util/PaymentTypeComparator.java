package com.yurets_y.payment_statistic_web.util;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component("payment-type-comparator")
public class PaymentTypeComparator implements Comparator<String> {
    private final List<String> typesOrder = Arrays.asList(
            "Відправлення",
            "Відправлення - міжнародне сполучення",
            "Прибуття",
            "Прибуття - імпорт",
            "Відомості плати за користування вагонами",
            "Накопичувальні карточки",
            "Коригування сум нарахованих платежів",
            "Штрафи",
            "Платіжні доручення"
    );

    @Override
    public int compare(String s1, String s2) {
        int obj1TypeIndex = typesOrder.indexOf(s1);
        obj1TypeIndex = obj1TypeIndex < 0 ? typesOrder.size() : obj1TypeIndex;
        int obj2TypeIndex = typesOrder.indexOf(s2);
        obj2TypeIndex = obj2TypeIndex < 0 ? typesOrder.size() : obj2TypeIndex;
        return Integer.compare(obj1TypeIndex,obj2TypeIndex);
    }

}
