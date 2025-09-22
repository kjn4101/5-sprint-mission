package com.codeit.transaction.repository;

import com.codeit.transaction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
