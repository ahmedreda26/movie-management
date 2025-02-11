import { Component } from '@angular/core';
import {AuthService} from "../services/auth/auth.service";
import {Router} from "@angular/router";
import {ROLE} from "../enum/ROLE";
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = { username: '', password: '' };

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.credentials).subscribe(
      (token) => {
        console.log("Token received:", token);
        this.authService.saveToken(token);

        // Extract user info from token
        const username = this.authService.getUsername();
        const role = this.authService.getUserRole();

        console.log(`Logged in as: ${username}, Role: ${role}`);

        if (role === ROLE.ADMIN) {
          this.router.navigate(['/admin-dashboard']);
        } else if (role === ROLE.USER) {
          this.router.navigate(['/user-dashboard']);
        }
      },
      (error) => {
        console.error("Login failed:", error);
      }
    );
  }

}
