import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {jwtDecode} from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<string> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, credentials)
      .pipe(map(response => response.token));
  }
  saveToken(token: string) {
    localStorage.setItem('jwt_token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  decodeToken(): any {
    const token = this.getToken();
    if (token) {
      return jwtDecode(token);
    }
    return null;
  }

  getUserRole(): string {
    const decodedToken = this.decodeToken();
    return decodedToken ? decodedToken.role : '';
  }

  getUsername(): string {
    const decodedToken = this.decodeToken();
    return decodedToken ? decodedToken.sub : '';
  }


  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
