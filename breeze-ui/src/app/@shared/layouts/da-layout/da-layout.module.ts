import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  DaLayoutComponent,
  DaLayoutFooterComponent,
  DaLayoutHeaderComponent,
  DaLayoutSecHeaderComponent,
  DaLayoutSecSidebarComponent,
  DaLayoutSidebarComponent,
} from './da-layout.component';
import { LayoutModule } from 'ng-devui/layout';

@NgModule({
  declarations: [
    DaLayoutComponent,
    DaLayoutSidebarComponent,
    DaLayoutSecSidebarComponent,
    DaLayoutHeaderComponent,
    DaLayoutSecHeaderComponent,
    DaLayoutFooterComponent,
  ],
  imports: [CommonModule, LayoutModule],
  exports: [
    DaLayoutComponent,
    DaLayoutSidebarComponent,
    DaLayoutSecSidebarComponent,
    DaLayoutHeaderComponent,
    DaLayoutSecHeaderComponent,
    DaLayoutFooterComponent,
  ],
})
export class DaLayoutModule {}
