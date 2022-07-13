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

@NgModule({
  declarations: [
    JobComponent,
    ArtifactComponent,
    ArtifactUploadComponent,
    ArtifactDeleteComponent,
    JobConfigComponent,
  ],
  imports: [SharedModule, JobRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class JobModule {
}
