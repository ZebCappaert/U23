import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {


  private apiUrl = "http://localhost:8080/api/users/";

  constructor(private http: HttpClient) { }

  //GET
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl)
  }

  //POST
  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user)
  }

  login(credentials: any): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}login`, credentials);
  }

  //DELETE
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}${id}`);
  }
}
