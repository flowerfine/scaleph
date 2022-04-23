import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { UserService } from 'src/app/@core/services/user.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-bind-email',
  templateUrl: './bind-email.component.html',
  styleUrls: ['../../user-center.component.scss'],
})
export class BindEmailComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') bindForm: DFormGroupRuleDirective;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    emailRules: {
      validators: [{ required: true }, { maxlength: 100 }, { email: true }],
      asyncValidators: [
        {
          sameName: this.sameEmail.bind(this),
          message: this.translate.instant('app.common.validate.sameEmail'),
        },
      ],
    },
    authCodeRules: {
      validators: [{ required: true }, { maxlength: 10 }],
    },
  };

  formData = {
    email: '',
    authCode: '',
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private userService: UserService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  getEmailAuthCode() {
    this.userService.getAuthCode(this.formData.email).subscribe((d) => {
      if (d.success) {
        this.notificationService.info(this.translate.instant('userCenter.getAuthCode.info'));
      }
    });
  }

  submitForm({ valid }) {
    if (valid) {
      this.userService.bindEmail(this.formData).subscribe((d) => {
        if (d.success) {
          this.notificationService.info(this.translate.instant('app.common.operate.success'));
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  sameEmail(value) {
    return this.userService.isEmailExists(value);
  }

  emailValid() {
    return !(this.formData.email != null && this.formData.email != undefined && this.formData.email != '');
  }
}
