import { Injectable, signal } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private userRole = signal<string | null>(null);

    setRole(role: string) {
        this.userRole.set(role);
    }

    getRole() {
        return this.userRole();
    }

    isCoach() {
        return this.userRole() === 'COACH';
    }
}