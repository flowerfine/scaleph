import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { MetaDataSource } from 'src/app/@core/data/datadev.data';
import { DataSourceService } from 'src/app/@core/services/datadev/datasource.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-clickhouse-datasource',
  templateUrl: './clickhouse-datasource.component.html',
  styleUrls: ['../datasource.component.scss'],
})
export class ClickHouseDatasourceComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  isPasswordChanged: boolean = false;
  additionalPropsPlaceholder: string = '';
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    datasourceNameRules: {
      validators: [
        { required: true },
        { maxlength: 60 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    hostRules: {
      validators: [{ required: true }, { maxlength: 256 }],
    },
    databaseNameRules: {
      validators: [{ required: true }, { maxlength: 64 }],
    },
    usernameRules: {
      validators: [{ required: true }, { maxlength: 120 }],
    },
    passwordRules: {
      validators: [{ required: true }, { maxlength: 120 }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
    additionalPropsRules: {
      validators: [{ maxlength: 2048 }],
    },
  };

  formData = {
    id: null,
    datasourceName: null,
    host: null,
    databaseName: null,
    username: null,
    password: null,
    remark: null,
    additionalPropsStr: null,
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private datasourceService: DataSourceService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.additionalPropsPlaceholder = this.translate.instant('datadev.additionalProps.placeholder');
    if (this.data.item.item) {
      this.formData = {
        id: this.data.item.item.id,
        datasourceName: this.data.item.item.datasourceName,
        host: this.data.item.item.props.host,
        databaseName: this.data.item.item.props.databaseName,
        username: this.data.item.item.props.username,
        password: '***',
        remark: this.data.item.item.remark,
        additionalPropsStr: this.data.item.item.additionalPropsStr,
      };
    }
  }

  submitForm({ valid }) {
    let ds: MetaDataSource = {
      id: this.formData.id,
      datasourceName: this.formData.datasourceName,
      datasourceType: this.data.item?.item?.id ? this.data.item?.item?.datasourceType : { value: this.data.item.data },
      props: {
        host: this.formData.host,
        databaseName: this.formData.databaseName,
        username: this.formData.username,
        password: this.formData.password,
      },
      remark: this.formData.remark,
      additionalPropsStr: this.formData.additionalPropsStr,
    };
    if (valid) {
      if (this.data.item?.item?.id) {
        this.datasourceService.update(ds).subscribe((d) => {
          if (d.success) {
            this.data.item.onClose();
            this.data.item.refresh();
          }
        });
      } else {
        this.datasourceService.add(ds).subscribe((d) => {
          if (d.success) {
            this.data.item.onClose();
            this.data.item.refresh();
          }
        });
      }
    }
  }

  passwordChange(event: any, isPasswdField: boolean) {
    if (!isPasswdField) {
      this.formData.password = null;
    }
    this.isPasswordChanged = true;
  }

  testConnection() {
    let ds: MetaDataSource = {
      id: this.formData.id,
      datasourceName: this.formData.datasourceName,
      datasourceType: this.data.item?.item?.id ? this.data.item?.item?.datasourceType : { value: this.data.item.data },
      props: {
        host: this.formData.host,
        databaseName: this.formData.databaseName,
        username: this.formData.username,
        password: this.formData.password,
      },
      remark: this.formData.remark,
      additionalPropsStr: this.formData.additionalPropsStr,
      passwdChanged: this.data.item?.item?.id ? this.isPasswordChanged : true,
    };
    if (this.formDir.isReady) {
      this.datasourceService.testConnection(ds).subscribe((d) => {
        if (d.success) {
          this.notificationService.success(this.translate.instant('datadev.testConnect.success'));
        }
      });
    }
  }
}
