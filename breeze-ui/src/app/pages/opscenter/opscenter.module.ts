import { NgModule } from '@angular/core';
import { BatchJobComponent } from './batch-job/batch-job.component';
import { RealtimeJobComponent } from './realtime-job/realtime-job.component';
import { OpsCenterRoutingModule } from './opscenter.routing.module';
import { SharedModule } from 'src/app/@shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OpsCenterComponent } from './opscenter.component';

@NgModule({
  declarations: [OpsCenterComponent, BatchJobComponent, RealtimeJobComponent],
  imports: [SharedModule, OpsCenterRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class OpsCenterModule {}
