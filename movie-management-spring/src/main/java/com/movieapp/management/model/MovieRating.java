package com.movieapp.management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MovieRatings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int rating;
}
