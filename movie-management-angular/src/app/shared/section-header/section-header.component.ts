import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Movie} from "../../models/movie";
import {Router} from "@angular/router";

@Component({
  selector: 'app-section-header',
  templateUrl: './section-header.component.html',
  styleUrls: ['./section-header.component.css']
})
export class SectionHeaderComponent {
  @Input() title: string = 'Section Title';
  @Input() username: string = '';
  @Input() searchQuery: string = '';
  @Output() backToMovies = new EventEmitter<void>();
  @Output() search = new EventEmitter<string>();
  constructor(private router: Router) {}

  onSearch() {
    this.search.emit(this.searchQuery);
  }

  onBackToMovies() {
    this.backToMovies.emit();
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }

}
