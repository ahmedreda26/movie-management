import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { UserDashboardComponent } from './user-dashboard/user-dashboard.component';
import {FormsModule} from "@angular/forms";
import {LoginComponent} from "./login/login.component";
import {HttpClientModule} from "@angular/common/http";
import {InputTextModule} from "primeng/inputtext";
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {MessageService} from "primeng/api";
import { MovieListComponent } from './shared/movie-list/movie-list.component';
import { NoMoviesMessageComponent } from './shared/no-movies-message/no-movies-message.component';
import { SectionHeaderComponent } from './shared/section-header/section-header.component';
import {ToastrModule} from "ngx-toastr";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { LogoutButtonComponent } from './shared/logout-button/logout-button.component';
import {RatingModule} from "primeng/rating";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminDashboardComponent,
    UserDashboardComponent,
    MovieListComponent,
    NoMoviesMessageComponent,
    SectionHeaderComponent,
    LogoutButtonComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ButtonModule,
    CardModule,
    InputTextModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right', // Position of the toast
      timeOut: 3000, // Toast duration
      closeButton: true, // Show close button
    }),
    RatingModule,
  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
