import { Injectable, signal } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private currentUser = signal<User | null>(null);

    setUser(user: User) {
        this.currentUser.set(user);
    }

    getUser() {
        return this.currentUser();
    }

    getRole() {
        return this.currentUser()?.role || null;
    }

    logout() {
        this.currentUser.set(null);
    }

    isCoach() {
        return this.currentUser()?.role === 'COACH';
    }
}