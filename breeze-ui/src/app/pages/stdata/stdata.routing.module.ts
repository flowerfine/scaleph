import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DataElementComponent } from './data-element/data-element.component';
import { RefdataComponent } from './refdata/refdata.component';
import { StdataComponent } from './stdata.component';
import { SystemComponent } from './system/system.component';

const routes: Routes = [
  {
    path: '',
    component: StdataComponent,
    children: [
      { path: 'refdata', component: RefdataComponent },
      { path: 'dataElement', component: DataElementComponent },
      { path: 'system', component: SystemComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class StdataRoutingModule {}
