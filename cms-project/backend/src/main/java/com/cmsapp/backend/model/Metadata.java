package com.cmsapp.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "plot", columnDefinition = "TEXT")
    private String plot;

    @Column(name = "poster", length = 1000)
    private String poster;

    @Column(name = "year")
    private Integer year;

    @Column(name = "language", length = 500)
    private String language;

    @Column(name = "country", length = 500)
    private String country;
}
