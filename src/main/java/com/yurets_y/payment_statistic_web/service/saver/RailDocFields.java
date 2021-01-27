package com.yurets_y.payment_statistic_web.service.saver;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;

public enum RailDocFields {
    ORDER_NUMBER("№ п/п"){
        @Override
        public Object getField(RailroadDocument document, int vagIndex) {
            return vagIndex;
        }
    },

    DOC_NUMBER("№ накладной"){
        @Override
        public Object getField(RailroadDocument document, int vagIndex) {
            return document.getDocNumber();
        }
    },
    DOC_DATE("Дата док"){
        @Override
        public Object getField(RailroadDocument document, int vagIndex) {
            return document.getDocDate();
        }
    };


    private String title;

    RailDocFields(String title) {
        this.title = title;
    }

    public abstract Object getField(RailroadDocument document, int vagIndex);

    public String getTitle() {
        return title;
    }
}
