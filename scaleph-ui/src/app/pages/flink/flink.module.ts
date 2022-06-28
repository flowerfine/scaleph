import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslateModule} from '@ngx-translate/core';
import {SharedModule} from 'src/app/@shared/shared.module';
import {FlinkComponent} from './flink.component';
import {FlinkRoutingModule} from './flink.routing.module';
import {ReleaseComponent} from './release/release.component';
import {ReleaseUploadComponent} from "./release/release-upload/release-upload.component";
import {ReleaseDeleteComponent} from "./release/release-delete/release-delete.component";
import {DeployConfigComponent} from "./deploy-config/deploy-config.component";
import {DeployConfigNewComponent} from "./deploy-config/deploy-config-new/deploy-config-new.component";
import {DeployConfigDeleteComponent} from "./deploy-config/deploy-config-delete/deploy-config-delete.component";
import {DeployConfigUpdateComponent} from "./deploy-config/deploy-config-update/deploy-config-update.component";
import {DeployConfigFileComponent} from "./deploy-config-file/deploy-config-file.component";
import {
  DeployConfigFileUploadComponent
} from "./deploy-config-file/deploy-config-file-upload/deploy-config-file-upload.component";
import {
  DeployConfigFileDeleteComponent
} from "./deploy-config-file/deploy-config-file-delete/deploy-config-file-delete.component";
import {ClusterConfigComponent} from "./cluster-config/cluster-config.component";
import {ClusterConfigNewComponent} from "./cluster-config/cluster-config-new/cluster-config-new.component";
import {ClusterConfigDeleteComponent} from "./cluster-config/cluster-config-delete/cluster-config-delete.component";

@NgModule({
  declarations: [
    FlinkComponent,
    ReleaseComponent,
    ReleaseUploadComponent,
    ReleaseDeleteComponent,
    DeployConfigComponent,
    DeployConfigNewComponent,
    DeployConfigUpdateComponent,
    DeployConfigDeleteComponent,
    DeployConfigFileComponent,
    DeployConfigFileUploadComponent,
    DeployConfigFileDeleteComponent,
    ClusterConfigComponent,
    ClusterConfigNewComponent,
    ClusterConfigDeleteComponent,
  ],
  imports: [SharedModule, FlinkRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class FlinkModule {
}
