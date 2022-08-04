import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslateModule} from '@ngx-translate/core';
import {SharedModule} from 'src/app/@shared/shared.module';
import {ResourceComponent} from './resource.component';
import {ResourceRoutingModule} from './resource.routing.module';
import {ReleaseSeatunnelComponent} from "./seatunnel/release-seatunnel.component";
import {ReleaseSeatunnelUploadComponent} from "./seatunnel/release-seatunnel-upload/release-seatunnel-upload.component";
import {ReleaseSeatunnelDeleteComponent} from "./seatunnel/release-seatunnel-delete/release-seatunnel-delete.component";
import {ReleaseFlinkComponent} from "./flink/release-flink.component";
import {ReleaseFlinkUploadComponent} from "./flink/release-flink-upload/release-flink-upload.component";
import {ReleaseFlinkDeleteComponent} from "./flink/release-flink-delete/release-flink-delete.component";

@NgModule({
  declarations: [
    ResourceComponent,
    ReleaseSeatunnelComponent,
    ReleaseSeatunnelUploadComponent,
    ReleaseSeatunnelDeleteComponent,
    ReleaseFlinkComponent,
    ReleaseFlinkUploadComponent,
    ReleaseFlinkDeleteComponent,
  ],
  imports: [SharedModule, ResourceRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class ResourceModule {
}
