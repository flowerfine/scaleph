import { DOCUMENT } from '@angular/common';
import { Inject, ViewChild } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, Dict, PRIVILEGE_CODE, USER_AUTH } from 'src/app/@core/data/app.data';
import { DiResourceFile, DiResourceFileParam } from 'src/app/@core/data/studio.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { DiProjectService } from 'src/app/@core/services/di-project.service';
import { DiResourceFileService } from 'src/app/@core/services/di-resource.service';
import { ResourceDeleteComponent } from './resource-delete/resource-delete.component';

@Component({
  selector: 'app-resource',
  templateUrl: './resource.component.html',
  styleUrls: ['./resource.component.scss'],
})
export class ResourceComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: DiResourceFile[] = [];
  projectList: Dict[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = { project: null, fileName: '', fileType: '' };

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private projectService: DiProjectService,
    private resourceService: DiResourceFileService
  ) {}

  ngOnInit(): void {
    this.projectService.listAll().subscribe((d) => {
      this.projectList = d;
    });
    this.refreshTable();
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: DiResourceFileParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      projectId: this.searchFormConfig.project ? this.searchFormConfig.project.value : '',
      fileName: this.searchFormConfig.fileName,
    };

    this.resourceService.listByPage(param).subscribe((d) => {
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
    this.searchFormConfig = { project: null, fileName: '', fileType: '' };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openDeleteResourceDialog(items: DiResourceFile[]) {
    const results = this.modalService.open({
      id: 'resource-delete',
      width: '346px',
      backdropCloseable: true,
      component: ResourceDeleteComponent,
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

  downloadResource(item: DiResourceFile) {
    let url: string =
      'api/di/resource/download?projectId=' +
      item.projectId +
      '&fileName=' +
      item.fileName +
      '&' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    const a = document.createElement('a');
    a.href = url;
    a.download = item.fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
