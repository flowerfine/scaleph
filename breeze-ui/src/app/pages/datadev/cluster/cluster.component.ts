import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { DiClusterConfig, DiClusterConfigParam } from 'src/app/@core/data/datadev.data';
import { DictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { AuthService } from 'src/app/@core/services/auth.service';
import { ClusterService } from 'src/app/@core/services/datadev/cluster.service';
import { ClusterDeleteComponent } from './cluster-delete/cluster-delete.component';
import { ClusterNewComponent } from './cluster-new/cluster-new.component';
import { ClusterUpdateComponent } from './cluster-update/cluster-update.component';

@Component({
  selector: 'app-cluster',
  templateUrl: './cluster.component.html',
  styleUrls: ['./cluster.component.scss'],
})
export class ClusterComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: DiClusterConfig[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = { clusterName: '', clusterType: null };
  clusterTypeList: Dict[] = [];
  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: DictDataService,
    private clusterService: ClusterService
  ) {}

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.clusterType).subscribe((d) => {
      this.clusterTypeList = d;
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: DiClusterConfigParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      clusterName: this.searchFormConfig.clusterName,
      clusterType: this.searchFormConfig.clusterType ? this.searchFormConfig.clusterType.value : '',
    };

    this.clusterService.listByPage(param).subscribe((d) => {
      this.pager.total = d.total;
      this.dataTableDs = d.records;
      this.loadTarget.loadingInstance.close();
      this.dataLoading = false;
      this.dataTable.setTableCheckStatus({ pageAllChecked: false });
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
    this.searchFormConfig = { clusterName: '', clusterType: null };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddClusterDialog() {
    const results = this.modalService.open({
      id: 'cluster-new',
      width: '580px',
      backdropCloseable: true,
      component: ClusterNewComponent,
      data: {
        title: { name: this.translate.instant('datadev.cluster') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteClusterDialog(items: DiClusterConfig[]) {
    const results = this.modalService.open({
      id: 'cluster-delete',
      width: '346px',
      backdropCloseable: true,
      component: ClusterDeleteComponent,
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

  openEditClusterDialog(item: DiClusterConfig) {
    const results = this.modalService.open({
      id: 'cluster-edit',
      width: '580px',
      backdropCloseable: true,
      component: ClusterUpdateComponent,
      data: {
        title: { name: this.translate.instant('datadev.cluster') },
        item: item,
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
