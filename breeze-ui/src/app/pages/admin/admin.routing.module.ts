import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminComponent } from './admin.component';
import { DictComponent } from './dict/dict.component';
import { PrivilegeComponent } from './privilege/privilege.component';
import { SettingComponent } from './setting/setting.component';
import { UserComponent } from './user/user.component';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      { path: 'dict', component: DictComponent },
      { path: 'user', component: UserComponent },
      { path: 'privilege', component: PrivilegeComponent },
      { path: 'setting', component: SettingComponent },
      { path: '', redirectTo: 'user', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
