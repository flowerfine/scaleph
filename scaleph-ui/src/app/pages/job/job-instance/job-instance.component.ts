import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {FlinkClusterInstance, FlinkClusterInstanceParam} from 'src/app/@core/data/flink.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {
  FlinkJobConfig,
  FlinkJobConfigParam,
  FlinkJobInstance,
  FlinkJobInstanceParam
} from "../../../@core/data/job.data";
import {JobConfigService} from "../../../@core/services/job/job-config.service";
import {JobInstanceService} from "../../../@core/services/job/job-instance.service";
import {ClusterInstanceService} from "../../../@core/services/flink/cluster-instance.service";

@Component({
  selector: 'app-job-instance',
  templateUrl: './job-instance.component.html',
  styleUrls: ['./job-instance.component.scss'],
})
export class JobInstanceComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: FlinkJobInstance[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  searchFormConfig = {
    flinkJobConfig: null,
    flinkClusterInstance: null,
    status: null
  };

  flinkJobStatusList: Dict[] = []

  flinkJobConfigList: FlinkJobConfig[] = []
  flinkJobConfigResult: PageResponse<FlinkJobConfig> = null

  flinkClusterInstanceList: FlinkClusterInstance[] = []
  flinkClusterInstanceResult: PageResponse<FlinkClusterInstance> = null

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private clusterInstanceService: ClusterInstanceService,
    private jobConfigService: JobConfigService,
    private jobInstanceService: JobInstanceService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.flinkJobStatus).subscribe((d) => {
      this.flinkJobStatusList = d;
    });
    let flinkJobConfigParam: FlinkJobConfigParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.jobConfigService.list(flinkJobConfigParam).subscribe((d) => {
      this.flinkJobConfigResult = d
      this.flinkJobConfigList = d.records;
    });

    let flinkClusterInstanceParam: FlinkClusterInstanceParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex
    }
    this.clusterInstanceService.list(flinkClusterInstanceParam).subscribe((d) => {
      this.flinkClusterInstanceResult = d
      this.flinkClusterInstanceList = d.records;
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: FlinkJobInstanceParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      flinkJobConfigId: this.searchFormConfig.flinkJobConfig ? this.searchFormConfig.flinkJobConfig.id : '',
      flinkClusterInstanceId: this.searchFormConfig.flinkClusterInstance ? this.searchFormConfig.flinkClusterInstance.id : '',
      status: this.searchFormConfig.status ? this.searchFormConfig.status.value : ''
    };

    this.jobInstanceService.list(param).subscribe((d) => {
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
      flinkJobConfig: null,
      flinkClusterInstance: null,
      status: null
    };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  onFlinkJobConfigLoadMore(event) {
    let loaded = this.flinkJobConfigResult.current * this.flinkJobConfigResult.size
    if (loaded >= this.flinkJobConfigResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkJobConfigParam: FlinkJobConfigParam = {
        pageSize: this.flinkJobConfigResult.size,
        current: this.flinkJobConfigResult.current + 1
      }
      this.jobConfigService.list(flinkJobConfigParam).subscribe((d) => {
        this.flinkJobConfigResult = d
        this.flinkJobConfigList = [...this.flinkJobConfigList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }

  onFlinkClusterInstanceLoadMore(event) {
    let loaded = this.flinkClusterInstanceResult.current * this.flinkClusterInstanceResult.size
    if (loaded >= this.flinkClusterInstanceResult.total) {
      event.instance.loadFinish();
    } else {
      let flinkClusterInstanceParam: FlinkClusterInstanceParam = {
        pageSize: this.flinkClusterInstanceResult.size,
        current: this.flinkClusterInstanceResult.current + 1
      }
      this.clusterInstanceService.list(flinkClusterInstanceParam).subscribe((d) => {
        this.flinkClusterInstanceResult = d
        this.flinkClusterInstanceList = [...this.flinkClusterInstanceList, ...d.records];
        event.instance.loadFinish();
      });
    }
  }

  openAddJobConfigDialog() {

  }

  openEditJobConfigDialog(item: FlinkJobConfig) {

  }

  openDeleteJobConfigDialog(items: FlinkJobConfig[]) {

  }
}
