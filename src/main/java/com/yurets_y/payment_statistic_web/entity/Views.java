package com.yurets_y.payment_statistic_web.entity;

public class Views {
    /*
    * Только базовые поля для загрузки информации в сводках, и изначально загруженных данных
    * */
    public interface ShortView{

    }

    /*
    * Расширенное отображение сущности, включая сущности один к одному, и не включая сущности один к многим
    * */
    public interface NormalView extends  ShortView{

    }
    /* Расширенное отображение, включает отображение сущностей один к многим
    * */
    public interface ExtendedView extends NormalView{

    }
    /*
    * Отображение всех полей, кроме полей аудитинга.
    * */
    public interface FullView extends ExtendedView {

    }
//    Предназначен для дебага приложения админом, только сервисные поля
    public interface ExtraView extends FullView {

    }

}
