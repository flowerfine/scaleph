import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { SysDictData, SysDictDataParam, SysDictType, SysDictTypeParam } from 'src/app/@core/data/admin.data';
import { DEFAULT_PAGE_PARAM, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { DictTypeDeleteComponent } from './dict-type-delete/dict-type-delete.component';
import { DictTypeNewComponent } from './dict-type-new/dict-type-new.component';
import { DictTypeUpdateComponent } from './dict-type-update/dict-type-update.component';
import { DictDataNewComponent } from './dict-data-new/dict-data-new.component';
import { DictDataUpdateComponent } from './dict-data-update/dict-data-update.component';
import { DictDataDeleteComponent } from './dict-data-delete/dict-data-delete.component';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from 'src/app/@core/services/auth.service';
import { SysDictTypeService } from 'src/app/@core/services/admin/dict-type.service';
import { SysDictDataService } from 'src/app/@core/services/admin/dict-data.service';

@Component({
  selector: 'app-dict',
  templateUrl: './dict.component.html',
  styleUrls: ['./dict.component.scss'],
})
export class DictComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dictTypeTable', { static: true }) dictTypeTable: DataTableComponent;
  @ViewChild('dictDataTable', { static: true }) dictDataTable: DataTableComponent;
  dictTypeLoading: boolean = false;
  dictDataLoading: boolean = false;
  dictTypeTableChecked: boolean = false;
  dictDataTableChecked: boolean = false;
  dictTypeLoadTarget: any;
  dictDataLoadTarget: any;
  dictTypeTableDs: SysDictType[] = [];
  dictDataTableDs: SysDictData[] = [];
  dictTypePager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  dictDataPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  searchFormConfig = { dictTypeCode: '', dictCode: '', dictValue: '' };

  constructor(
    private dictTypeService: SysDictTypeService,
    private dictDataService: SysDictDataService,
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
    this.refreshDictTypeTable();
    this.refreshDictDataTable();
  }

  refreshDictTypeTable() {
    this.openDictTypeLoading();
    let param: SysDictTypeParam = {
      pageSize: this.dictTypePager.pageSize,
      current: this.dictTypePager.pageIndex,
      dictTypeCode: this.searchFormConfig.dictTypeCode,
    };
    this.dictTypeService.listByPage(param).subscribe((d) => {
      this.dictTypePager.total = d.total;
      this.dictTypeTableDs = d.records;
      this.dictTypeLoadTarget.loadingInstance.close();
      this.dictTypeLoading = false;
      this.dictTypeTable.setTableCheckStatus({ pageAllChecked: false });
      this.getDictTypeCheckedStatus();
    });
  }

  refreshDictDataTable() {
    this.openDictDataLoading();
    let param: SysDictDataParam = {
      pageSize: this.dictDataPager.pageSize,
      current: this.dictDataPager.pageIndex,
      dictTypeCode: this.searchFormConfig.dictTypeCode,
      dictCode: this.searchFormConfig.dictCode,
      dictValue: this.searchFormConfig.dictValue,
    };
    this.dictDataService.listByPage(param).subscribe((d) => {
      this.dictDataPager.total = d.total;
      this.dictDataTableDs = d.records;
      this.dictDataLoadTarget.loadingInstance.close();
      this.dictDataLoading = false;
      this.dictDataTable.setTableCheckStatus({ pageAllChecked: false });
      this.getDictDataCheckedStatus();
    });
  }

  dataTypeRowClick(row: SysDictType) {
    this.searchFormConfig.dictTypeCode = row.dictTypeCode;
    this.refreshDictDataTable();
  }

  reset() {
    this.searchFormConfig = { dictTypeCode: '', dictCode: '', dictValue: '' };
    this.dictTypePager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.dictDataPager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshDictTypeTable();
    this.refreshDictDataTable();
  }

  openDictTypeLoading() {
    const dc = this.doc.querySelector('#dictTypeContent');
    this.dictTypeLoadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.dictTypeLoading = true;
  }

  openDictDataLoading() {
    const dc = this.doc.querySelector('#dictDataContent');
    this.dictDataLoadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.dictDataLoading = true;
  }

  getDictTypeCheckedStatus() {
    if (this.dictTypeTable.getCheckedRows().length > 0) {
      this.dictTypeTableChecked = true;
    } else {
      this.dictTypeTableChecked = false;
    }
  }

  getDictDataCheckedStatus() {
    if (this.dictDataTable.getCheckedRows().length > 0) {
      this.dictDataTableChecked = true;
    } else {
      this.dictDataTableChecked = false;
    }
  }

  openAddDictTypeDialog() {
    const results = this.modalService.open({
      id: 'dict-type-new',
      width: '580px',
      backdropCloseable: true,
      component: DictTypeNewComponent,
      data: {
        title: { name: this.translate.instant('admin.dictType') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshDictTypeTable();
        },
      },
    });
  }

  openAddDictDataDialog() {
    const results = this.modalService.open({
      id: 'dict-data-new',
      width: '580px',
      backdropCloseable: true,
      component: DictDataNewComponent,
      data: {
        title: { name: this.translate.instant('admin.dict') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshDictDataTable();
        },
      },
    });
  }

  openEditDictTypeDialog(item: SysDictType) {
    const results = this.modalService.open({
      id: 'dict-type-edit',
      width: '580px',
      backdropCloseable: true,
      component: DictTypeUpdateComponent,
      data: {
        title: { name: this.translate.instant('admin.dictType') },
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshDictTypeTable();
        },
      },
    });
  }

  openEditDictDataDialog(item: SysDictData) {
    const results = this.modalService.open({
      id: 'dict-data-edit',
      width: '580px',
      backdropCloseable: true,
      component: DictDataUpdateComponent,
      data: {
        title: { name: this.translate.instant('admin.dict') },
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshDictDataTable();
        },
      },
    });
  }

  openDeleteDictTypeDialog(items: SysDictType[]) {
    const results = this.modalService.open({
      id: 'dict-type-delete',
      width: '346px',
      backdropCloseable: true,
      component: DictTypeDeleteComponent,
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

  openDeleteDictDataDialog(items: SysDictData[]) {
    const results = this.modalService.open({
      id: 'dict-data-delete',
      width: '346px',
      backdropCloseable: true,
      component: DictDataDeleteComponent,
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
