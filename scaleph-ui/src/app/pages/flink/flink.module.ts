import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslateModule} from '@ngx-translate/core';
import {SharedModule} from 'src/app/@shared/shared.module';
import {FlinkComponent} from './flink.component';
import {FlinkRoutingModule} from './flink.routing.module';
import {ClusterConfigComponent} from "./cluster-config/cluster-config.component";
import {ClusterConfigNewComponent} from "./cluster-config/cluster-config-new/cluster-config-new.component";
import {ClusterConfigDeleteComponent} from "./cluster-config/cluster-config-delete/cluster-config-delete.component";
import {ClusterConfigUpdateComponent} from "./cluster-config/cluster-config-update/cluster-config-update.component";
import {ClusterConfigOptionsComponent} from "./cluster-config-options/cluster-config-options.component";
import {ClusterInstanceComponent} from "./cluster-instance/cluster-instance.component";
import {
  SessionClusterInstanceNewComponent
} from "./cluster-instance/session-cluster-instance-new/session-cluster-instance-new.component";
import {
  ClusterInstanceShutdownComponent
} from "./cluster-instance/cluster-instance-shutdown/cluster-instance-shutdown.component";

@NgModule({
  declarations: [
    FlinkComponent,
    ClusterConfigComponent,
    ClusterConfigNewComponent,
    ClusterConfigUpdateComponent,
    ClusterConfigDeleteComponent,
    ClusterConfigOptionsComponent,
    ClusterInstanceComponent,
    SessionClusterInstanceNewComponent,
    ClusterInstanceShutdownComponent,
  ],
  imports: [SharedModule, FlinkRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class FlinkModule {
}
