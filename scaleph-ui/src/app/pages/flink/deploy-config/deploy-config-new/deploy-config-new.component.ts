import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout, IFileOptions, IUploadOptions} from 'ng-devui';
import {FlinkDeployConfig, FlinkDeployConfigUploadParam} from "../../../../@core/data/flink.data";
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";

@Component({
  selector: 'app-project-new',
  templateUrl: './deploy-config-new.component.html',
  styleUrls: ['../deploy-config.component.scss'],
})
export class DeployConfigNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    configTypeRules: {
      validators: [
        {required: true},
        {maxlength: 30}
      ],
    },
    nameRules: {
      validators: [{required: true}],
    },
    remarkRules: {
      validators: [{maxlength: 200}],
    },
  };

  formData = {
    configType: null,
    name: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private deployConfigService: DeployConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({valid}) {
    let row: FlinkDeployConfig = {
      configType: this.formData.configType,
      name: this.formData.name,
      remark: this.formData.remark,
    };
    if (valid) {
      this.deployConfigService.add(row).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  close(event) {
    this.data.onClose(event);
  }
}
