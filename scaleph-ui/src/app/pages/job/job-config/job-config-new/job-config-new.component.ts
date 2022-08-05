import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout} from 'ng-devui';
import {FlinkClusterConfig, FlinkClusterConfigParam} from "../../../../@core/data/flink.data";
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse} from "../../../../@core/data/app.data";
import {SysDictDataService} from "../../../../@core/services/admin/dict-data.service";
import {ClusterConfigService} from "../../../../@core/services/flink/cluster-config.service";
import {FlinkJobConfig} from "../../../../@core/data/job.data";
import {JobConfigService} from "../../../../@core/services/job/job-config.service";
import {ReleaseFlinkService} from "../../../../@core/services/resource/release-flink.service";

@Component({
  selector: 'app-job-config-new',
  templateUrl: './job-config-new.component.html',
  styleUrls: ['../job-config.component.scss'],
})
export class JobConfigNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    typeRules: {
      validators: [{required: true}],
    },
    nameRules: {
      validators: [{required: true}],
    },
    remarkRules: {
      validators: [{maxlength: 200}],
    },
  };

  flinkJobTypeList: Dict[] = []

  flinkClusterConfigList: FlinkClusterConfig[] = []
  flinkClusterConfigResult: PageResponse<FlinkClusterConfig> = null

  formData = {
    type: null,
    name: null,
    flinkClusterConfig: null,
    jobConfig: null,
    flinkConfig: null,
    remark: null,
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private dictDataService: SysDictDataService,
    private releaseFlinkService: ReleaseFlinkService,
    private deployConfigService: DeployConfigService,
    private clusterConfigService: ClusterConfigService,
    private jobConfigService: JobConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.flinkJobType).subscribe((d) => {
      this.flinkJobTypeList = d;
    });

    let flinkClusterConfigParam: FlinkClusterConfigParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.clusterConfigService.list(flinkClusterConfigParam).subscribe((d) => {
      this.flinkClusterConfigResult = d
      this.flinkClusterConfigList = d.records;
    });
  }

  onFlinkClusterConfigLoadMore(event) {
    let loaded = this.flinkClusterConfigResult.current * this.flinkClusterConfigResult.size
    if (loaded >= this.flinkClusterConfigResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkClusterConfigParam: FlinkClusterConfigParam = {
        pageSize: this.flinkClusterConfigResult.size,
        current: this.flinkClusterConfigResult.current + 1
      }
      this.clusterConfigService.list(flinkClusterConfigParam).subscribe((d) => {
        this.flinkClusterConfigResult = d
        this.flinkClusterConfigList = [...this.flinkClusterConfigList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }

  submitForm({valid}) {
    let row: FlinkJobConfig = {
      type: this.formData.type,
      name: this.formData.name,
      flinkClusterConfigId: this.formData.flinkClusterConfig.id,
      jobConfig: null,
      flinkConfig: null,
      remark: this.formData.remark,
    };
    if (valid) {
      this.jobConfigService.add(row).subscribe((d) => {
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
