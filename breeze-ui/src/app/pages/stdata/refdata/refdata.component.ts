import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { MetaDataSet, MetaDataSetParam, MetaDataSetType, MetaDataSetTypeParam } from 'src/app/@core/data/stdata.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { RefdataService } from 'src/app/@core/services/stdata/refdata.service';
import { RefdataDataDeleteComponent } from './refdata-data-delete/refdata-data-delete.component';
import { RefdataDataNewComponent } from './refdata-data-new/refdata-data-new.component';
import { RefdataDataUpdateComponent } from './refdata-data-update/refdata-data-update.component';
import { RefdataTypeDeleteComponent } from './refdata-type-delete/refdata-type-delete.component';
import { RefdataTypeNewComponent } from './refdata-type-new/refdata-type-new.component';
import { RefdataTypeUpdateComponent } from './refdata-type-update/refdata-type-update.component';

@Component({
  selector: 'app-refdata',
  templateUrl: './refdata.component.html',
  styleUrls: ['./refdata.component.scss'],
})
export class RefdataComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('refdataTypeTable', { static: true }) refdataTypeTable: DataTableComponent;
  @ViewChild('refDataTable', { static: true }) refataTable: DataTableComponent;
  refdataTypeLoading: boolean = false;
  refdataLoading: boolean = false;
  refdataTypeTableChecked: boolean = false;
  refdataTableChecked: boolean = false;
  refdataTypeLoadTarget: any;
  refdataLoadTarget: any;
  refdataTypeTableDs: MetaDataSetType[] = [];
  refdataTableDs: MetaDataSet[] = [];
  refdataTypePager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  refdataPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  searchFormConfig = { dataSetTypeCode: '', dataSetCode: '', dataSetValue: '' };

  constructor(
    private refdataService: RefdataService,
    private modalService: ModalService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.refreshTable();
  }
  refreshTable() {
    this.refreshRefdataTypeTable();
    this.refreshRefdataTable();
  }

  refreshRefdataTypeTable() {
    this.openRefdataTypeLoading();
    let param: MetaDataSetTypeParam = {
      pageSize: this.refdataTypePager.pageSize,
      current: this.refdataTypePager.pageIndex,
      dataSetTypeCode: this.searchFormConfig.dataSetTypeCode,
    };
    this.refdataService.listTypeByPage(param).subscribe((d) => {
      this.refdataTypePager.total = d.total;
      this.refdataTypeTableDs = d.records;
      this.refdataTypeLoadTarget.loadingInstance.close();
      this.refdataTypeLoading = false;
      this.refdataTypeTable.setTableCheckStatus({ pageAllChecked: false });
      this.getRefdataTypeCheckedStatus();
    });
  }

  refreshRefdataTable() {
    this.openRefdataDataLoading();
    let param: MetaDataSetParam = {
      pageSize: this.refdataPager.pageSize,
      current: this.refdataPager.pageIndex,
      dataSetTypeCode: this.searchFormConfig.dataSetTypeCode,
      dataSetCode: this.searchFormConfig.dataSetCode,
      dataSetValue: this.searchFormConfig.dataSetValue,
    };
    this.refdataService.listDataByPage(param).subscribe((d) => {
      this.refdataPager.total = d.total;
      this.refdataTableDs = d.records;
      this.refdataLoadTarget.loadingInstance.close();
      this.refdataLoading = false;
      this.refataTable.setTableCheckStatus({ pageAllChecked: false });
      this.getRefdataCheckedStatus();
    });
  }

  dataTypeRowClick(row: MetaDataSetType) {
    this.searchFormConfig.dataSetTypeCode = row.dataSetTypeCode;
    this.refreshRefdataTable();
  }

  reset() {
    this.searchFormConfig = { dataSetTypeCode: '', dataSetCode: '', dataSetValue: '' };
    this.refdataTypePager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refdataPager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshRefdataTypeTable();
    this.refreshRefdataTable();
  }

  openRefdataTypeLoading() {
    const dc = this.doc.querySelector('#refdataTypeContent');
    this.refdataTypeLoadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.refdataTypeLoading = true;
  }

  openRefdataDataLoading() {
    const dc = this.doc.querySelector('#refdataContent');
    this.refdataLoadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.refdataLoading = true;
  }

  getRefdataTypeCheckedStatus() {
    if (this.refdataTypeTable.getCheckedRows().length > 0) {
      this.refdataTypeTableChecked = true;
    } else {
      this.refdataTypeTableChecked = false;
    }
  }

  getRefdataCheckedStatus() {
    if (this.refataTable.getCheckedRows().length > 0) {
      this.refdataTableChecked = true;
    } else {
      this.refdataTableChecked = false;
    }
  }

  openAddRefdataTypeDialog() {
    const results = this.modalService.open({
      id: 'refdata-type-new',
      width: '580px',
      backdropCloseable: true,
      component: RefdataTypeNewComponent,
      data: {
        title: { name: this.translate.instant('stdata.refdata.type') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshRefdataTypeTable();
        },
      },
    });
  }

  openAddRefdataDialog() {
    const results = this.modalService.open({
      id: 'refdata-data-new',
      width: '580px',
      backdropCloseable: true,
      component: RefdataDataNewComponent,
      data: {
        title: { name: this.translate.instant('stdata.refdata.data') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshRefdataTable();
        },
      },
    });
  }

  openEditRefdataTypeDialog(item: MetaDataSetType) {
    const results = this.modalService.open({
      id: 'refdata-type-edit',
      width: '580px',
      backdropCloseable: true,
      component: RefdataTypeUpdateComponent,
      data: {
        title: { name: this.translate.instant('stdata.refdata.type') },
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshRefdataTypeTable();
        },
      },
    });
  }

  openEditRefdataDialog(item: MetaDataSet) {
    const results = this.modalService.open({
      id: 'refdata-data-edit',
      width: '580px',
      backdropCloseable: true,
      component: RefdataDataUpdateComponent,
      data: {
        title: { name: this.translate.instant('stdata.refdata.data') },
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshRefdataTable();
        },
      },
    });
  }

  openDeleteRefdataTypeDialog(items: MetaDataSetType[]) {
    const results = this.modalService.open({
      id: 'refdata-type-delete',
      width: '346px',
      backdropCloseable: true,
      component: RefdataTypeDeleteComponent,
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

  openDeleteRefdataDialog(items: MetaDataSet[]) {
    const results = this.modalService.open({
      id: 'refdata-data-delete',
      width: '346px',
      backdropCloseable: true,
      component: RefdataDataDeleteComponent,
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
