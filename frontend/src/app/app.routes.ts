import { Routes } from '@angular/router';
import { UserRegister } from './components/user-register/user-register';
import { UserList } from './components/user-list/user-list';
import { adminGuard } from './guard/admin.guard';
import { Login } from './components/login/login';
import { Homepage } from './components/homepage/homepage';
import { basicGuard } from './guard/basic.guard';
import { Planning } from './components/planning/planning';

export const routes: Routes = [
    // no login
    { path: 'register', component: UserRegister },
    { path: 'login', component: Login },
    { path: '', redirectTo: '/login', pathMatch: 'full' },

    // basic login
    { path: 'home', component: Homepage, canActivate: [basicGuard] },
    { path: 'planning', component: Planning, canActivate: [basicGuard] },

    // ROLE COACH
    { path: 'admin/users', component: UserList, canActivate: [adminGuard] },
];