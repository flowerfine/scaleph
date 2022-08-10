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
import {ClusterCredentialComponent} from "./cluster/cluster-credential.component";
import {ClusterCredentialNewComponent} from "./cluster/cluster-credential-new/cluster-credential-new.component";
import {
  ClusterCredentialUpdateComponent
} from "./cluster/cluster-credential-update/cluster-credential-update.component";
import {
  ClusterCredentialDeleteComponent
} from "./cluster/cluster-credential-delete/cluster-credential-delete.component";
import {ClusterCredentialFileComponent} from "./cluster/cluster-credential-file/cluster-credential-file.component";
import {
  ClusterCredentialFileUploadComponent
} from "./cluster/cluster-credential-file/cluster-credential-file-upload/cluster-credential-file-upload.component";
import {
  ClusterCredentialFileDeleteComponent
} from "./cluster/cluster-credential-file/cluster-credential-file-delete/cluster-credential-file-delete.component";

@NgModule({
  declarations: [
    ResourceComponent,
    ReleaseSeatunnelComponent,
    ReleaseSeatunnelUploadComponent,
    ReleaseSeatunnelDeleteComponent,
    ReleaseFlinkComponent,
    ReleaseFlinkUploadComponent,
    ReleaseFlinkDeleteComponent,
    ClusterCredentialComponent,
    ClusterCredentialNewComponent,
    ClusterCredentialUpdateComponent,
    ClusterCredentialDeleteComponent,
    ClusterCredentialFileComponent,
    ClusterCredentialFileUploadComponent,
    ClusterCredentialFileDeleteComponent,
  ],
  imports: [SharedModule, ResourceRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class ResourceModule {
}
