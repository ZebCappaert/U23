import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../models/user.model';
import { UserService } from '../../service/user';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-user-list',
  imports: [AsyncPipe],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList {

  users$: Observable<User[]> | undefined;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
  }

  deleteUser(id: number | undefined): void {
    if (!id) return;
    else {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          console.log('User deleted');
          this.users$ = this.userService.getUsers();
        },
        error: (err) => {
          console.error('Fout bij het verwijderen:', err);
          alert('Verwijderen mislukt. Is de backend bereikbaar?');
        }
      });
    }
  }
}
