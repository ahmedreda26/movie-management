package com.movieapp.management.repo;

import com.movieapp.management.model.MovieRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {
    // Optional: Find a rating by user and movie to check if the user has already rated the movie
    Optional<MovieRating> findByUserIdAndMovieId(Long userId, Long movieId);

    // Add this method to fetch all ratings for a movie
    @Query("SELECT COALESCE(AVG(mr.rating), 0) FROM MovieRating mr WHERE mr.movie.id = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);

}
