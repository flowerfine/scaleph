import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { BasicConfig } from 'src/app/@core/data/admin.data';
import { SystemConfigService } from 'src/app/@core/services/system-config.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-basic-setting',
  templateUrl: './basic-setting.component.html',
  styleUrls: ['../setting.component.scss'],
})
export class BasicSettingComponent implements OnInit {
  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    seatunnelHomeRules: {
      validators: [{ required: true }, { maxlength: 1024 }],
    },
  };
  formData: BasicConfig = {
    seatunnelHome: '',
  };
  constructor(
    private translate: TranslateService,
    private systemConfigService: SystemConfigService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.refreshBasicConfig();
  }

  refreshBasicConfig() {
    this.systemConfigService.getBasicConfig().subscribe((d) => {
      this.formData = d;
    });
  }

  submitForm({ valid }) {
    if (valid) {
      this.systemConfigService.configBasic(this.formData).subscribe((d) => {
        if (d.success) {
          this.notificationService.success(this.translate.instant('app.common.operate.success'));
          this.refreshBasicConfig();
        }
      });
    }
  }
}
