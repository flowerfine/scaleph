import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslateModule} from '@ngx-translate/core';
import {SharedModule} from 'src/app/@shared/shared.module';
import {JobComponent} from './job.component';
import {JobRoutingModule} from './job.routing.module';
import {ArtifactComponent} from "./artifact/artifact.component";
import {ArtifactUploadComponent} from "./artifact/artifact-upload/artifact-upload.component";
import {ArtifactDeleteComponent} from "./artifact/artifact-delete/artifact-delete.component";
import {JobConfigComponent} from "./job-config/job-config.component";
import {JobConfigNewComponent} from "./job-config/job-config-new/job-config-new.component";
import {JobConfigDeleteComponent} from "./job-config/job-config-delete/job-config-delete.component";

@NgModule({
  declarations: [
    JobComponent,
    ArtifactComponent,
    ArtifactUploadComponent,
    ArtifactDeleteComponent,
    JobConfigComponent,
    JobConfigNewComponent,
    JobConfigDeleteComponent,
  ],
  imports: [SharedModule, JobRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class JobModule {
}
