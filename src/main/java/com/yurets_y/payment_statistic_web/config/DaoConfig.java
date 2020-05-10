package com.yurets_y.payment_statistic_web.config;

import com.yurets_y.payment_statistic_web.repo.PaymentDetailsRepo;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import com.yurets_y.payment_statistic_web.service.PaymentListServiceRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AliasFor;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class DaoConfig {

    private PaymentListRepo paymentListRepo;

    private PaymentDetailsRepo paymentDetailsRepo;

//    @Autowired
//    public void setPaymentListRepo(PaymentListRepo paymentListRepo) {
//        this.paymentListRepo = paymentListRepo;
//    }
//
//    @Bean
//    @Primary
//    public PaymentListService paymentListService(){
//        System.out.println("Creating paymentListService in config class");
//        return new PaymentListServiceRepoImpl(this.paymentListRepo);
//    }

    //    @Bean(name={"defaultEMF", "entityManagerFactory"})
//    public EntityManagerFactory defaultEMF(){
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("basic-persistence");
//        return emf;
//    }
//    TODO Попробовать переопределить EMF и подменить Entity manager fartory
//    Похоже он раздается основному контексту через автосвязывание бинов.

}
