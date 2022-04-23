import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ClusterComponent } from './cluster/cluster.component';
import { JobComponent } from './job/job.component';
import { ProjectComponent } from './project/project.component';
import { ResourceComponent } from './resource/resource.component';
import { StudioComponent } from './studio.component';

const routes: Routes = [
  {
    path: '',
    component: StudioComponent,
    children: [
      { path: 'project', component: ProjectComponent },
      { path: 'job', component: JobComponent },
      { path: 'cluster', component: ClusterComponent },
      { path: 'resource', component: ResourceComponent },
      { path: '', redirectTo: 'project', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class StudioRoutingModule {}
