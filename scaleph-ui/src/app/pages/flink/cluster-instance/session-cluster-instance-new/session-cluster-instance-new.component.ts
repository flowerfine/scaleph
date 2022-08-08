import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout} from 'ng-devui';
import {
  FlinkClusterConfig,
  FlinkClusterConfigParam,
  FlinkSessionClusterAddParam
} from "../../../../@core/data/flink.data";
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse} from "../../../../@core/data/app.data";
import {SysDictDataService} from "../../../../@core/services/admin/dict-data.service";
import {ClusterConfigService} from "../../../../@core/services/flink/cluster-config.service";
import {ClusterInstanceService} from "../../../../@core/services/flink/cluster-instance.service";

@Component({
  selector: 'app-cluster-config-new',
  templateUrl: './session-cluster-instance-new.component.html',
  styleUrls: ['../cluster-instance.component.scss'],
})
export class SessionClusterInstanceNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    flinkClusterConfigRules: {
      validators: [{required: true}],
    },
    remarkRules: {
      validators: [{maxlength: 200}],
    },
  };


  deployModeList: Dict[] = []
  sessionDeployModel = this.deployModeList[2]
  flinkClusterConfigList: FlinkClusterConfig[] = []
  flinkClusterConfigResult: PageResponse<FlinkClusterConfig> = null

  formData = {
    flinkClusterConfig: null,
    deployMode: null,
    remark: null,
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private dictDataService: SysDictDataService,
    private clusterConfigService: ClusterConfigService,
    private clusterInstanceService: ClusterInstanceService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.flinkDeploymentMode).subscribe((d) => {
      this.deployModeList = d;
      this.sessionDeployModel = this.deployModeList[2]
      let flinkClusterConfigParam: FlinkClusterConfigParam = {
        pageSize: DEFAULT_PAGE_PARAM.pageSize,
        current: DEFAULT_PAGE_PARAM.pageIndex,
        deployMode: this.sessionDeployModel ? this.sessionDeployModel.value : ''
      }
      this.clusterConfigService.list(flinkClusterConfigParam).subscribe((d) => {
        this.flinkClusterConfigResult = d
        this.flinkClusterConfigList = d.records;
      });
    });
  }

  onFlinkClusterConfigLoadMore(event) {
    let loaded = this.flinkClusterConfigResult.current * this.flinkClusterConfigResult.size
    if (loaded >= this.flinkClusterConfigResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkClusterConfigParam: FlinkClusterConfigParam = {
        pageSize: this.flinkClusterConfigResult.size,
        current: this.flinkClusterConfigResult.current + 1,
        deployMode: this.sessionDeployModel ? this.sessionDeployModel.value : ''
      }
      this.clusterConfigService.list(flinkClusterConfigParam).subscribe((d) => {
        this.flinkClusterConfigResult = d
        this.flinkClusterConfigList = [...this.flinkClusterConfigList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }

  submitForm({valid}) {
    let row: FlinkSessionClusterAddParam = {
      flinkClusterConfigId: this.formData.flinkClusterConfig.id,
      remark: this.formData.remark
    };
    if (valid) {
      this.clusterInstanceService.add(row).subscribe((d) => {
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
