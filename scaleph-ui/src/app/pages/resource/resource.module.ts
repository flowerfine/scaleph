import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslateModule} from '@ngx-translate/core';
import {SharedModule} from 'src/app/@shared/shared.module';
import {ResourceComponent} from './resource.component';
import {ResourceRoutingModule} from './resource.routing.module';
import {ReleaseSeatunnelComponent} from "./seatunnel/release-seatunnel.component";
import {ReleaseSeatunnelUploadComponent} from "./seatunnel/release-seatunnel-upload/release-seatunnel-upload.component";
import {ReleaseSeatunnelDeleteComponent} from "./seatunnel/release-seatunnel-delete/release-seatunnel-delete.component";

@NgModule({
  declarations: [
    ResourceComponent,
    ReleaseSeatunnelComponent,
    ReleaseSeatunnelUploadComponent,
    ReleaseSeatunnelDeleteComponent,
  ],
  imports: [SharedModule, ResourceRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class ResourceModule {
}
