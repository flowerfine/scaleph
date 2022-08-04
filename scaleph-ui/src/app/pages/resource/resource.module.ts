import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslateModule} from '@ngx-translate/core';
import {SharedModule} from 'src/app/@shared/shared.module';
import {ResourceComponent} from './resource.component';
import {ResourceRoutingModule} from './resource.routing.module';
import {ArtifactComponent} from "../job/artifact/artifact.component";
import {ArtifactDeleteComponent} from "../job/artifact/artifact-delete/artifact-delete.component";
import {JobConfigUpdateComponent} from "../job/job-config/job-config-update/job-config-update.component";
import {JobInstanceComponent} from "../job/job-instance/job-instance.component";
import {JobConfigNewComponent} from "../job/job-config/job-config-new/job-config-new.component";
import {JobConfigComponent} from "../job/job-config/job-config.component";
import {ArtifactUploadComponent} from "../job/artifact/artifact-upload/artifact-upload.component";

@NgModule({
  declarations: [
    ResourceComponent,
    ArtifactComponent,
    ArtifactUploadComponent,
    ArtifactDeleteComponent,
    JobConfigComponent,
    JobConfigNewComponent,
    JobConfigUpdateComponent,
    JobConfigDeleteComponent,
    JobInstanceComponent,
  ],
  imports: [SharedModule, ResourceRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class ResourceModule {
}
