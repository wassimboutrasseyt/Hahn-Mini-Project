import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { ProjectDetailComponent } from './components/project-detail/project-detail.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'projects', component: ProjectsComponent, canActivate: [authGuard] },
  { path: 'projects/:id', component: ProjectDetailComponent, canActivate: [authGuard] }
];
