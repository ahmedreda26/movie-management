package com.movieapp.management.service.impl;

import com.movieapp.management.model.Movie;
import com.movieapp.management.repo.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MovieRatingService {
    private final MovieRepository movieRepository;

    public MovieRatingService( MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public String rateMovie( Long movieId, int rating) {
        // Check if the user has already rated the movie
        Optional<Movie> existingMovie = movieRepository.findById( movieId);
        // Update the existing Movie
            Movie movie = existingMovie.get();
            movie.setRating(rating);
            movieRepository.save(movie);
        return "Movie rated successfully.";
    }
}
