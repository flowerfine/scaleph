import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudioComponent } from './studio.component';

const routes: Routes = [
  {
    path: '',
    component: StudioComponent,
    children: [],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class StudioRoutingModule {}
