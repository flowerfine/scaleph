import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BatchJobComponent } from './batch-job/batch-job.component';
import { OpsCenterComponent } from './opscenter.component';
import { RealtimeJobComponent } from './realtime-job/realtime-job.component';

const routes: Routes = [
  {
    path: '',
    component: OpsCenterComponent,
    children: [
      { path: 'batch', component: BatchJobComponent },
      { path: 'realtime', component: RealtimeJobComponent },
      { path: '', redirectTo: 'realtime', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OpsCenterRoutingModule {}
