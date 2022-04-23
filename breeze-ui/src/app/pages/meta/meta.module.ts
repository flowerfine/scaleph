import { NgModule } from '@angular/core';
import { MetaComponent } from './meta.component';
import { DatasourceComponent } from './datasource/datasource.component';
import { SharedModule } from 'src/app/@shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { MetaRoutingModule } from './meta.routing.module';
import { DatasourceDeleteComponent } from './datasource/datasource-delete/datasource-delete.component';
import { DatasourceNewPreComponent } from './datasource/datasource-new-pre/datasource-new-pre.component';
import { DatasourceNewComponent } from './datasource/datasource-new/datasource-new.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DatasourceUpdateComponent } from './datasource/datasource-update/datasource-update.component';

@NgModule({
  declarations: [
    MetaComponent,
    DatasourceComponent,
    DatasourceDeleteComponent,
    DatasourceNewPreComponent,
    DatasourceNewComponent,
    DatasourceUpdateComponent,
  ],
  imports: [SharedModule, MetaRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class MetaModule {}
