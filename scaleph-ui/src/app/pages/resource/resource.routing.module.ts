import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ResourceComponent} from './resource.component';
import {ArtifactComponent} from "../job/artifact/artifact.component";
import {JobInstanceComponent} from "../job/job-instance/job-instance.component";
import {JobConfigComponent} from "../job/job-config/job-config.component";
import {ReleaseSeatunnelComponent} from "./seatunnel/release-seatunnel.component";

const routes: Routes = [
  {
    path: '',
    component: ResourceComponent,
    children: [
      {path: 'seatunnel', component: ReleaseSeatunnelComponent},
      {path: 'job-config', component: JobConfigComponent},
      {path: 'job-instance', component: JobInstanceComponent},
      {path: '', redirectTo: 'seatunnel', pathMatch: 'full'},
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ResourceRoutingModule {
}
