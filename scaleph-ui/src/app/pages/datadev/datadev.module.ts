import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from 'src/app/@shared/shared.module';
import { DataDevComponent } from './datadev.component';
import { DataDevRoutingModule } from './datadev.routing.module';
import { DatasourceDeleteComponent } from './datasource/datasource-delete/datasource-delete.component';
import { DatasourceNewPreComponent } from './datasource/datasource-new-pre/datasource-new-pre.component';
import { DatasourceNewComponent } from './datasource/datasource-new/datasource-new.component';
import { DatasourceUpdateComponent } from './datasource/datasource-update/datasource-update.component';
import { DatasourceComponent } from './datasource/datasource.component';
import { WorkbenchComponent } from './workbench/workbench.component';
import { JobComponent } from './job/job.component';
import { BaseNodeComponent } from './workbench/base-node/base-node.component';
import { ProjectComponent } from './project/project.component';
import { ProjectNewComponent } from './project/project-new/project-new.component';
import { ProjectDeleteComponent } from './project/project-delete/project-delete.component';
import { ProjectUpdateComponent } from './project/project-update/project-update.component';
import { JobNewComponent } from './job/job-new/job-new.component';
import { JobDeleteComponent } from './job/job-delete/job-delete.component';
import { JobUpdateComponent } from './job/job-update/job-update.component';
import { DirectoryNewComponent } from './job/directory-new/directory-new.component';
import { DirectoryUpdateComponent } from './job/directory-update/directory-update.component';
import { DirectoryDeleteComponent } from './job/directory-delete/directory-delete.component';
import { JobStartComponent } from './job/job-start/job-start.component';
import { JobPropertityComponent } from './workbench/job-propertity/job-propertity.component';
import { StepPropertityComponent } from './workbench/step-propertity/step-propertity.component';
import { SourceTableStepComponent } from './workbench/steps/source-table-step/source-table-step.component';
import { SinkTableStepComponent } from './workbench/steps/sink-table-step/sink-table-step.component';
import { ResourceComponent } from './resource/resource.component';
import { ClusterComponent } from './cluster/cluster.component';
import { ResourceNewComponent } from './resource/resource-new/resource-new.component';
import { ResourceDeleteComponent } from './resource/resource-delete/resource-delete.component';
import { ClusterNewComponent } from './cluster/cluster-new/cluster-new.component';
import { ClusterUpdateComponent } from './cluster/cluster-update/cluster-update.component';
import { ClusterDeleteComponent } from './cluster/cluster-delete/cluster-delete.component';
import { JobStopComponent } from './job/job-stop/job-stop.component';
import { JobCrontabSettingComponent } from './job/job-crontab-setting/job-crontab-setting.component';
import { JdbcDatasourceComponent } from './datasource/jdbc-datasource/jdbc-datasource.component';
import { GenericDbDatasourceComponent } from './datasource/generic-db-datasource/generic-db-datasource.component';
import { SinkConsoleStepComponent } from './workbench/steps/sink-console-step/sink-console-step.component';
import { SourceMockStepComponent } from './workbench/steps/source-mock-step/source-mock-step.component';
import { SourceMockStreamStepComponent } from './workbench/steps/source-mock-stream-step/source-mock-stream-step.component';
import { KafkaDatasourceComponent } from './datasource/kafka-datasource/kafka-datasource.component';
import { SourceKafkaStepComponent } from './workbench/steps/source-kafka-step/source-kafka-step.component';
import { SinkKafkaStepComponent } from './workbench/steps/sink-kafka-step/sink-kafka-step.component';
import { DorisDatasourceComponent } from './datasource/doris-datasource/doris-datasource.component';
import { SinkDorisStepComponent } from './workbench/steps/sink-doris-step/sink-doris-step.component';

@NgModule({
  declarations: [
    DataDevComponent,
    DatasourceComponent,
    DatasourceDeleteComponent,
    DatasourceNewPreComponent,
    DatasourceNewComponent,
    DatasourceUpdateComponent,
    WorkbenchComponent,
    JobComponent,
    BaseNodeComponent,
    ProjectComponent,
    ProjectNewComponent,
    ProjectDeleteComponent,
    ProjectUpdateComponent,
    JobNewComponent,
    JobDeleteComponent,
    JobUpdateComponent,
    DirectoryNewComponent,
    DirectoryUpdateComponent,
    DirectoryDeleteComponent,
    JobStartComponent,
    JobPropertityComponent,
    StepPropertityComponent,
    SourceTableStepComponent,
    SinkTableStepComponent,
    ResourceComponent,
    ClusterComponent,
    ResourceNewComponent,
    ResourceDeleteComponent,
    ClusterNewComponent,
    ClusterUpdateComponent,
    ClusterDeleteComponent,
    JobStopComponent,
    JobCrontabSettingComponent,
    JdbcDatasourceComponent,
    GenericDbDatasourceComponent,
    SinkConsoleStepComponent,
    SourceMockStepComponent,
    SourceMockStreamStepComponent,
    KafkaDatasourceComponent,
    SourceKafkaStepComponent,
    SinkKafkaStepComponent,
    DorisDatasourceComponent,
    SinkDorisStepComponent,
  ],
  imports: [SharedModule, DataDevRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class DatadevModule {}
