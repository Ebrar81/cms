package com.cmsapp.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    // Metadata ile Many-to-One ilişkisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metadata_id")
    private Metadata metadata;

    // Cast ile Many-to-Many ilişkisi
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "content_movie_cast",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "cast_id")
    )
    private List<Cast> casts;

    @Column(name = "rating", length = 50)
    private String rating;

    @Column(name = "director", length = 500)
    private String director;

    @Column(name = "actors", columnDefinition = "TEXT")
    private String actors;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
