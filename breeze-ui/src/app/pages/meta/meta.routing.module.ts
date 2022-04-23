import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DatasourceComponent } from './datasource/datasource.component';
import { MetaComponent } from './meta.component';

const routes: Routes = [
  {
    path: '',
    component: MetaComponent,
    children: [{ path: 'datasource', component: DatasourceComponent }],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MetaRoutingModule {}
