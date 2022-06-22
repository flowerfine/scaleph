import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FlinkComponent } from './flink.component';
import { ReleaseComponent } from './release/release.component';
import {DeployConfigComponent} from "./deploy-config/deploy-config.component";

const routes: Routes = [
  {
    path: '',
    component: FlinkComponent,
    children: [
      { path: 'release', component: ReleaseComponent },
      { path: 'deploy-config', component: DeployConfigComponent },
      { path: '', redirectTo: 'release', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FlinkRoutingModule {}
