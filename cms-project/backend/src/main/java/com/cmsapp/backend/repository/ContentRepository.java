package com.cmsapp.backend.repository;

import com.cmsapp.backend.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    // jpa'nın hazır veritabanı işlemlerini kullanmak için
    //  BASİT ARAMA METHOD'LARI

    List<Content> findByRating(String rating);
    List<Content> findByRatingGreaterThan(String rating);
    List<Content> findByDirectorContainingIgnoreCase(String director);
    // sıralı listeleme (büyükten küçüğe)
    List<Content> findByOrderByRatingDesc();

    // JOIN ile metadata'ya erişim - Method name ile yapılamaz!
    @Query("SELECT c FROM Content c JOIN c.metadata m WHERE m.title LIKE %:title%")
    List<Content> findContentsByMetadataTitle(@Param("title") String title);

   }
