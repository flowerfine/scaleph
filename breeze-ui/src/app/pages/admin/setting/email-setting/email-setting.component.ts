import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { EmailConfig } from 'src/app/@core/data/admin.data';
import { SystemConfigService } from 'src/app/@core/services/admin/system-config.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-email-setting',
  templateUrl: './email-setting.component.html',
  styleUrls: ['../setting.component.scss'],
})
export class EmailSettingComponent implements OnInit {
  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    emailRules: {
      validators: [{ required: true }, { maxlength: 100 }, { email: true }],
    },
    hostRules: {
      validators: [{ required: true }],
    },
    passwordRules: {
      validators: [{ required: true }],
    },
    portRules: {
      validators: [{ required: true }],
    },
  };
  formData: EmailConfig = {
    email: '',
    host: '',
    password: '',
    port: 25,
  };

  constructor(
    private translate: TranslateService,
    private systemConfigService: SystemConfigService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.refreshEmailInfo();
  }

  refreshEmailInfo() {
    this.systemConfigService.getEmailConfig().subscribe((d) => {
      this.formData = d;
    });
  }

  submitForm({ valid }) {
    if (valid) {
      this.systemConfigService.configEmail(this.formData).subscribe((d) => {
        if (d.success) {
          this.notificationService.success(this.translate.instant('app.common.operate.success'));
          this.refreshEmailInfo();
        }
      });
    }
  }
}
