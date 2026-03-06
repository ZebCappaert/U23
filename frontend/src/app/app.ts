import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { UserService } from './service/user';
import { User } from './models/user.model';
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterOutlet } from "@angular/router";
import { AuthService } from './service/authService';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule, RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  users$: Observable<User[]> | undefined;

  constructor(private userService: UserService, public authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
