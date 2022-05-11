import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { DataSourceMeta } from 'src/app/@core/data/datadev.data';
import { DataSourceService } from 'src/app/@core/services/datadev/datasource.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-datasource-new',
  templateUrl: './datasource-new.component.html',
  styleUrls: ['../datasource.component.scss'],
})
export class DatasourceNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  formLayout = FormLayout.Horizontal;
  jdbcPropsPlaceholder: string;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    dataSourceNameRules: {
      validators: [
        { required: true },
        { maxlength: 60 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    hostNameRules: {
      validators: [{ maxlength: 250 }],
    },
    databaseNameRules: {
      validators: [{ maxlength: 50 }],
    },
    portRules: {
      validators: [{ pattern: /^[0-9]*$/, message: this.translate.instant('app.common.validate.number') }],
    },
    userNameRules: {
      validators: [{ maxlength: 100 }],
    },
    passwordRules: {
      validators: [{ maxlength: 200 }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    dataSourceName: null,
    hostName: null,
    databaseName: null,
    port: null,
    userName: null,
    password: null,
    remark: null,
    generalProps: null,
    jdbcProps: null,
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private datasourceService: DataSourceService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.jdbcPropsPlaceholder = this.translate.instant('datadev.jdbcProps.placeholder');
  }

  submitForm({ valid }) {
    let ds: DataSourceMeta = {
      dataSourceName: this.formData.dataSourceName,
      dataSourceType: { value: this.data.data },
      connectionType: { value: 'jdbc' },
      hostName: this.formData.hostName,
      databaseName: this.formData.databaseName,
      port: this.formData.port,
      userName: this.formData.userName,
      password: this.formData.password,
      remark: this.formData.remark,
      jdbcProps: this.formData.jdbcProps,
    };
    if (valid) {
      this.datasourceService.add(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  testConnection() {
    let ds: DataSourceMeta = {
      dataSourceName: this.formData.dataSourceName,
      dataSourceType: { value: this.data.data },
      connectionType: { value: 'jdbc' },
      hostName: this.formData.hostName,
      databaseName: this.formData.databaseName,
      port: this.formData.port,
      userName: this.formData.userName,
      password: this.formData.password,
      remark: this.formData.remark,
      jdbcProps: this.formData.jdbcProps,
      passwdChanged: true,
    };
    if (this.formDir.isReady) {
      this.datasourceService.testConnection(ds).subscribe((d) => {
        if (d.success) {
          this.notificationService.success(this.translate.instant('datadev.testConnect.success'));
        }
      });
    }
  }

  isRdbms(): boolean {
    let type: string = this.data.data;
    if (type == null || type == undefined || type == '') {
      return false;
    } else if (type == 'mysql' || type == 'oracle' || type == 'postgre') {
      return true;
    } else {
      return false;
    }
  }

  isBigdata(): boolean {
    let type: string = this.data.data;
    if (type == null || type == undefined || type == '') {
      return false;
    } else if (type == 'hive' || type == 'hbase') {
      return true;
    } else {
      return false;
    }
  }

  isJdbc(): boolean {
    let type: string = this.data.data;
    if (type == null || type == undefined || type == '') {
      return false;
    } else if (type == 'jdbc') {
      return true;
    } else {
      return false;
    }
  }
}
