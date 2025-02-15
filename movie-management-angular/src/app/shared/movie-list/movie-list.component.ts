import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Movie} from "../../models/movie";

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent {
  @Input() movie: Movie ={averageRating: 0, imdbId: "", poster: "", title: "", type: "", year: ""};
  @Input() isAdmin: boolean = false;
  @Input() movieRating: number = 0;

  @Output() add = new EventEmitter<Movie>();
  @Output() delete = new EventEmitter<number>();
  @Output() rate = new EventEmitter<{ movieId: number; rating: number }>();


  addMovie() {
    this.add.emit(this.movie);
  }

  deleteMovie() {
    this.delete.emit(this.movie.id);
  }

  rateMovie() {
    if (this.movie.id) {
      this.rate.emit({ movieId: this.movie.id, rating: this.movieRating });
    }
  }

}
