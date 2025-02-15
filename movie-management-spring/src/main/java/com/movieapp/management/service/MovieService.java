package com.movieapp.management.service;

import com.movieapp.management.model.Movie;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface MovieService {
     Movie saveMovie(Movie movie);

     void deleteMovie(Long id);

     Map<String, Object> addBatchMovies(List<Movie> movies);

     Map<String, Object> deleteBatchMovies(List<Long> movieIds);

     Page<Movie> getAllMovies(int page, int size);

     Movie getMovieById(Long id);

     List<Movie> searchMovies(String query);

     Boolean isMovieTitleExists(String title);
}
