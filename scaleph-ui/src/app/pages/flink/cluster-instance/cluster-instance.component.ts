import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {
  FlinkClusterConfig,
  FlinkClusterConfigParam,
  FlinkClusterInstance,
  FlinkClusterInstanceParam, FlinkDeployConfig, FlinkDeployConfigParam
} from 'src/app/@core/data/flink.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterConfigService} from "../../../@core/services/flink/cluster-config.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {ClusterConfigNewComponent} from "../cluster-config/cluster-config-new/cluster-config-new.component";
import {ClusterConfigUpdateComponent} from "../cluster-config/cluster-config-update/cluster-config-update.component";
import {ClusterConfigDeleteComponent} from "../cluster-config/cluster-config-delete/cluster-config-delete.component";
import {ClusterInstanceService} from "../../../@core/services/flink/cluster-instance.service";

@Component({
  selector: 'app-cluster-instance',
  templateUrl: './cluster-instance.component.html',
  styleUrls: ['./cluster-instance.component.scss'],
})
export class ClusterInstanceComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: FlinkClusterInstance[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  searchFormConfig = {
    name: '',
    flinkClusterConfig: null,
    status: null
  };

  statusList: Dict[] = []
  flinkClusterConfigList: FlinkClusterConfig[] = []
  flinkClusterConfigResult: PageResponse<FlinkClusterConfig> = null

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private clusterConfigService: ClusterConfigService,
    private clusterInstanceService: ClusterInstanceService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.flinkClusterStatus).subscribe((d) => {
      this.statusList = d;
    });
    let flinkClusterConfigParam: FlinkClusterConfigParam = {
      pageSize:  DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.clusterConfigService.list(flinkClusterConfigParam).subscribe((d) => {
      this.flinkClusterConfigResult = d
      this.flinkClusterConfigList = d.records;
    });

  }

  refreshTable() {
    this.openDataTableLoading();
    let param: FlinkClusterInstanceParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      name: this.searchFormConfig.name || '',
      flinkClusterConfigId: this.searchFormConfig.flinkClusterConfig ? this.searchFormConfig.flinkClusterConfig.id : '',
      status: this.searchFormConfig.status ? this.searchFormConfig.status.value : '',
    };

    this.clusterInstanceService.list(param).subscribe((d) => {
      this.pager.total = d.total;
      this.dataTableDs = d.records;
      this.loadTarget.loadingInstance.close();
      this.dataLoading = false;
      this.dataTable.setTableCheckStatus({pageAllChecked: false});
      this.getDataTableCheckedStatus();
    });
  }

  openDataTableLoading() {
    const dc = this.doc.querySelector('#dataTableContent');
    this.loadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.dataLoading = true;
  }

  getDataTableCheckedStatus() {
    if (this.dataTable.getCheckedRows().length > 0) {
      this.dataTableChecked = true;
    } else {
      this.dataTableChecked = false;
    }
  }

  reset() {
    this.searchFormConfig = {
      name: '',
      flinkClusterConfig: null,
      status: null,
    };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  onFlinkDeployConfigLoadMore(event) {
    let loaded = this.flinkClusterConfigResult.current * this.flinkClusterConfigResult.size
    if (loaded >= this.flinkClusterConfigResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkClusterConfigParam: FlinkClusterConfigParam = {
        pageSize:  this.flinkClusterConfigResult.size,
        current: this.flinkClusterConfigResult.current + 1
      }
      this.clusterConfigService.list(flinkClusterConfigParam).subscribe((d) => {
        this.flinkClusterConfigResult = d
        this.flinkClusterConfigList = [...this.flinkClusterConfigList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }
}
