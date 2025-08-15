package com.cmsapp.backend.repository;

import com.cmsapp.backend.model.Cast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface CastRepository extends JpaRepository<Cast, Long>{

    // İsme göre arama (büyük/küçük harf duyarsız)
    List<Cast> findByNameContainingIgnoreCase(String name);

    // İsim sırasıyla listeleme
    List<Cast> findByOrderByNameAsc();


//    // Reverse JOIN - Cast'ten Content'e gidip rating'e bakma
//    @Query("SELECT DISTINCT c FROM Cast c JOIN c.contents content WHERE content.rating > :minRating")
//    List<Cast> findCastsByContentRating(@Param("minRating") String minRating);

    // DÜZELTME: Content'ten Cast'e doğru JOIN yapıyoruz
    @Query("SELECT DISTINCT c FROM Cast c " +
            "JOIN Content content ON c MEMBER OF content.casts " +
            "WHERE content.rating > :minRating")
    List<Cast> findCastsByContentRating(@Param("minRating") String minRating);
}
