package com.movieapp.management.controller;

import com.movieapp.management.DTO.RateMovieDTO;
import com.movieapp.management.model.Movie;
import com.movieapp.management.service.MovieService;
import com.movieapp.management.service.impl.MovieRatingService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieRatingService movieRatingService;

    public MovieController(MovieService movieService,MovieRatingService movieRatingService) {
        this.movieService = movieService;
        this.movieRatingService = movieRatingService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        boolean isMovie = movieService.isMovieTitleExists(movie.getTitle());
        if (isMovie) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Movie with the title '" + movie.getTitle() + "' already exists.");
        }
        Movie savedMovie = movieService.saveMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @PostMapping("/addBatch")
    public ResponseEntity<?> addBatchMovies(@RequestBody List<Movie> movies) {
        Map<String, Object> response = movieService.addBatchMovies(movies);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "The movie was removed successfully.");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteBatch")
    public ResponseEntity<?> deleteBatchMovies(@RequestBody List<Long> movieIds) {
        Map<String, Object> response = movieService.deleteBatchMovies(movieIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }


    @GetMapping
    public Page<Movie> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "4") int size) {
        return movieService.getAllMovies(page, size);
    }


    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String query) {
        List<Movie> movies = movieService.searchMovies(query);
        return ResponseEntity.ok(movies);
    }

    @PostMapping("/rate")
    public ResponseEntity<Map<String, String>> rateMovie(@RequestBody RateMovieDTO rateMovieDTO) {
        try {
            String message =movieRatingService.rateMovie( rateMovieDTO.getMovieId(), rateMovieDTO.getUserName(), rateMovieDTO.getRating());

            return ResponseEntity.ok(Collections.singletonMap("message", message));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error processing rating: " + e.getMessage()));
        }
    }

}
