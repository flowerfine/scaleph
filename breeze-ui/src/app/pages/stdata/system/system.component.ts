import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { MetaSystem, MetaSystemParam } from 'src/app/@core/data/meta.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { MetaSystemService } from 'src/app/@core/services/meta-system.service';
import { SystemDeleteComponent } from './system-delete/system-delete.component';
import { SystemNewComponent } from './system-new/system-new.component';
import { SystemUpdateComponent } from './system-update/system-update.component';

@Component({
  selector: 'app-system',
  templateUrl: './system.component.html',
  styleUrls: ['./system.component.scss'],
})
export class SystemComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: MetaSystem[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = { systemCode: '', systemName: '' };

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private metaSystemService: MetaSystemService
  ) {}

  ngOnInit(): void {
    this.refreshTable();
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: MetaSystemParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      systemCode: this.searchFormConfig.systemCode,
      systemName: this.searchFormConfig.systemName,
    };
    this.metaSystemService.listByPage(param).then((d) => {
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
    this.searchFormConfig = { systemCode: '', systemName: '' };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddSystemDialog() {
    const results = this.modalService.open({
      id: 'system-new',
      width: '580px',
      backdropCloseable: true,
      component: SystemNewComponent,
      data: {
        title: { name: this.translate.instant('stdata.system') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteSystemDialog(items: MetaSystem[]) {
    const results = this.modalService.open({
      id: 'system-delete',
      width: '346px',
      backdropCloseable: true,
      component: SystemDeleteComponent,
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

  openEditSystemDialog(item: MetaSystem) {
    const results = this.modalService.open({
      id: 'system-edit',
      width: '580px',
      backdropCloseable: true,
      component: SystemUpdateComponent,
      data: {
        title: { name: this.translate.instant('stdata.system') },
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
