import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout} from 'ng-devui';
import {
  FlinkClusterConfig,
  FlinkDeployConfig,
  FlinkDeployConfigParam,
  FlinkRelease,
  FlinkReleaseParam, KeyValueConfig
} from "../../../../@core/data/flink.data";
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse} from "../../../../@core/data/app.data";
import {SysDictDataService} from "../../../../@core/services/admin/dict-data.service";
import {ReleaseService} from "../../../../@core/services/flink/release.service";
import {ClusterConfigService} from "../../../../@core/services/flink/cluster-config.service";
import {DataTableComponent, DFormGroupRuleDirective} from "@devui";

@Component({
  selector: 'app-cluster-config-update',
  templateUrl: './cluster-config-update.component.html',
  styleUrls: ['../cluster-config.component.scss'],
})
export class ClusterConfigUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    flinkVersionRules: {
      validators: [{required: true}],
    },
    resourceProviderRules: {
      validators: [{required: true}],
    },
    deployModeRules: {
      validators: [{required: true}],
    },
    flinkReleaseRules: {
      validators: [{required: true}],
    },
    flinkDeployConfigRules: {
      validators: [{required: true}],
    },
    nameRules: {
      validators: [{required: true}],
    },
    remarkRules: {
      validators: [{maxlength: 200}],
    },
  };

  flinkVersionList: Dict[] = []
  resourceProviderList: Dict[] = []
  deployModeList: Dict[] = []
  flinkReleaseList: FlinkRelease[] = []
  flinkDeployConfigList: FlinkDeployConfig[] = []

  flinkDeployConfigResult: PageResponse<FlinkDeployConfig> = null

  formData = {
    id: null,
    name: null,
    flinkVersion: null,
    resourceProvider: null,
    deployMode: null,
    flinkRelease: null,
    flinkDeployConfig: null,
    remark: null,
  };

  headerNewForm: boolean = false;
  customConfigdataTableDs: Array<KeyValueConfig> = []
  defaultRowData = {
    key: null,
    value: null
  }

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private dictDataService: SysDictDataService,
    private releaseService: ReleaseService,
    private deployConfigService: DeployConfigService,
    private clusterConfigService: ClusterConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      name: this.data.item.name,
      flinkVersion: this.data.item.flinkVersion,
      resourceProvider: this.data.item.resourceProvider,
      deployMode: this.data.item.deployMode,
      flinkRelease: null,
      flinkDeployConfig: null,
      remark: this.data.item.remark,
    };
    for (let name in this.data.item.configOptions) {
      let keyValueConfig = {
        key: name,
        value: this.data.item.configOptions[name]
      }
      this.customConfigdataTableDs.push(keyValueConfig)
    }
    this.releaseService.selectOne(this.data.item.flinkReleaseId).subscribe((d) => {
      this.formData.flinkRelease = d;
    });
    this.deployConfigService.selectOne(this.data.item.deployConfigFileId).subscribe((d) => {
      this.formData.flinkDeployConfig = d;
    });

    this.dictDataService.listByType(DICT_TYPE.flinkVersion).subscribe((d) => {
      this.flinkVersionList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkResourceProvider).subscribe((d) => {
      this.resourceProviderList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkDeploymentMode).subscribe((d) => {
      this.deployModeList = d;
    });
    this.releaseService.list(this.data.item.flinkVersion?.value||'').subscribe((d) => {
      this.flinkReleaseList = d.records;
    });

    let flinkDeployConfigParam: FlinkDeployConfigParam = {
      pageSize:  DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.deployConfigService.list(flinkDeployConfigParam).subscribe((d) => {
      this.flinkDeployConfigResult = d
      this.flinkDeployConfigList = d.records;
    });
  }

  onFlinkVersionValueChange(flinkVersion) {
    this.formData.flinkRelease = null
    let param: FlinkReleaseParam = {
      version: flinkVersion.value,
    };

    this.releaseService.list(param).subscribe((d) => {
      this.flinkReleaseList = d.records;
    });
  }

  onFlinkDeployConfigLoadMore(event) {
    let loaded = this.flinkDeployConfigResult.current * this.flinkDeployConfigResult.size
    if (loaded >= this.flinkDeployConfigResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkDeployConfigParam: FlinkDeployConfigParam = {
        pageSize:  this.flinkDeployConfigResult.size,
        current: this.flinkDeployConfigResult.current + 1
      }
      this.deployConfigService.list(flinkDeployConfigParam).subscribe((d) => {
        this.flinkDeployConfigResult = d
        this.flinkDeployConfigList = [...this.flinkDeployConfigList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }

  newRow() {
    this.headerNewForm = true;
  }

  quickRowAdded() {
    const newData = {...this.defaultRowData};
    this.customConfigdataTableDs.unshift(newData);
    this.headerNewForm = false;
    this.defaultRowData = {
      key: null,
      value: null
    }
  }

  quickRowCancel() {
    this.headerNewForm = false;
  }

  deleteRow(rowIndex) {
    this.customConfigdataTableDs.splice(rowIndex, 1);
  }

  submitForm({valid}) {
    let customConfigOptions: {[key:string]: any} = {};
    this.customConfigdataTableDs.forEach((config) => {
      customConfigOptions[config.key] = config.value
    })

    let row: FlinkClusterConfig = {
      id: this.formData.id,
      name: this.formData.name,
      flinkVersion: this.formData.flinkVersion,
      resourceProvider: this.formData.resourceProvider,
      deployMode: this.formData.deployMode,
      flinkReleaseId: this.formData.flinkRelease.id,
      deployConfigFileId: this.formData.flinkDeployConfig.id,
      configOptions: customConfigOptions,
      remark: this.formData.remark
    };
    if (valid) {
      this.clusterConfigService.update(row).subscribe((d) => {
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
