package com.cmsapp.backend.repository;

import com.cmsapp.backend.model.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, Long> {

    List<Metadata> findByTitleContainingIgnoreCase(String title);
    List<Metadata> findByYear(Integer year);


    List<Metadata> findByTitleAndYearOrderByIdDesc(String title, Integer year);

    // Belirli yıldan sonra çıkan filmler
    List<Metadata> findByYearGreaterThan(Integer year);

    // Native SQL - PostgreSQL'in ILIKE özelliği (büyük/küçük harf duyarsız)
    @Query(value = "SELECT * FROM metadata WHERE " +
            "title ILIKE '%' || :keyword || '%' OR " +
            "plot ILIKE '%' || :keyword || '%'",
            nativeQuery = true)
    List<Metadata> searchInTitleOrPlot(@Param("keyword") String keyword);


}