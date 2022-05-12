import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DataBoardComponent } from './data-board/data-board.component';
import { StudioComponent } from './studio.component';

const routes: Routes = [
  {
    path: '',
    component: StudioComponent,
    children: [
      { path: 'board', component: DataBoardComponent },
      { path: '', redirectTo: 'board', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class StudioRoutingModule {}
