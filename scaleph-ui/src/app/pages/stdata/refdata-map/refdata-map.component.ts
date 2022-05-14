import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { MetaDataMap, MetaDataMapParam } from 'src/app/@core/data/stdata.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { RefdataService } from 'src/app/@core/services/stdata/refdata.service';
import { RefdataMapDeleteComponent } from './refdata-map-delete/refdata-map-delete.component';
import { RefdataMapNewComponent } from './refdata-map-new/refdata-map-new.component';

@Component({
  selector: 'app-refdata-map',
  templateUrl: './refdata-map.component.html',
  styleUrls: ['./refdata-map.component.scss'],
})
export class RefdataMapComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: MetaDataMap[] = [];
  checkboxLabel = this.translate.instant('stdata.auto');
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = { srcDataSetTypeCode: '', tgtDataSetTypeCode: '', srcDataSetCode: '', tgtDataSetCode: '', auto: true };
  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private refdataService: RefdataService
  ) {}

  ngOnInit(): void {
    this.refreshTable();
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: MetaDataMapParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      srcDataSetTypeCode: this.searchFormConfig.srcDataSetTypeCode,
      tgtDataSetTypeCode: this.searchFormConfig.tgtDataSetTypeCode,
      srcDataSetCode: this.searchFormConfig.srcDataSetCode,
      tgtDataSetCode: this.searchFormConfig.tgtDataSetCode,
      auto: this.searchFormConfig.auto,
    };
    this.refdataService.listMapByPage(param).subscribe((d) => {
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
    this.searchFormConfig = { srcDataSetTypeCode: '', tgtDataSetTypeCode: '', srcDataSetCode: '', tgtDataSetCode: '', auto: true };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddDataMapDialog() {
    const results = this.modalService.open({
      id: 'refdata-map-new',
      width: '580px',
      backdropCloseable: true,
      component: RefdataMapNewComponent,
      data: {
        title: { name: this.translate.instant('stdata.refdata.map') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteDataMapDialog(items: MetaDataMap[]) {
    const results = this.modalService.open({
      id: 'refdata-map-delete',
      width: '346px',
      backdropCloseable: true,
      component: RefdataMapDeleteComponent,
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
