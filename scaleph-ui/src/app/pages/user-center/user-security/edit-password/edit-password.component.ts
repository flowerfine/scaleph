import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { UserService } from 'src/app/@core/services/admin/user.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-edit-password',
  templateUrl: './edit-password.component.html',
  styleUrls: ['../../user-center.component.scss'],
})
export class EditPasswordComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    oldPasswordRules: {
      validators: [
        { required: true },
        { minlength: 6 },
        { maxlength: 32 },
        { pattern: /^[a-zA-Z0-9\d@$!%*?&.]+(\s+[a-zA-Z0-9]+)*$/, message: this.translate.instant('app.common.validate.patternPassword') },
      ],
    },
    passwordRules: {
      validators: [
        { required: true },
        { minlength: 6 },
        { maxlength: 32 },
        {
          sameToOldPassWord: this.sameToOldPassWord.bind(this),
          message: this.translate.instant('app.common.validate.sameToOldPassword'),
        },
        { pattern: /^[a-zA-Z0-9\d@$!%*?&.]+(\s+[a-zA-Z0-9]+)*$/, message: this.translate.instant('app.common.validate.patternPassword') },
      ],
    },
    confirmPasswordRules: {
      validators: [
        { required: true },
        {
          sameToPassWord: this.sameToPassWord.bind(this),
          message: this.translate.instant('app.common.validate.samePassword'),
        },
        { minlength: 6 },
        { maxlength: 32 },
        { pattern: /^[a-zA-Z0-9\d@$!%*?&.]+(\s+[a-zA-Z0-9]+)*$/, message: this.translate.instant('app.common.validate.patternPassword') },
      ],
    },
  };

  formData = {
    oldPassword: '',
    password: '',
    confirmPassword: '',
  };

  constructor(
    private route: Router,
    private elr: ElementRef,
    private translate: TranslateService,
    private userService: UserService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  sameToPassWord(value: string) {
    return value === this.formData.password;
  }

  sameToOldPassWord(value: string) {
    return value != this.formData.oldPassword;
  }

  submitForm({ valid }) {
    if (valid) {
      this.userService.editPassword(this.formData).subscribe((d) => {
        if (d.success) {
          this.notificationService.info(this.translate.instant('app.common.operate.success'));
          this.data.onClose();
          setTimeout(() => {
            this.route.navigate(['/login']);
          }, 3000);
        }
      });
    }
  }
}
