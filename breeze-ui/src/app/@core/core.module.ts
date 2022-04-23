import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';

import { throwIfAlreadyLoaded } from './module-import-guard';
import { AuthService } from './services/auth.service';
import { PersonalizeService } from './services/personalize.service';
import { AuthGuardService } from './services/auth-guard-service.guard';
import { CustomThemeService } from './services/custom-theme.service';

export const DEVUI_CORE_PROVIDERS = [AuthService, PersonalizeService, AuthGuardService, CustomThemeService];

@NgModule({
  declarations: [],
  imports: [CommonModule],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }

  static forRoot(): ModuleWithProviders<CoreModule> {
    return {
      ngModule: CoreModule,
      providers: [...DEVUI_CORE_PROVIDERS],
    };
  }
}
