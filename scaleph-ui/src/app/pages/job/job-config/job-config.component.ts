import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PageResponse, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {FlinkClusterConfig, FlinkClusterConfigParam} from 'src/app/@core/data/flink.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterConfigService} from "../../../@core/services/flink/cluster-config.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {FlinkJobConfig, FlinkJobConfigParam} from "../../../@core/data/job.data";
import {JobConfigService} from "../../../@core/services/job/job-config.service";
import {ClusterConfigNewComponent} from "../../flink/cluster-config/cluster-config-new/cluster-config-new.component";
import {JobConfigNewComponent} from "./job-config-new/job-config-new.component";
import {
  ClusterConfigDeleteComponent
} from "../../flink/cluster-config/cluster-config-delete/cluster-config-delete.component";
import {JobConfigDeleteComponent} from "./job-config-delete/job-config-delete.component";

@Component({
  selector: 'app-job-config',
  templateUrl: './job-config.component.html',
  styleUrls: ['./job-config.component.scss'],
})
export class JobConfigComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: FlinkJobConfig[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  searchFormConfig = {
    type: null,
    name: '',
    flinkClusterConfig: null
  };

  typeList: Dict[] = []
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
    private jobConfigService: JobConfigService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.flinkJobType).subscribe((d) => {
      this.typeList = d;
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

  refreshTable() {
    this.openDataTableLoading();
    let param: FlinkJobConfigParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      type: this.searchFormConfig.type ? this.searchFormConfig.type.value : '',
      name: this.searchFormConfig.name || '',
      flinkClusterConfigId: this.searchFormConfig.flinkClusterConfig ? this.searchFormConfig.flinkClusterConfig.id : ''
    };

    this.jobConfigService.list(param).subscribe((d) => {
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
      type: null,
      name: '',
      flinkClusterConfig: null
    };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
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

  openAddJobConfigDialog() {
    const results = this.modalService.open({
      id: 'job-config-add',
      width: '580px',
      backdropCloseable: true,
      component: JobConfigNewComponent,
      data: {
        title: {name: this.translate.instant('job.job-config.name_')},
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openEditJobConfigDialog(item: FlinkJobConfig) {

  }

  openDeleteJobConfigDialog(items: FlinkJobConfig[]) {
    const results = this.modalService.open({
      id: 'job-config-delete',
      width: '346px',
      backdropCloseable: true,
      component: JobConfigDeleteComponent,
      data: {
        title: this.translate.instant('app.common.operate.delete.confirm.title'),
        items: items,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }
}
