import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DataDevComponent } from './datadev.component';
import { DatasourceComponent } from './datasource/datasource.component';
import { ClusterComponent } from './cluster/cluster.component';
import { JobComponent } from './job/job.component';
import { ProjectComponent } from './project/project.component';
import { ResourceComponent } from './resource/resource.component';

const routes: Routes = [
  {
    path: '',
    component: DataDevComponent,
    children: [
      { path: 'datasource', component: DatasourceComponent },
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
export class DataDevRoutingModule {}
