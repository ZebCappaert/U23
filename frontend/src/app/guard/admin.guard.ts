import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/authService';

export const adminGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.getRole() === 'COACH') {
        return true;
    } else {
        console.warn('Toegang geweigerd: Je bent geen COACH');
        router.navigate(['/register']);
        return false;
    }
};