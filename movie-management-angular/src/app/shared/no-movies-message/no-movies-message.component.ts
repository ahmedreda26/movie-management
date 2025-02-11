import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-no-movies-message',
  templateUrl: './no-movies-message.component.html',
  styleUrls: ['./no-movies-message.component.css']
})
export class NoMoviesMessageComponent {
  @Input() message: string = 'No movies available to display.';

}
