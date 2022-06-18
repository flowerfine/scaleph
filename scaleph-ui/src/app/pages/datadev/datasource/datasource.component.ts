import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, DialogService, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {MetaDataSource, MetaDataSourceParam} from 'src/app/@core/data/datadev.data';
import {SysDictDataService} from 'src/app/@core/services/admin/dict-data.service';
import {AuthService} from 'src/app/@core/services/auth.service';
import {DataSourceService} from 'src/app/@core/services/datadev/datasource.service';
import {DatasourceDeleteComponent} from './datasource-delete/datasource-delete.component';
import {DatasourceNewPreComponent} from './datasource-new-pre/datasource-new-pre.component';
import {DatasourceUpdateComponent} from './datasource-update/datasource-update.component';

@Component({
  selector: 'app-datasource',
  templateUrl: './datasource.component.html',
  styleUrls: ['./datasource.component.scss'],
})
export class DatasourceComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading = false;
  dataTableChecked = false;
  loadTarget: any;
  dataTableDs: MetaDataSource[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = {dataSourceName: '', dataSourceType: null};
  dataSourceTypeList: Dict[] = [];

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dataSourceService: DataSourceService,
    private dictDataService: SysDictDataService,
    private dialogService: DialogService
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.datasourceType).subscribe((d) => {
      this.dataSourceTypeList = d;
    });
  }

  refreshTable(): void {
    this.openDataTableLoading();
    const param: MetaDataSourceParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      dataSourceName: this.searchFormConfig.dataSourceName,
      dataSourceType: this.searchFormConfig.dataSourceType ? this.searchFormConfig.dataSourceType.value : '',
    };
    this.dataSourceService.listByPage(param).subscribe((d) => {
      this.pager.total = d.total;
      this.dataTableDs = d.records;
      this.loadTarget.loadingInstance.close();
      this.dataLoading = false;
      this.dataTable.setTableCheckStatus({pageAllChecked: false});
      this.getDataTableCheckedStatus();
    });
  }

  openDataTableLoading(): void {
    const dc = this.doc.querySelector('#dataTableContent');
    this.loadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.dataLoading = true;
  }

  reset(): void {
    this.searchFormConfig = {dataSourceName: '', dataSourceType: ''};
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddDataSourceDialog(): void {
    const results = this.modalService.open({
      id: 'datasource-new-pre',
      width: '480px',
      backdropCloseable: true,
      placement: 'top',
      component: DatasourceNewPreComponent,
      data: {
        title: '',
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteDataSourceDialog(items: MetaDataSource[]): void {
    const results = this.modalService.open({
      id: 'datasource-delete',
      width: '346px',
      backdropCloseable: true,
      component: DatasourceDeleteComponent,
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

  openEditDataSourceDialog(item: MetaDataSource): void {
    const results = this.modalService.open({
      id: 'datasource-edit',
      width: '580px',
      backdropCloseable: true,
      component: DatasourceUpdateComponent,
      data: {
        title: {name: this.translate.instant('datadev.datasource')},
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

  openShowPasswordDialog(item: MetaDataSource): void {
    this.dataSourceService.showPassword(item).subscribe((d) => {
      if (d.success) {
        const results = this.dialogService.open({
          id: 'datasource-show-password',
          width: '350px',
          maxHeight: '600px',
          title: this.translate.instant('datadev.password'),
          content: d.data,
          backdropCloseable: true,
          dialogtype: 'info',
          buttons: [],
        });
      }
    });
  }

  getDataTableCheckedStatus(): void {
    if (this.dataTable.getCheckedRows().length > 0) {
      this.dataTableChecked = true;
    } else {
      this.dataTableChecked = false;
    }
  }
}
