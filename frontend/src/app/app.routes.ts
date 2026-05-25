import { Routes } from '@angular/router';
import { UserRegister } from './components/user-register/user-register';
import { UserList } from './components/user-list/user-list';
import { adminGuard } from './guard/admin.guard';
import { Login } from './components/login/login';
import { Homepage } from './components/homepage/homepage';
import { basicGuard } from './guard/basic.guard';
import { Planning } from './components/planning/planning';
import { Activitydetails } from './components/activitydetails/activitydetails';
import { Drills } from './components/drills/drills';
import { ReflectionContainer } from './components/reflectioncontainer';

export const routes: Routes = [
    // no login
    { path: 'register', component: UserRegister },
    { path: 'login', component: Login },
    { path: '', redirectTo: '/login', pathMatch: 'full' },


    // canActivate: [basicGuard] (editing purposes)
    // basic login
    { path: 'home', component: Homepage, canActivate: [basicGuard] },
    { path: 'planning', component: Planning, canActivate: [basicGuard] },
    { path: 'drills', component: Drills, canActivate: [basicGuard] },
    { path: 'reflections', component: ReflectionContainer, canActivate: [basicGuard] },

    // ROLE COACH
    { path: 'admin/users', component: UserList, canActivate: [adminGuard] },
    { path: 'planning/detail/:id', component: Activitydetails, canActivate: [adminGuard] },
];