import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { DataSourceMeta } from 'src/app/@core/data/meta.data';
import { DataSourceService } from 'src/app/@core/services/datasource.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-datasource-update',
  templateUrl: './datasource-update.component.html',
  styleUrls: ['../datasource.component.scss'],
})
export class DatasourceUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  formLayout = FormLayout.Horizontal;
  jdbcPropsPlaceholder: string;
  oldPassword: string;
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
    id: null,
    dataSourceName: null,
    hostName: null,
    databaseName: null,
    port: null,
    userName: null,
    password: null,
    remark: null,
    generalProps: null,
    jdbcProps: '',
  };
  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private datasourceService: DataSourceService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.jdbcPropsPlaceholder = this.translate.instant('meta.jdbcProps.placeholder');
    this.formData = {
      id: this.data.item.id,
      dataSourceName: this.data.item.dataSourceName,
      hostName: this.data.item.hostName,
      databaseName: this.data.item.databaseName,
      port: this.data.item.port,
      userName: this.data.item.userName,
      password: this.data.item.password,
      remark: this.data.item.remark,
      generalProps: this.data.item.generalProps,
      jdbcProps: this.data.item.jdbcProps,
    };
    this.oldPassword = this.data.item.password;
  }

  submitForm({ valid }) {
    let ds: DataSourceMeta = {
      id: this.formData.id,
      dataSourceName: this.formData.dataSourceName,
      dataSourceType: this.data.item.dataSourceType,
      connectionType: { value: 'jdbc' },
      hostName: this.formData.hostName,
      databaseName: this.formData.databaseName,
      port: this.formData.port,
      userName: this.formData.userName,
      password: this.formData.password,
      remark: this.formData.remark,
      jdbcProps: this.formData.jdbcProps,
      passwdChanged: this.formData.password != this.oldPassword,
    };
    if (valid) {
      this.datasourceService.update(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  testConnection() {
    let ds: DataSourceMeta = {
      id: this.formData.id,
      dataSourceName: this.formData.dataSourceName,
      dataSourceType: this.data.item.dataSourceType,
      connectionType: { value: 'jdbc' },
      hostName: this.formData.hostName,
      databaseName: this.formData.databaseName,
      port: this.formData.port,
      userName: this.formData.userName,
      password: this.formData.password,
      remark: this.formData.remark,
      jdbcProps: this.formData.jdbcProps || '',
      passwdChanged: this.formData.password != this.oldPassword,
    };
    if (this.formDir.isReady) {
      this.datasourceService.testConnection(ds).subscribe((d) => {
        if (d.success) {
          this.notificationService.success(this.translate.instant('meta.testConnect.success'));
        }
      });
    }
  }

  isRdbms(): boolean {
    let type: string = this.data.item.dataSourceType.value;
    if (type == null || type == undefined || type == '') {
      return false;
    } else if (type == 'mysql' || type == 'oracle' || type == 'postgre') {
      return true;
    } else {
      return false;
    }
  }

  isBigdata(): boolean {
    let type: string = this.data.item.dataSourceType.value;
    if (type == null || type == undefined || type == '') {
      return false;
    } else if (type == 'hive' || type == 'hbase') {
      return true;
    } else {
      return false;
    }
  }

  isJdbc(): boolean {
    let type: string = this.data.item.dataSourceType.value;
    if (type == null || type == undefined || type == '') {
      return false;
    } else if (type == 'jdbc') {
      return true;
    } else {
      return false;
    }
  }
}
