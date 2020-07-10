package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
//    @Query("select u from User u where u.name like %?1")
    User findByUsername(String name);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
