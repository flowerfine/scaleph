import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterCredentialService} from "../../../@core/services/resource/cluster-credential.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {ClusterCredential, ClusterCredentialParam} from "../../../@core/data/resource.data";
import {ClusterCredentialNewComponent} from "./cluster-credential-new/cluster-credential-new.component";
import {ClusterCredentialUpdateComponent} from "./cluster-credential-update/cluster-credential-update.component";
import {ClusterCredentialDeleteComponent} from "./cluster-credential-delete/cluster-credential-delete.component";

@Component({
  selector: 'app-cluster-credential',
  templateUrl: './cluster-credential.component.html',
  styleUrls: ['./cluster-credential.component.scss'],
})
export class ClusterCredentialComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: ClusterCredential[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = {configType: null, name: ''};

  resourceClusterTypeList: Dict[] = []

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private clusterCredentialService: ClusterCredentialService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.resourceClusterType).subscribe((d) => {
      this.resourceClusterTypeList = d;
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: ClusterCredentialParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      configType: this.searchFormConfig.configType ? this.searchFormConfig.configType.value : '',
      name: this.searchFormConfig.name,
    };

    this.clusterCredentialService.list(param).subscribe((d) => {
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
    this.searchFormConfig = {configType: '', name: ''};
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddClusterCredentialDialog() {
    const results = this.modalService.open({
      id: 'cluster-credential-add',
      width: '580px',
      backdropCloseable: true,
      component: ClusterCredentialNewComponent,
      data: {
        title: {name: this.translate.instant('resource.cluster-credential.name_')},
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openClusterCredential(row: ClusterCredential) {
    this.router.navigate(['/scaleph', 'resource', 'cluster-credential-file'], {
      queryParams: {
        id: row.id
      }
    });
  }

  openEditClusterCredentialDialog(item: ClusterCredential) {
    const results = this.modalService.open({
      id: 'cluster-credential-edit',
      width: '580px',
      backdropCloseable: true,
      component: ClusterCredentialUpdateComponent,
      data: {
        title: {name: this.translate.instant('resource.cluster-credential.name_')},
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

  openDeleteClusterCredentialDialog(items: ClusterCredential[]) {
    const results = this.modalService.open({
      id: 'cluster-credential-delete',
      width: '346px',
      backdropCloseable: true,
      component: ClusterCredentialDeleteComponent,
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
