import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DFormGroupRuleDirective, DValidateRules, FormLayout} from 'ng-devui';
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";
import {FlinkDeployConfig} from "../../../../@core/data/flink.data";

@Component({
  selector: 'app-deploy-config-update',
  templateUrl: './deploy-config-update.component.html',
  styleUrls: ['../deploy-config.component.scss'],
})
export class DeployConfigUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    configTypeRules: {
      validators: [
        {required: true},
        {maxlength: 30},
        {pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord')},
      ],
    },
    nameRules: {
      validators: [{required: true}, {maxlength: 60}],
    },
    remarkRules: {
      validators: [{maxlength: 200}],
    },
  };

  formData = {
    id: null,
    configType: null,
    name: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private deployConfigService: DeployConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      configType: this.data.item.configType,
      name: this.data.item.name,
      remark: this.data.item.remark,
    };
  }

  submitForm({valid}) {
    let ds: FlinkDeployConfig = {
      id: this.formData.id,
      configType: this.formData.configType,
      name: this.formData.name,
      remark: this.formData.remark,
    };
    if (valid) {
      this.deployConfigService.update(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
