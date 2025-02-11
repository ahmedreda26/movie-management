import { Component } from '@angular/core';
import {MovieService} from "../services/movie/movie.service";
import {Movie} from "../models/movie";
import {MessageService} from "primeng/api";
import {AuthService} from "../services/auth/auth.service";
import {ROLE} from "../enum/ROLE";
import {ToastrService} from "ngx-toastr";
import {NotificationService} from "../services/notification/Notification.Service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  movies : Movie[] = [];
  searchQuery = '';
  isAdmin = false;
  username: string = '';
  currentPage = 0;
  pageSize = 4;
  totalPages = 0;

  constructor(
    private movieService: MovieService,
    private authService: AuthService,
    private notificationService: NotificationService,
  ) {}

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.loadMovies();
    this.checkUserRole()
  }

  loadMovies() {
    this.movieService.getAllMovies(this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        this.movies = data.content;
        this.totalPages = data.totalPages;
      },
      error: (error) => {
        this.notificationService.error('Failed to load movies.');
      },
      complete: () => {
        console.log('Movies fetching completed.');
      },
    });
  }

  searchMovies(searchQuery: string) {
    this.searchQuery = searchQuery;
    if (!searchQuery.trim()) return;
    this.movieService.searchMoviesFromOmdb(searchQuery).subscribe({
      next: (response) => {
        this.movies = response;
      },
      error: (error) => {
        this.notificationService.error('Failed to perform search.');
      },
    });
  }

  addMovie(movie: Movie) {
    this.movieService.addMovie(movie).subscribe({
      next: (response) => {
          this.notificationService.success(`${response.title} was added successfully!`, 'Movie Added');
      },
      error: (error: HttpErrorResponse) => {
        if (error.status == 409) {
          this.notificationService.warning(`"${movie.title}" already exists.`, 'Duplicate Movie');
        } else {
          this.notificationService.error('Failed to add the movie.', 'Error');
        }
      },
    });
  }

  deleteMovie(id: number) {
    this.movieService.deleteMovie(id).subscribe({
      next: (response) => {
        const message = response.message || 'The movie was removed successfully.';
        this.notificationService.warning(message, 'Movie Removed');
        this.loadMovies(); // Refresh the movie list
      },
      error: (error) => {
        console.error('Delete movie failed:', error);
        this.notificationService.error('Failed to remove the movie.', 'Error');
      },
    });
  }

  checkUserRole() {
    this.isAdmin = this.authService.getUserRole() === ROLE.ADMIN;
  }
  goBackToMovies()
  {
    this.searchQuery='';
    this.loadMovies();
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
