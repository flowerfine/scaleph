import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {JobComponent} from './job.component';
import {ArtifactComponent} from "./artifact/artifact.component";

const routes: Routes = [
  {
    path: '',
    component: JobComponent,
    children: [
      {path: 'artifact', component: ArtifactComponent},
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
