import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {FormLayout, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterConfigService} from "../../../@core/services/flink/cluster-config.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {FlinkClusterConfig} from "../../../@core/data/flink.data";
import {ClusterCredentialService} from "../../../@core/services/resource/cluster-credential.service";
import {FlinkReleaseService} from "../../../@core/services/resource/flink-release.service";
import {
  ClusterCredential,
  ClusterCredentialParam,
  FlinkRelease,
  FlinkReleaseParam
} from "../../../@core/data/resource.data";

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

  isNone: boolean = false;
  isFixedDelay: boolean = false;
  isFailureRate: boolean = false;
  isExponentialDelay: boolean = false;

  isZookeeperHA: boolean = false;
  isKubernetesHA: boolean = false;

  formData = {
    name: null,
    flinkRelease: null,
    clusterCredential: null,
    deployMode: null,
    configOptions: {},
    remark: null
  }

  flinkReleaseList: FlinkRelease[] = []
  flinkReleaseResult: PageResponse<FlinkRelease> = null

  clusterCredentialList: ClusterCredential[] = []
  clusterCredentialResult: PageResponse<ClusterCredential> = null

  flinkStateBackendList: Dict[] = []
  deployModeList: Dict[] = []
  flinkSemanticList: Dict[] = []
  flinkCheckpointRetainList: Dict[] = []
  flinkRestartStrategyList: Dict[] = []
  flinkHAList: Dict[] = []

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private releaseFlinkService: FlinkReleaseService,
    private clusterCredentialService: ClusterCredentialService,
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
    this.dictDataService.listByType(DICT_TYPE.flinkHA).subscribe((d) => {
      this.flinkHAList = d;
    });

    let releaseFlinkParam: FlinkReleaseParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.releaseFlinkService.list(releaseFlinkParam).subscribe((d) => {
      this.flinkReleaseResult = d
      this.flinkReleaseList = d.records;
    });

    let clusterCredentialParam: ClusterCredentialParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.clusterCredentialService.list(clusterCredentialParam).subscribe((d) => {
      this.clusterCredentialResult = d
      this.clusterCredentialList = d.records;
    });
  }

  onRestartStrategyValueChange(event) {
    this.isNone = event.label == 'none'
    this.isFixedDelay = event.label == 'fixed-delay'
    this.isFailureRate = event.label == 'failure-rate'
    this.isExponentialDelay = event.label == 'exponential-delay'
  }

  onHAValueChange(event) {
    this.isZookeeperHA = event.label == 'ZooKeeper'
    this.isKubernetesHA = event.label == 'Kubernetes'
  }

  onFlinkReleaseLoadMore(event) {
    let loaded = this.flinkReleaseResult.current * this.flinkReleaseResult.size
    if (loaded >= this.flinkReleaseResult.total) {
      event.instance.loadFinish();
    } else {
      let releaseFlinkParam: FlinkReleaseParam = {
        pageSize: this.flinkReleaseResult.size,
        current: this.flinkReleaseResult.current + 1
      }
      this.releaseFlinkService.list(releaseFlinkParam).subscribe((d) => {
        this.flinkReleaseResult = d
        this.flinkReleaseList = [...this.flinkReleaseList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }

  onClusterCredentialLoadMore(event) {
    let loaded = this.clusterCredentialResult.current * this.clusterCredentialResult.size
    if (loaded >= this.clusterCredentialResult.total) {
      event.instance.loadFinish();
    } else {
      let clusterCredentialParam: ClusterCredentialParam = {
        pageSize: this.clusterCredentialResult.size,
        current: this.clusterCredentialResult.current + 1
      }
      this.clusterCredentialService.list(clusterCredentialParam).subscribe((d) => {
        this.clusterCredentialResult = d
        this.clusterCredentialList = [...this.clusterCredentialList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }



  submitForm() {
    let isDict = (vlaue: any): vlaue is Dict =>
      typeof (vlaue as Dict)['label'] == 'string'

    let customConfigOptions: { [key: string]: any } = {};
    for (let key in this.formData.configOptions) {
      let value = this.formData.configOptions[key]
      if (isDict(value)) {
        customConfigOptions[key] = value['value']
      } else {
        customConfigOptions[key] = value
      }
    }

    let row: FlinkClusterConfig = {
      name: this.formData.name,
      flinkVersion: this.formData.flinkRelease.version,
      resourceProvider: this.formData.clusterCredential.configType,
      deployMode: this.formData.deployMode,
      flinkRelease: this.formData.flinkRelease,
      clusterCredential: this.formData.clusterCredential,
      configOptions: customConfigOptions,
      remark: this.formData.remark
    };

    this.clusterConfigService.add(row).subscribe((d) => {
      if (d.success) {

      }
    });
  }

}
