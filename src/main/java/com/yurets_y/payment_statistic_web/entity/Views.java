package com.yurets_y.payment_statistic_web.entity;

public class Views {
    public interface ShortView{

    }

    public interface NormalView extends  ShortView{

    }

    public interface FullView extends NormalView {

    }
//    Предназначен для дебага приложения админом, только сервисные поля
    public interface ExtraView extends FullView {

    }

}
