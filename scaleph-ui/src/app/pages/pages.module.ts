import { NgModule } from '@angular/core';
import { DialogService, BackTopModule } from 'ng-devui';
import { SharedModule } from '../@shared/shared.module';
import { PagesRoutingModule } from './pages-routing.module';
import { PagesComponent } from './pages.component';
import { DaLayoutModule } from '../@shared/layouts/da-layout';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  imports: [PagesRoutingModule, SharedModule, BackTopModule, DaLayoutModule, TranslateModule],
  declarations: [PagesComponent],
  providers: [DialogService],
})
export class PagesModule {}
