import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from 'src/app/@shared/shared.module';
import { FlinkComponent } from './flink.component';
import { FlinkRoutingModule } from './flink.routing.module';
import { ReleaseComponent } from './release/release.component';

@NgModule({
  declarations: [
    FlinkComponent,
    ReleaseComponent
  ],
  imports: [SharedModule, FlinkRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule],
})
export class FlinkModule {}
