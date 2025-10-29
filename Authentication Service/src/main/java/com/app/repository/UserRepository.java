package com.app.repository;


import com.app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * SQL query: SELECT * FROM users WHERE userId =
     *
     * @param id as String
     * @return User
     */
    User getUserById(Long id);

    User getUserByEmailId(String emailId);

    User getUserByMobileNo(String mobileNo);

    boolean existsByEmailId(String emailId);

    boolean existsByMobileNo(String mobileNo);

    Optional<User> findByEmailId(String emailId);

    Optional<User> findByUserName(String username);

    Optional<User> findByMobileNo(String mobileNo);

    Optional<User> findByEmailIdOrMobileNoOrUserName(String emailId, String mobileNo, String username);


}
