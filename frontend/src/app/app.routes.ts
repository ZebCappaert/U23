import { Routes } from '@angular/router';
import { UserRegister } from './components/user-register/user-register';
import { UserList } from './components/user-list/user-list';
import { adminGuard } from './guard/guard';

export const routes: Routes = [
    { path: 'register', component: UserRegister },
    { path: 'admin/users', component: UserList, canActivate: [adminGuard] },
    { path: '', redirectTo: '/register', pathMatch: 'full' }
];