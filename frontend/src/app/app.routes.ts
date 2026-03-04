import { Routes } from '@angular/router';
import { UserRegister } from './components/user-register/user-register';
import { UserList } from './components/user-list/user-list';

export const routes: Routes = [
    { path: 'register', component: UserRegister },
    { path: 'admin/users', component: UserList },
    { path: '', redirectTo: '/register', pathMatch: 'full' }
];