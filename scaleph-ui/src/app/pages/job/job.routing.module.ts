import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {JobComponent} from './job.component';
import {ArtifactComponent} from "./artifact/artifact.component";
import {JobConfigComponent} from "./job-config/job-config.component";
import {JobInstanceComponent} from "./job-instance/job-instance.component";

const routes: Routes = [
  {
    path: '',
    component: JobComponent,
    children: [
      {path: 'artifact', component: ArtifactComponent},
      {path: 'job-config', component: JobConfigComponent},
      {path: 'job-instance', component: JobInstanceComponent},
      {path: '', redirectTo: 'artifact', pathMatch: 'full'},
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class JobRoutingModule {
}
