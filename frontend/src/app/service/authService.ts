// auth.service.ts
import { Injectable, signal } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentUser = signal<User | null>(null);

    constructor() {
        this.restoreSession();
    }

    private restoreSession() {
        const name = "currentUser=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');

        for (let i = 0; i < ca.length; i++) {
            let c = ca[i].trim();
            if (c.indexOf(name) === 0) {
                const userData = c.substring(name.length, c.length);
                this.currentUser.set(JSON.parse(userData));
            }
        }
    }

    setUser(user: User) {
        // Sla op als cookie (verloopt bijvoorbeeld na 1 dag)
        const date = new Date();
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
        const expires = "expires=" + date.toUTCString();

        document.cookie = `currentUser=${JSON.stringify(user)}; ${expires}; path=/; SameSite=Strict`;
        this.currentUser.set(user);
    }

    logout() {
        // Verwijder cookie door de verloopdatum in het verleden te zetten
        document.cookie = "currentUser=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        this.currentUser.set(null);
    }

    getUser() {
        return this.currentUser();
    }

    isCoach(): boolean {
        return this.currentUser()?.role === 'COACH';
    }
}