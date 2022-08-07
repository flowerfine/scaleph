import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FlinkComponent} from './flink.component';
import {ClusterConfigComponent} from "./cluster-config/cluster-config.component";
import {ClusterConfigOptionsComponent} from "./cluster-config-options/cluster-config-options.component";
import {ClusterInstanceComponent} from "./cluster-instance/cluster-instance.component";

const routes: Routes = [
  {
    path: '',
    component: FlinkComponent,
    children: [
      {path: 'cluster-config', component: ClusterConfigComponent},
      {path: 'cluster-config-options', component: ClusterConfigOptionsComponent},
      {path: 'cluster-instance', component: ClusterInstanceComponent},
      {path: '', redirectTo: 'cluster-config-options', pathMatch: 'full'},
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FlinkRoutingModule {
}
