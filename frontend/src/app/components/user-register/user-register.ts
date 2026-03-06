import { Component } from '@angular/core';
import { User } from '../../models/user.model';
import { UserService } from '../../service/user';
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/authService';

@Component({
  selector: 'app-user-register',
  imports: [FormsModule],
  templateUrl: './user-register.html',
  styleUrl: './user-register.css',
})
export class UserRegister {

  users$: Observable<User[]> | undefined;

  public confirmPassword: string = '';

  constructor(private userService: UserService, private router: Router, private authService: AuthService) { }

  public newUser: User = {
    username: '',
    firstName: '',
    lastName: '',
    password: '',
    role: 'PLAYER'
  }

  saveUser() {
    this.userService.addUser(this.newUser).subscribe({
      next: (savedUser) => {
        console.log("User saved: ", savedUser);
        this.users$ = this.userService.getUsers();
        this.newUser = {
          username: '',
          firstName: '',
          lastName: '',
          password: '',
          role: 'PLAYER'
        }
        this.authService.setUser(savedUser);
        this.router.navigate(['/home']);
      }
    });
  }
}
