import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from 'src/app/@shared/shared.module';
import { FlinkComponent } from './flink.component';
import { FlinkRoutingModule } from './flink.routing.module';
import { ReleaseComponent } from './release/release.component';
import {ReleaseUploadComponent} from "./release/release-upload/release-upload.component";
import {ReleaseDeleteComponent} from "./release/release-delete/release-delete.component";
import {DeployConfigComponent} from "./deploy-config/deploy-config.component";
import {DeployConfigUploadComponent} from "./deploy-config/deploy-config-upload/deploy-config-upload.component";
import {DeployConfigNewComponent} from "./deploy-config/deploy-config-new/deploy-config-new.component";
import {DeployConfigDeleteComponent} from "./deploy-config/deploy-config-delete/deploy-config-delete.component";

@NgModule({
  declarations: [
    FlinkComponent,
    ReleaseComponent,
    ReleaseUploadComponent,
    ReleaseDeleteComponent,
    DeployConfigComponent,
    DeployConfigNewComponent,
    DeployConfigDeleteComponent,
    DeployConfigUploadComponent,
  ],
  imports: [SharedModule, FlinkRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class FlinkModule {}
