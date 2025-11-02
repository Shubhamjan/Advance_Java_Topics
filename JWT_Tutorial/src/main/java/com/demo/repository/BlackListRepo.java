package com.demo.repository;

import com.demo.entity.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BlackListRepo extends JpaRepository<BlackListToken,Long> {

    @Modifying
    @Query(value = "Delete from BlackList t where t.expiry_date<:currentTime",nativeQuery = true)
    boolean isTokenBlackListed(String token);

    boolean existsByToken(String token);

    @Modifying
    @Query("Delete from black_list b where b.expiry_date< :currentTime")
    void deleteExpiredToken(LocalDateTime now);
}
