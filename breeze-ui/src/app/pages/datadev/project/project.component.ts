import { DOCUMENT } from '@angular/common';
import { Inject } from '@angular/core';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, LoadingService, ModalService } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { DiProject, DiProjectParam } from 'src/app/@core/data/datadev.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { ProjectService } from 'src/app/@core/services/datadev/project.service';
import { ResourceNewComponent } from '../resource/resource-new/resource-new.component';
import { ProjectDeleteComponent } from './project-delete/project-delete.component';
import { ProjectNewComponent } from './project-new/project-new.component';
import { ProjectUpdateComponent } from './project-update/project-update.component';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss'],
})
export class ProjectComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: DiProject[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = { projectCode: '', projectName: '' };
  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private projectService: ProjectService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.refreshTable();
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: DiProjectParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      projectCode: this.searchFormConfig.projectCode,
      projectName: this.searchFormConfig.projectName,
    };

    this.projectService.listByPage(param).subscribe((d) => {
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
    this.searchFormConfig = { projectCode: '', projectName: '' };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddProjectDialog() {
    const results = this.modalService.open({
      id: 'project-new',
      width: '580px',
      backdropCloseable: true,
      component: ProjectNewComponent,
      data: {
        title: { name: this.translate.instant('datadev.project') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteProjectDialog(items: DiProject[]) {
    const results = this.modalService.open({
      id: 'project-delete',
      width: '346px',
      backdropCloseable: true,
      component: ProjectDeleteComponent,
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

  openEditProjectDialog(item: DiProject) {
    const results = this.modalService.open({
      id: 'project-edit',
      width: '580px',
      backdropCloseable: true,
      component: ProjectUpdateComponent,
      data: {
        title: { name: this.translate.instant('datadev.project') },
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

  openProject(row: DiProject) {
    this.router.navigate(['/breeze', 'datadev', 'job'], {
      queryParams: {
        projectId: row.id,
        projectCode: row.projectCode,
      },
    });
  }

  openAddResourceDialog(row: DiProject) {
    const results = this.modalService.open({
      id: 'resource-new',
      width: '580px',
      backdropCloseable: true,
      component: ResourceNewComponent,
      data: {
        title: { name: this.translate.instant('datadev.resource') },
        projectId: row.id,
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
