import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { MetaDataElement, MetaDataElementParam } from 'src/app/@core/data/stdata.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { DataElementService } from 'src/app/@core/services/stdata/data-element.service';
import { DataElementDeleteComponent } from './data-element-delete/data-element-delete.component';
import { DataElementNewComponent } from './data-element-new/data-element-new.component';
import { DataElementUpdateComponent } from './data-element-update/data-element-update.component';

@Component({
  selector: 'app-data-element',
  templateUrl: './data-element.component.html',
  styleUrls: ['./data-element.component.scss'],
})
export class DataElementComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: MetaDataElement[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = { elementCode: '', elementName: '' };
  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private metaElementService: DataElementService
  ) {}

  ngOnInit(): void {
    this.refreshTable();
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: MetaDataElementParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      elementCode: this.searchFormConfig.elementCode,
      elementName: this.searchFormConfig.elementName,
    };
    this.metaElementService.listByPage(param).subscribe((d) => {
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
    this.searchFormConfig = { elementCode: '', elementName: '' };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddDataElementDialog() {
    const results = this.modalService.open({
      id: 'data-element-new',
      width: '580px',
      backdropCloseable: true,
      component: DataElementNewComponent,
      data: {
        title: { name: this.translate.instant('stdata.data.element') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteDataElementDialog(items: MetaDataElement[]) {
    const results = this.modalService.open({
      id: 'data-element-delete',
      width: '346px',
      backdropCloseable: true,
      component: DataElementDeleteComponent,
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

  openEditDataElementDialog(item: MetaDataElement) {
    const results = this.modalService.open({
      id: 'data-element-edit',
      width: '580px',
      backdropCloseable: true,
      component: DataElementUpdateComponent,
      data: {
        title: { name: this.translate.instant('stdata.data.element') },
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
