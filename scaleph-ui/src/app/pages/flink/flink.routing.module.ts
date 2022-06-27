import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FlinkComponent } from './flink.component';
import { ReleaseComponent } from './release/release.component';
import {DeployConfigComponent} from "./deploy-config/deploy-config.component";
import {DeployConfigFileComponent} from "./deploy-config-file/deploy-config-file.component";
import {ClusterConfigComponent} from "./cluster-config/cluster-config.component";

const routes: Routes = [
  {
    path: '',
    component: FlinkComponent,
    children: [
      { path: 'release', component: ReleaseComponent },
      { path: 'deploy-config', component: DeployConfigComponent },
      { path: 'deploy-config-file', component: DeployConfigFileComponent },
      { path: 'cluster-config', component: ClusterConfigComponent },
      { path: '', redirectTo: 'release', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FlinkRoutingModule {}
