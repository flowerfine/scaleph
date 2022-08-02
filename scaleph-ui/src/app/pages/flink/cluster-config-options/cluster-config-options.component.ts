import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {FormLayout, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterConfigService} from "../../../@core/services/flink/cluster-config.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {
  FlinkClusterConfig,
  FlinkDeployConfig,
  FlinkDeployConfigParam,
  FlinkRelease, FlinkReleaseParam
} from "../../../@core/data/flink.data";
import {ReleaseService} from "../../../@core/services/flink/release.service";
import {DeployConfigService} from "../../../@core/services/flink/deploy-config.service";

@Component({
  selector: 'app-cluster-config-options',
  templateUrl: './cluster-config-options.component.html',
  styleUrls: ['./cluster-config-options.component.scss'],
})
export class ClusterConfigOptionsComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;

  layoutDirection: FormLayout = FormLayout.Horizontal;

  isBasicCollapsed = true;
  isStateCollapsed = true;
  isFaultCollapsed = true;
  isHACollapsed = true;
  isMemCollapsed = true;
  isLogCollapsed = true;
  isAdditionalCollapsed = true;

  formData = {
    name: null,
    flinkRelease: null,
    flinkDeployConfig: null,
    deployMode: null,
    configOptions: {}
  }

  flinkReleaseList: FlinkRelease[] = []
  flinkReleaseResult: PageResponse<FlinkRelease> = null

  flinkDeployConfigList: FlinkDeployConfig[] = []
  flinkDeployConfigResult: PageResponse<FlinkDeployConfig> = null

  flinkStateBackendList: Dict[] = []
  deployModeList: Dict[] = []
  flinkSemanticList: Dict[] = []
  flinkCheckpointRetainList: Dict[] = []
  flinkRestartStrategyList: Dict[] = []

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private releaseService: ReleaseService,
    private deployConfigService: DeployConfigService,
    private clusterConfigService: ClusterConfigService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.dictDataService.listByType(DICT_TYPE.flinkStateBackend).subscribe((d) => {
      this.flinkStateBackendList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkDeploymentMode).subscribe((d) => {
      this.deployModeList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkSemantic).subscribe((d) => {
      this.flinkSemanticList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkCheckpointRetain).subscribe((d) => {
      this.flinkCheckpointRetainList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkRestartStrategy).subscribe((d) => {
      this.flinkRestartStrategyList = d;
    });

    let flinkReleaseParam: FlinkReleaseParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.releaseService.list(flinkReleaseParam).subscribe((d) => {
      this.flinkReleaseResult = d
      this.flinkReleaseList = d.records;
    });

    let flinkDeployConfigParam: FlinkDeployConfigParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.deployConfigService.list(flinkDeployConfigParam).subscribe((d) => {
      this.flinkDeployConfigResult = d
      this.flinkDeployConfigList = d.records;
    });
  }

  onValueChange(event) {
    console.log(this.formData)
  }

  onFlinkReleaseLoadMore(event) {
    let loaded = this.flinkReleaseResult.current * this.flinkReleaseResult.size
    if (loaded >= this.flinkReleaseResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkReleaseParam: FlinkReleaseParam = {
        pageSize: this.flinkReleaseResult.size,
        current: this.flinkReleaseResult.current + 1
      }
      this.releaseService.list(flinkReleaseParam).subscribe((d) => {
        this.flinkReleaseResult = d
        this.flinkReleaseList = [...this.flinkReleaseList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }

  onFlinkDeployConfigLoadMore(event) {
    let loaded = this.flinkDeployConfigResult.current * this.flinkDeployConfigResult.size
    if (loaded >= this.flinkDeployConfigResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkDeployConfigParam: FlinkDeployConfigParam = {
        pageSize: this.flinkDeployConfigResult.size,
        current: this.flinkDeployConfigResult.current + 1
      }
      this.deployConfigService.list(flinkDeployConfigParam).subscribe((d) => {
        this.flinkDeployConfigResult = d
        this.flinkDeployConfigList = [...this.flinkDeployConfigList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }


}
