package com.movieapp.management.service;

import com.movieapp.management.model.Movie;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {
     Movie saveMovie(Movie movie);

     void deleteMovie(Long id);

    Page<Movie> getAllMovies(int page, int size);

      List<Movie> searchMovies(String query);

    Boolean isMovieTitleExists(String title);
}
