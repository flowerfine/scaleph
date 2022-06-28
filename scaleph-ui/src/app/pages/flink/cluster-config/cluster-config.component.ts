import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {FlinkClusterConfig, FlinkClusterConfigParam} from 'src/app/@core/data/flink.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterConfigService} from "../../../@core/services/flink/cluster-config.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {ClusterConfigNewComponent} from "./cluster-config-new/cluster-config-new.component";
import {ClusterConfigDeleteComponent} from "./cluster-config-delete/cluster-config-delete.component";

@Component({
  selector: 'app-cluster-config',
  templateUrl: './cluster-config.component.html',
  styleUrls: ['./cluster-config.component.scss'],
})
export class ClusterConfigComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: FlinkClusterConfig[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  searchFormConfig = {
    name: '',
    flinkVersion: null,
    resourceProvider: null,
    deployMode: null
  };

  flinkVersionList: Dict[] = []
  resourceProviderList: Dict[] = []
  deployModeList: Dict[] = []

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private clusterConfigService: ClusterConfigService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.flinkVersion).subscribe((d) => {
      this.flinkVersionList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkResourceProvider).subscribe((d) => {
      this.resourceProviderList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.flinkDeploymentMode).subscribe((d) => {
      this.deployModeList = d;
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: FlinkClusterConfigParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      name: this.searchFormConfig.name || '',
      flinkVersion: this.searchFormConfig.flinkVersion ? this.searchFormConfig.flinkVersion.value : '',
      resourceProvider: this.searchFormConfig.resourceProvider ? this.searchFormConfig.resourceProvider.value : '',
      deployMode: this.searchFormConfig.deployMode ? this.searchFormConfig.deployMode.value : ''
    };

    this.clusterConfigService.list(param).subscribe((d) => {
      this.pager.total = d.total;
      this.dataTableDs = d.records;
      console.log(d)
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
      flinkVersion: null,
      resourceProvider: null,
      deployMode: null
    };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddClusterConfigDialog() {
    const results = this.modalService.open({
      id: 'cluster-config-add',
      width: '580px',
      backdropCloseable: true,
      component: ClusterConfigNewComponent,
      data: {
        title: {name: this.translate.instant('flink.cluster-config.name_')},
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteClusterConfigDialog(items: FlinkClusterConfig[]) {
    const results = this.modalService.open({
      id: 'cluster-config-delete',
      width: '346px',
      backdropCloseable: true,
      component: ClusterConfigDeleteComponent,
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
