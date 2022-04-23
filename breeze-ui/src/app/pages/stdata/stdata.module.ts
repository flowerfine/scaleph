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

@NgModule({
  declarations: [
    StdataComponent,
    RefdataComponent,
    SystemComponent,
    DataElementComponent,
    SystemNewComponent,
    SystemUpdateComponent,
    SystemDeleteComponent,
  ],
  imports: [SharedModule, StdataRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class StdataModule {}
