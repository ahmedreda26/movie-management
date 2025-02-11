package com.movieapp.management.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RateMovieDTO {
    private Long movieId;
    private int rating;
}