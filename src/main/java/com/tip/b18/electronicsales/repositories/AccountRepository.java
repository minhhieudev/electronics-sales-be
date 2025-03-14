package com.tip.b18.electronicsales.repositories;

import com.tip.b18.electronicsales.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>{
    Optional<Account> findByUserName(String userName);

    @Query("SELECT a FROM Account a WHERE a.userName LIKE CONCAT('%', :search, '%') OR a.numberPhone LIKE CONCAT('%', :search, '%')")
    Page<Account> findByUserNameOrNumberPhone(@Param("search") String search, Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByNumberPhone(String numberPhone);
}
