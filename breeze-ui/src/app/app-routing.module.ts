import { NgModule } from '@angular/core';
import { Routes, RouterModule, ExtraOptions } from '@angular/router';
import { AuthGuardService } from './@core/services/auth-guard-service.guard';
import { LoginComponent } from './@shared/components/login/login.component';
import { RegisterComponent } from './@shared/components/register/register.component';
import { ForbiddenComponent } from './pages/abnormal/forbidden/forbidden.component';
import { NotFoundComponent } from './pages/abnormal/not-found/not-found.component';
import { ServerErrorComponent } from './pages/abnormal/server-error/server-error.component';
import { WorkbenchComponent } from './pages/studio/workbench/workbench.component';

const routes: Routes = [
  {
    path: 'breeze',
    loadChildren: () => import('./pages/pages.module').then((m) => m.PagesModule),
    canActivate: [AuthGuardService],
  },
  {
    path: 'workbench',
    component: WorkbenchComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: '403',
    component: ForbiddenComponent,
  },
  {
    path: '404',
    component: NotFoundComponent,
  },
  {
    path: '500',
    component: ServerErrorComponent,
  },
  {
    path: '',
    redirectTo: 'breeze',
    pathMatch: 'full',
  },
  {
    path: '**',
    component: NotFoundComponent,
  },
];

const config: ExtraOptions = {
  useHash: false,
};

@NgModule({
  imports: [RouterModule.forRoot(routes, config)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
