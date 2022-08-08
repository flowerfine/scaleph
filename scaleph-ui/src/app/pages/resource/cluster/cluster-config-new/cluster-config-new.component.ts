import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout} from 'ng-devui';
import {FlinkDeployConfig} from "../../../../@core/data/flink.data";
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";
import {Dict, DICT_TYPE} from "../../../../@core/data/app.data";
import {SysDictDataService} from "../../../../@core/services/admin/dict-data.service";

@Component({
  selector: 'app-cluster-config-new',
  templateUrl: './cluster-config-new.component.html',
  styleUrls: ['../cluster-config.component.scss'],
})
export class ClusterConfigNewComponent implements OnInit {
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

  resourceClusterTypeList: Dict[] = []

  formData = {
    configType: null,
    name: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private dictDataService: SysDictDataService, private deployConfigService: DeployConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.resourceClusterType).subscribe((d) => {
      this.resourceClusterTypeList = d;
    });
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
