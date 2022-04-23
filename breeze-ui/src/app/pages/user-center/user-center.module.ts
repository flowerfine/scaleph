import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserCenterComponent } from './user-center.component';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from 'src/app/@shared/shared.module';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UserSecurityComponent } from './user-security/user-security.component';
import { UserMessageComponent } from './user-message/user-message.component';
import { UserLogComponent } from './user-log/user-log.component';
import { UserCenterRoutingModule } from './user-center.routing.module';
import { EditPasswordComponent } from './user-security/edit-password/edit-password.component';
import { BindEmailComponent } from './user-security/bind-email/bind-email.component';

@NgModule({
  imports: [CommonModule, TranslateModule, SharedModule, UserCenterRoutingModule],
  declarations: [UserCenterComponent, UserProfileComponent, UserSecurityComponent, UserMessageComponent, UserLogComponent, EditPasswordComponent, BindEmailComponent],
})
export class UserCenterModule {}
