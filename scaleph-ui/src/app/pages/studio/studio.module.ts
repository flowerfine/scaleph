import { NgModule } from '@angular/core';
import { StudioComponent } from './studio.component';
import { SharedModule } from 'src/app/@shared/shared.module';
import { StudioRoutingModule } from './studio.routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DataBoardComponent } from './data-board/data-board.component';
import { EchartsModule } from 'src/app/@shared/components/echarts';

@NgModule({
  declarations: [StudioComponent, DataBoardComponent],
  imports: [SharedModule, StudioRoutingModule, TranslateModule, FormsModule, ReactiveFormsModule, EchartsModule],
})
export class StudioModule {}
