import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {environment} from "../../../../environments/environment";
import {Movie} from "../../models/movie";

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private apiUrl = `${environment.apiUrl}/movies`;

  constructor(private http: HttpClient) {}

  getAllMovies(page: number, size: number): Observable<any> {
    return this.http.get(`${this.apiUrl}?page=${page}&size=${size}`, this.getHeaders());
  }


  addMovie(movie: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, movie, this.getHeaders());
  }

  deleteMovie(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, this.getHeaders());
  }

  searchMoviesFromOmdb(query: string): Observable<Movie[]> {
    return this.http.get<any>(`https://www.omdbapi.com/?apikey=9d0ce253&s=${query}`).pipe(
      map((response) => {
        return response.Search.map((item: any) => ({
          imdbId: item.imdbID,
          title: item.Title,
          year: item.Year,
          poster: item.Poster,
          type: item.Type,
          rating: 0,
        }));
      })
    );
  }

  searchMoviesInDatabase(query: string): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/search`, {
      params: { query },
      headers: this.getHeaders().headers,
    });
  }

  rateMovie( movieId: number, rating: number): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/rate`,
      { movieId, rating },
      {
        ...this.getHeaders(),
        responseType: 'text' as 'json'
      }
    );
  }

  private getHeaders() {
    return { headers: new HttpHeaders({ Authorization: `Bearer ${localStorage.getItem('jwt_token')}` }) };
  }
}
