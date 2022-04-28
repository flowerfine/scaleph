import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/@shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { StdataRoutingModule } from './stdata.routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RefdataComponent } from './refdata/refdata.component';
import { SystemComponent } from './system/system.component';
import { DataElementComponent } from './data-element/data-element.component';
import { SystemNewComponent } from './system/system-new/system-new.component';
import { SystemUpdateComponent } from './system/system-update/system-update.component';
import { SystemDeleteComponent } from './system/system-delete/system-delete.component';
import { StdataComponent } from './stdata.component';
import { DataElementNewComponent } from './data-element/data-element-new/data-element-new.component';
import { DataElementUpdateComponent } from './data-element/data-element-update/data-element-update.component';
import { DataElementDeleteComponent } from './data-element/data-element-delete/data-element-delete.component';
import { RefdataTypeNewComponent } from './refdata/refdata-type-new/refdata-type-new.component';
import { RefdataTypeUpdateComponent } from './refdata/refdata-type-update/refdata-type-update.component';
import { RefdataTypeDeleteComponent } from './refdata/refdata-type-delete/refdata-type-delete.component';
import { RefdataDataNewComponent } from './refdata/refdata-data-new/refdata-data-new.component';
import { RefdataDataUpdateComponent } from './refdata/refdata-data-update/refdata-data-update.component';
import { RefdataDataDeleteComponent } from './refdata/refdata-data-delete/refdata-data-delete.component';
import { RefdataMapComponent } from './refdata-map/refdata-map.component';
import { RefdataMapNewComponent } from './refdata-map/refdata-map-new/refdata-map-new.component';
import { RefdataMapDeleteComponent } from './refdata-map/refdata-map-delete/refdata-map-delete.component';

@NgModule({
  declarations: [
    StdataComponent,
    RefdataComponent,
    SystemComponent,
    DataElementComponent,
    SystemNewComponent,
    SystemUpdateComponent,
    SystemDeleteComponent,
    DataElementNewComponent,
    DataElementUpdateComponent,
    DataElementDeleteComponent,
    RefdataTypeNewComponent,
    RefdataTypeUpdateComponent,
    RefdataTypeDeleteComponent,
    RefdataDataNewComponent,
    RefdataDataUpdateComponent,
    RefdataDataDeleteComponent,
    RefdataMapComponent,
    RefdataMapNewComponent,
    RefdataMapDeleteComponent,
  ],
  imports: [SharedModule, StdataRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class StdataModule {}
