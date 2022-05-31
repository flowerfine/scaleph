import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ModalService } from 'ng-devui';
import { SecUser } from 'src/app/@core/data/admin.data';
import { UserService } from 'src/app/@core/services/admin/user.service';
import { BindEmailComponent } from './bind-email/bind-email.component';
import { EditPasswordComponent } from './edit-password/edit-password.component';

@Component({
  selector: 'app-user-security',
  templateUrl: './user-security.component.html',
  styleUrls: ['../user-center.component.scss'],
})
export class UserSecurityComponent implements OnInit {
  userInfo: SecUser = {};
  constructor(private translate: TranslateService, private userService: UserService, private modalService: ModalService) {}

  ngOnInit(): void {
    this.refreshUserInfo();
  }

  refreshUserInfo() {
    this.userService.getUserInfo().subscribe((d: SecUser) => {
      this.userInfo = d;
    });
  }

  openEditPasswordDialog() {
    const results = this.modalService.open({
      id: 'user-edit-password',
      width: '520px',
      backdropCloseable: true,
      component: EditPasswordComponent,
      data: {
        title: this.translate.instant('userCenter.editPassword'),
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
      },
    });
  }

  openBindEmailDialog() {
    const results = this.modalService.open({
      id: 'bind-email',
      width: '520px',
      backdropCloseable: true,
      component: BindEmailComponent,
      data: {
        title: this.translate.instant('userCenter.bindEmail'),
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: (event: any) => {
          this.refreshUserInfo();
        },
      },
    });
  }
}
