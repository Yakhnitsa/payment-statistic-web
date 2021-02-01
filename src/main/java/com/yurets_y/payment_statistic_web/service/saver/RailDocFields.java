package com.yurets_y.payment_statistic_web.service.saver;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;

import java.util.function.BiFunction;

public enum RailDocFields {
    ORDER_NUMBER("№ п/п",(doc,index) -> index),
    DOC_NUMBER("№ накладной",(doc,index) -> doc.getDocNumber()),
    DOC_DATE("Дата документа",(doc,index) -> doc.getDocDate()),

    SEND_STATION_NAME("ст. Отправления",(doc,index) -> doc.getSendStation().getRusName()),
    SEND_STATION_CODE("код ст. Отпр.",(doc,index) -> doc.getSendStation().getCode()),
    SEND_STATION_NAME_CODE("(код) ст. Отправления",(doc,index) ->
            String.format("(%s) %s",doc.getSendStation().getCode(),doc.getSendStation().getRusName())
    ),

    RECEIVE_STATION_NAME("ст. Назначения",(doc,index) -> doc.getReceiveStation().getRusName()),
    RECEIVE_STATION_CODE("код ст. Назнач.",(doc,index) -> doc.getReceiveStation().getCode()),
    RECEIVE_STATION_NAME_CODE("(код) ст. Назначения",(doc,index) ->
            String.format("(%s) %s",doc.getReceiveStation().getCode(),doc.getReceiveStation().getRusName())
    ),
//
//    OUT_STATION_NAME("ст. Выхода"),
//    OUT_STATION_CODE("код ст. Выхода."),
//    OUT_STATION_NAME_CODE("(код) ст. Выхода"),
//
//    INN_STATION_NAME("ст. Входа"),
//    INN_STATION_CODE("код ст. Входа"),
//    INN_STATION_NAME_CODE("(код) ст. Входа"),
//
    SENDER_NAME("Отправитель",(doc,index) -> doc.getCargoSender().getName()),
    SENDER_ADDRESS("Адрес отправителя",(doc,index) -> doc.getCargoSender().getAddress()),
    SENDER_RAILROAD_CODE("жд код отправителя",(doc,index) -> doc.getCargoSender().getRailroadCode()),
    SENDER_EDRPU_CODE("код ЕДРПУ отправителя",(doc,index) -> doc.getCargoSender().getEdrpuCode()),
    SENDER_NAME_CODE("(код) Отправитель",(doc,index) ->
            String.format("(%s) %s",doc.getCargoSender().getRailroadCode(),doc.getCargoSender().getName())
    ),
//
    RECEIVER_NAME("Получатель",(doc,index) -> doc.getCargoReceiver().getName()),
    RECEIVER_ADDRESS("Адрес получателя",(doc,index) -> doc.getCargoReceiver().getAddress()),
    RECEIVER_RAILROAD_CODE("ЖД код получателя",(doc,index) -> doc.getCargoReceiver().getRailroadCode()),
    RECEIVER_EDRPU_CODE("код ЕДРПУ получателя",(doc,index) -> doc.getCargoReceiver().getEdrpuCode()),
    RECEIVER_NAME_CODE("(код) Получатель",(doc,index) ->
            String.format("(%s) %s",doc.getCargoReceiver().getRailroadCode(),doc.getCargoReceiver().getName())
    ),
//
    CARGO_NAME("груз",(doc,index) -> doc.getCargoName()),
    CARGO_CODE("код груза",(doc,index) -> doc.getCargoCode()),
//
    TARIF_PAYER_NAME("Плательщик",(doc,index) -> doc.getTarifPayer().getName()),
    PAYER_CODE("Код плательщика",(doc,index) -> doc.getTarifPayer().getRailroadCode()),
    PAYMENT_SUMM("Сумма платежа",(doc,index) -> doc.getPayment()),
    TARIF_DISTANCE("Тарифное расстояние",(doc,index) -> doc.getTarifDistance()),
//
    VAGON_NUMBER("№ вагона",(doc,index) -> doc.getVagonList().get(index).getNumber()),
    VAGON_GROSS_WEIGHT("Вес брутто",(doc,index) -> doc.getVagonList().get(index).getGrossWeight()),
    VAGON_NET_WEIGHT("Вес нетто",(doc,index) -> doc.getVagonList().get(index).getNetWeight()),
    VAGON_TARE_WEIGTH("Вес тары",(doc,index) -> doc.getVagonList().get(index).getTareWeight()),
    VAGON_CARRYING_CAPASITY("Г/п вагона",(doc,index) -> doc.getVagonList().get(index).getCarryingCapacity()),
    VAGON_ZPU("ЗПУ вагона",(doc,index) -> doc.getVagonList().get(index).getZpuList()),
    VAGON_ZPU_COUNT("Количество ЗПУ",(doc,index) -> doc.getVagonList().get(index).getZpuCount()),
    VAGON_PAYMENT("Тариф на вагон",(doc,index) -> doc.getVagonList().get(index).getPayment()),
//
    COLUMN_7_INFO("графа7",(doc,index) -> doc.getColumn7info()),
    COLUMN_15_INFO("графа15",(doc,index) -> doc.getColumn15info()),
//
//    SPECIAL_CONDITIONS_STAMP("Особые условия, штамп 717"),
//
    VOID_SPACE(" ",(doc,index) -> " ")
;


    private String title;

    private BiFunction<RailroadDocument,Integer,Object> fieldExtractor;

    RailDocFields(String title, BiFunction<RailroadDocument,Integer,Object> fieldExtractor) {
        this.title = title;
        this.fieldExtractor = fieldExtractor;
    }

    public Object getField(RailroadDocument document, int vagIndex){
        return fieldExtractor.apply(document,vagIndex);
    };

    public String getTitle() {
        return title;
    }
}

