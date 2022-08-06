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
import {ClusterConfigComponent} from "./cluster/cluster-config.component";
import {ClusterConfigNewComponent} from "./cluster/cluster-config-new/cluster-config-new.component";
import {ClusterConfigUpdateComponent} from "./cluster/cluster-config-update/cluster-config-update.component";
import {ClusterConfigDeleteComponent} from "./cluster/cluster-config-delete/cluster-config-delete.component";
import {ClusterConfigFileComponent} from "./cluster/cluster-config-file/cluster-config-file.component";
import {
  ClusterConfigFileUploadComponent
} from "./cluster/cluster-config-file/cluster-config-file-upload/cluster-config-file-upload.component";
import {
  ClusterConfigFileDeleteComponent
} from "./cluster/cluster-config-file/cluster-config-file-delete/cluster-config-file-delete.component";

@NgModule({
  declarations: [
    ResourceComponent,
    ReleaseSeatunnelComponent,
    ReleaseSeatunnelUploadComponent,
    ReleaseSeatunnelDeleteComponent,
    ReleaseFlinkComponent,
    ReleaseFlinkUploadComponent,
    ReleaseFlinkDeleteComponent,
    ClusterConfigComponent,
    ClusterConfigNewComponent,
    ClusterConfigUpdateComponent,
    ClusterConfigDeleteComponent,
    ClusterConfigFileComponent,
    ClusterConfigFileUploadComponent,
    ClusterConfigFileDeleteComponent,
  ],
  imports: [SharedModule, ResourceRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class ResourceModule {
}
