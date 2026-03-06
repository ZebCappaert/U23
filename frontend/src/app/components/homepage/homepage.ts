import { Component } from '@angular/core';
import { AuthService } from '../../service/authService';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-homepage',
  imports: [],
  templateUrl: './homepage.html',
  styleUrl: './homepage.css',
})
export class Homepage {
  constructor(public authService: AuthService) { }
}
