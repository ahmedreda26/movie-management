package com.movieapp.management.service.impl;

import com.movieapp.management.model.Movie;
import com.movieapp.management.model.MovieRating;
import com.movieapp.management.model.User;
import com.movieapp.management.repo.MovieRatingRepository;
import com.movieapp.management.repo.MovieRepository;
import com.movieapp.management.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MovieRatingService {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final MovieRatingRepository movieRatingRepository;

    public MovieRatingService(MovieRepository movieRepository, UserRepository userRepository, MovieRatingRepository movieRatingRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.movieRatingRepository = movieRatingRepository;
    }


    @Transactional
    public String rateMovie(Long movieId, String userName, int rating) {

        User user = userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("User not found"));
        // Check if the movie and user exist
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        //User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user already rated the movie
        Optional<MovieRating> existingRating = movieRatingRepository.findByUserIdAndMovieId(user.getId(), movieId);

        if (existingRating.isPresent()) {
            // Update existing rating
            MovieRating movieRating = existingRating.get();
            movieRating.setRating(rating);
            movieRatingRepository.save(movieRating);
            return "Movie rating updated successfully.";
        } else {
            // Create a new rating
            MovieRating newRating = new MovieRating();
            newRating.setMovie(movie);
            newRating.setUser(user);
            newRating.setRating(rating);
            movieRatingRepository.save(newRating);
            return "Movie rated successfully.";
        }
    }

}
