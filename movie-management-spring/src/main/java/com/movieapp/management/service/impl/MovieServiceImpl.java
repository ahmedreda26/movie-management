package com.movieapp.management.service.impl;

import com.movieapp.management.model.Movie;
import com.movieapp.management.repo.MovieRatingRepository;
import com.movieapp.management.repo.MovieRepository;
import com.movieapp.management.service.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieRatingRepository movieRatingRepository;

    public MovieServiceImpl(MovieRepository movieRepository,MovieRatingRepository movieRatingRepository) {
        this.movieRepository = movieRepository;
        this.movieRatingRepository = movieRatingRepository;

    }

    @Transactional
    @Override
    public Movie saveMovie(Movie movie) {
        Movie savedMovie = movieRepository.save(movie);

        // Set the average rating
        savedMovie.setAverageRating(0.0);

        // Return the movie with the average rating
        return savedMovie;
    }

    @Override
    public Boolean isMovieTitleExists(String title) {
        return movieRepository.existsByTitleContainingIgnoreCase(title);
    }


    @Transactional
    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Map<String, Object> addBatchMovies(List<Movie> movies) {
        List<String> existingTitles = new ArrayList<>();
        List<Movie> savedMovies = new ArrayList<>();

        for (Movie movie : movies) {
            if (isMovieTitleExists(movie.getTitle())) {
                existingTitles.add(movie.getTitle());
            } else {
                savedMovies.add(saveMovie(movie));
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("savedMovies", savedMovies);
        if (!existingTitles.isEmpty()) {
            response.put("conflictMovies", existingTitles);
        }

        return response;
    }

    @Transactional
    @Override
    public Map<String, Object> deleteBatchMovies(List<Long> movieIds) {
        Map<String, Object> response = new HashMap<>();
        try {
            movieRepository.deleteAllById(movieIds);
            response.put("message", "Movies deleted successfully.");
        } catch (Exception e) {
            response.put("error", "An error occurred while deleting movies.");
            response.put("details", e.getMessage());
        }
        return response;
    }


    @Override
    public Page<Movie> getAllMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> movies = movieRepository.findAll(pageable);

        // Calculate and set the average rating for each movie
        movies.forEach(movie -> {
            Double averageRating = movieRatingRepository.findAverageRatingByMovieId(movie.getId());
            movie.setAverageRating(averageRating);
        });

        return movies;
    }


    @Override
    public Movie getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        // Calculate the average rating
        Double averageRating = movieRatingRepository.findAverageRatingByMovieId(id);
        // Set the average rating
        movie.setAverageRating(averageRating);
        return movie;
    }

    @Override
    public List<Movie> searchMovies(String query) {
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(query);
        movies.forEach(movie -> {
            Double averageRating = movieRatingRepository.findAverageRatingByMovieId(movie.getId());
            movie.setAverageRating(averageRating);
        });
        return movies;
    }




}
