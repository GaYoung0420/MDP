package com.example.demo.MDP;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface mdpRepository extends JpaRepository<mdpPurchaseCode,Long> {
    
    
    long countByCode(String code);
    
    Optional<mdpPurchaseCode> findByCode(String code);

    Page<mdpPurchaseCode> findByUser(String user, Pageable pageable);


    Page<mdpPurchaseCode> findAll(Pageable pageable);
    Page<mdpPurchaseCode> findByOrderByIdDesc(Pageable pageable);

    @Query(value="SELECT id FROM mdpPurchaseCode order by id desc limit 1", nativeQuery = true )
    long lastColumn();

    Long countByUser(String user);

    @Transactional
    @Modifying
    @Query(value="UPDATE mdpPurchaseCode SET USER=:user WHERE CODE=:code", nativeQuery = true )
    void updateUser(String user, String code);

    Long countByCodeAndUserIsNull(String code);
    
}    
