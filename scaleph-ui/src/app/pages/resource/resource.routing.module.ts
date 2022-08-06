import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ResourceComponent} from './resource.component';
import {ReleaseSeatunnelComponent} from "./seatunnel/release-seatunnel.component";
import {ReleaseFlinkComponent} from "./flink/release-flink.component";
import {ClusterConfigComponent} from "./cluster/cluster-config.component";

const routes: Routes = [
  {
    path: '',
    component: ResourceComponent,
    children: [
      {path: 'flink', component: ReleaseFlinkComponent},
      {path: 'seatunnel', component: ReleaseSeatunnelComponent},
      {path: 'cluster', component: ClusterConfigComponent},
      {path: '', redirectTo: 'flink', pathMatch: 'full'},
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ResourceRoutingModule {
}
