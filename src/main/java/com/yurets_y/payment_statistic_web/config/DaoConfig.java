package com.yurets_y.payment_statistic_web.config;

import com.yurets_y.payment_statistic_web.repo.PaymentDetailsRepo;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import org.springframework.context.annotation.Configuration;

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
