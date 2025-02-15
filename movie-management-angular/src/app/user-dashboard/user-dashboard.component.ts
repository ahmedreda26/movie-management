import { Component } from '@angular/core';
import {MovieService} from "../services/movie/movie.service";
import {AuthService} from "../services/auth/auth.service";
import {NotificationService} from "../services/notification/Notification.Service";
import {ROLE} from "../enum/ROLE";

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css']
})
export class UserDashboardComponent {
  movies: any[] = [];
  username: string = '';
  searchQuery = '';
  isAdmin = false;
  currentPage = 0;
  pageSize = 4;
  totalPages = 0;

  constructor(private movieService: MovieService,
              private authService: AuthService,
              private notificationService: NotificationService) {}

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.loadMovies();
    this.checkUserRole();
  }

  loadMovies() {
    this.movieService.getAllMovies(this.currentPage, this.pageSize).subscribe((data) => {
      this.movies = data.content; // `content` holds the paginated list of movies
      this.totalPages = data.totalPages;
    });
  }

  searchMovies(searchQuery: string) {
    this.searchQuery = searchQuery;
    if (!searchQuery.trim()) return;
    this.movieService.searchMoviesInDatabase(searchQuery).subscribe({
      next: (response) => {
        this.movies = response;
        this.notificationService.success(`Found ${response.length} movie(s).`, 'Search Successful');
      },
      error: (error) => {
        console.error('Search failed:', error);
        this.notificationService.error('Failed to search movies in the database.', 'Error');
      },
    });
  }

  rateMovie(movieId: number, rating: number) {
    const userName = this.authService.getUsername();
    this.movieService.rateMovie(userName, movieId, rating).subscribe({
      next: (response) => {
        console.log('✅ Response received:', response);
        this.notificationService.success('Movie rated successfully!', 'Rating');
        this.updateMovieRating(movieId, rating);
      },
      error: (error) => {
        console.error('Rating failed:', error);
        this.notificationService.error('Failed to rate the movie.', 'Error');
      },
    });
  }

  goBackToMovies()
  {
    this.searchQuery='';
    this.loadMovies();
  }

  updateMovieRating(movieId: number, rating: number) {
    const movie = this.movies.find(m => m.id === movieId);
    if (movie) {
      movie.rating = rating;
    }
  }

  checkUserRole() {
    this.isAdmin = this.authService.getUserRole() === ROLE.ADMIN;
  }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadMovies();
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadMovies();
    }
  }
}
