import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../service/user';
import { AuthService } from '../../service/authService';
import { Router } from '@angular/router';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  public loginData = {
    username: '',
    password: ''
  };

  public errorMessage = '';

  constructor(private userService: UserService, private authService: AuthService, private router: Router) { }

  onLogin() {
    this.userService.login(this.loginData).subscribe({
      next: (user: User) => {
        console.log("logging successful: ", user)
        this.authService.setUser(user)
        this.router.navigate(['/home'])
      },
      error: (err: any) => {
        console.error("Login fout: ", err);
        this.errorMessage = "Gebruikersnaam of wachtwoord onjuist";
      }
    })
  }
}
