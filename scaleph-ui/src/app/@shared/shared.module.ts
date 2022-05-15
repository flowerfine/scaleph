import { ModuleWithProviders, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClipboardModule } from '@angular/cdk/clipboard';
import {
  LayoutModule,
  AccordionModule,
  SearchModule,
  AvatarModule,
  BadgeModule,
  DropDownModule,
  FormModule,
  TabsModule,
  TextInputModule,
  ToggleModule,
  CheckBoxModule,
  ButtonModule,
  DrawerModule,
  DCommonModule,
  AlertModule,
  ToastModule,
  TooltipModule,
  RadioModule,
  CardModule,
  DataTableModule,
  BreadcrumbModule,
  TagsModule,
  SelectModule,
  LoadingModule,
  ModalModule,
  PaginationModule,
  StatusModule,
  TreeModule,
  DatepickerModule,
  DatepickerProModule,
  TransferModule,
  PanelModule,
  TextareaModule,
  SplitterModule,
  TreeSelectModule,
  UploadModule,
  InputNumberModule,
  PopoverModule,
} from 'ng-devui';
import { I18nModule } from 'ng-devui/i18n';
import { RelativeTimeModule } from 'ng-devui/relative-time';
import { TranslateModule } from '@ngx-translate/core';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { LoginComponent } from './components/login/login.component';
import { SideSettingsComponent } from './components/side-settings/side-settings.component';
import { PersonalizeComponent } from './components/personalize/personalize.component';
import { HeaderOperationComponent } from './components/header/header-operation/header-operation.component';
import { NavbarComponent } from './components/header/navbar/navbar.component';
import { HeaderLogoComponent } from './components/header/header-logo/header-logo.component';
import { SideMenuComponent } from './components/side-menu/side-menu.component';
import { RegisterComponent } from './components/register/register.component';
import { HeaderNoticeComponent } from './components/header/header-operation/header-notice/header-notice.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

const DEVUI_MODULES = [
  LayoutModule,
  AccordionModule,
  SearchModule,
  AvatarModule,
  BadgeModule,
  DropDownModule,
  FormModule,
  TabsModule,
  TextInputModule,
  ToggleModule,
  CheckBoxModule,
  ButtonModule,
  DrawerModule,
  CardModule,
  DataTableModule,
  BreadcrumbModule,
  TagsModule,
  SelectModule,
  RadioModule,
  LoadingModule,
  ModalModule,
  PaginationModule,
  StatusModule,
  TreeModule,
  DatepickerModule,
  DatepickerProModule,
  TransferModule,
  RelativeTimeModule,
  TextareaModule,
  PanelModule,
  SplitterModule,
  TreeSelectModule,
  UploadModule,
  PopoverModule,
  InputNumberModule,
];
const COMPONENTS = [HeaderComponent, FooterComponent, NavbarComponent, PersonalizeComponent];
@NgModule({
  declarations: [
    LoginComponent,
    SideSettingsComponent,
    HeaderOperationComponent,
    HeaderLogoComponent,
    SideMenuComponent,
    HeaderNoticeComponent,
    RegisterComponent,
    ...COMPONENTS,
    NotificationsComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    TranslateModule,
    DCommonModule,
    AlertModule,
    ClipboardModule,
    ToastModule,
    TooltipModule,
    I18nModule,
    FontAwesomeModule,
    ...DEVUI_MODULES,
  ],
  exports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    HeaderLogoComponent,
    HeaderOperationComponent,
    I18nModule,
    SideMenuComponent,
    ...DEVUI_MODULES,
    ...COMPONENTS,
  ],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders<SharedModule> {
    return {
      ngModule: SharedModule,
      providers: [],
    };
  }
}
