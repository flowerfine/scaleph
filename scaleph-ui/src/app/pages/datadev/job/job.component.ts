import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, ITreeItem, LoadingService, ModalService, OperableTreeComponent, TreeNode } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { DiJob, DiJobParam } from 'src/app/@core/data/datadev.data';
import { SysDictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { AuthService } from 'src/app/@core/services/auth.service';
import { DirectoryService } from 'src/app/@core/services/datadev/directory.service';
import { JobService } from 'src/app/@core/services/datadev/job.service';
import { DirectoryDeleteComponent } from './directory-delete/directory-delete.component';
import { DirectoryNewComponent } from './directory-new/directory-new.component';
import { DirectoryUpdateComponent } from './directory-update/directory-update.component';
import { JobCrontabSettingComponent } from './job-crontab-setting/job-crontab-setting.component';
import { JobDeleteComponent } from './job-delete/job-delete.component';
import { JobNewComponent } from './job-new/job-new.component';
import { JobStartComponent } from './job-start/job-start.component';
import { JobStopComponent } from './job-stop/job-stop.component';
import { JobUpdateComponent } from './job-update/job-update.component';
@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrls: ['./job.component.scss'],
})
export class JobComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  @ViewChild('operableTree', { static: true }) operableTree: OperableTreeComponent;
  currentSelectedNode;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: DiJob[] = [];
  defaultProjectId: number = 0;
  defaultProjectCode: string = '';
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = { jobCode: '', jobName: '', jobType: null, runtimeState: null, directoryId: '' };
  jobTypeList: Dict[] = [];
  jobStatusList: Dict[] = [];
  runtimeStateList: Dict[] = [];
  dirList: ITreeItem[];

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private jobService: JobService,
    private directoryService: DirectoryService,
    private dictDataService: SysDictDataService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.dictDataService.listByType(DICT_TYPE.jobType).subscribe((d) => {
      this.jobTypeList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.jobStatus).subscribe((d) => {
      this.jobStatusList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.runtimeState).subscribe((d) => {
      this.runtimeStateList = d;
    });

    this.route.queryParams.subscribe((params) => {
      let id: number = params['projectId'];
      let projectCode: string = params['projectCode'];
      if (id != null && id != undefined && id != 0) {
        this.defaultProjectId = id;
        this.defaultProjectCode = projectCode;
        this.refreshTable();
        this.refreshDirList();
      }
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: DiJobParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      projectId: this.defaultProjectId,
      jobCode: this.searchFormConfig.jobCode,
      jobName: this.searchFormConfig.jobName,
      jobType: this.searchFormConfig.jobType ? this.searchFormConfig.jobType.value : '',
      runtimeState: this.searchFormConfig.runtimeState ? this.searchFormConfig.runtimeState.value : '',
      directoryId: this.searchFormConfig.directoryId,
    };

    this.jobService.listByPage(param).subscribe((d) => {
      this.pager.total = d.total;
      this.dataTableDs = d.records;
      this.loadTarget.loadingInstance.close();
      this.dataLoading = false;
      this.dataTable.setTableCheckStatus({ pageAllChecked: false });
      this.getDataTableCheckedStatus();
    });
  }

  refreshDirList(): void {
    this.directoryService.listProjectDir(this.defaultProjectId).subscribe((d) => {
      this.dirList = d;
    });
  }

  dataTableRowClick(row: DiJob): void {
    this.operableTree.treeFactory.openNodesById(row.directory.id);
    this.operableTree.treeFactory.activeNodeById(row.directory.id);
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
    this.searchFormConfig = { jobCode: '', jobName: '', jobType: null, runtimeState: null, directoryId: '' };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openJobWorkbench(item: DiJob) {
    const url = this.router.serializeUrl(this.router.createUrlTree(['workbench']));
    window.open(url + '?id=' + item.id, '_blank', 'noopener');
  }

  openAddJobDialog(jobType: string) {
    const results = this.modalService.open({
      id: 'job-new',
      width: '580px',
      backdropCloseable: true,
      component: JobNewComponent,
      data: {
        title: { name: this.translate.instant('datadev.job') },
        type: jobType,
        projectId: this.defaultProjectId,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: (jobId: number) => {
          this.refreshTable();
          const url = this.router.serializeUrl(this.router.createUrlTree(['workbench']));
          window.open(url + '?id=' + jobId, '_blank', 'noopener');
        },
      },
    });
  }

  openDeleteJobDialog(items: DiJob[]) {
    const results = this.modalService.open({
      id: 'job-delete',
      width: '346px',
      backdropCloseable: true,
      component: JobDeleteComponent,
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

  openEditJobDialog(item: DiJob) {
    const results = this.modalService.open({
      id: 'job-edit',
      width: '580px',
      backdropCloseable: true,
      component: JobUpdateComponent,
      data: {
        title: { name: this.translate.instant('datadev.job') },
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

  openJobCrontabSettingDialog(item: DiJob) {
    const results = this.modalService.open({
      id: 'job-crontab-setting',
      width: '580px',
      backdropCloseable: true,
      component: JobCrontabSettingComponent,
      data: {
        title: { name: this.translate.instant('datadev.job') },
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

  openRunJobDialog(item: DiJob) {
    const results = this.modalService.open({
      id: 'job-start',
      width: '580px',
      backdropCloseable: true,
      component: JobStartComponent,
      data: {
        title: this.translate.instant('datadev.job'),
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

  openStopJobDialog(item: DiJob) {
    const results = this.modalService.open({
      id: 'job-stop',
      width: '346px',
      backdropCloseable: true,
      component: JobStopComponent,
      data: {
        title: this.translate.instant('datadev.job'),
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

  onDirSelected(node: TreeNode) {
    this.searchFormConfig.directoryId = node.id;
    this.refreshTable();
  }

  openAddDirDialog(event, node) {
    const results = this.modalService.open({
      id: 'directory-new',
      width: '580px',
      backdropCloseable: true,
      component: DirectoryNewComponent,
      data: {
        title: { name: this.translate.instant('datadev.directory') },
        item: node,
        projectId: this.defaultProjectId,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: (node: TreeNode) => {
          this.operableTree.treeFactory.addNode(node);
          if (node.parentId != undefined) {
            this.operableTree.treeFactory.openNodesById(node.id);
            this.operableTree.treeFactory.activeNodeById(node.id);
          }
        },
      },
    });
  }

  openEditDirDialog(event, node) {
    const results = this.modalService.open({
      id: 'directory-edit',
      width: '580px',
      backdropCloseable: true,
      component: DirectoryUpdateComponent,
      data: {
        title: { name: this.translate.instant('datadev.directory') },
        item: node,
        index: this.operableTree.treeFactory.getNodeIndex(node),
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: (node) => {
          let editNode = this.operableTree.treeFactory.getNodeById(node.id);
          editNode.title = node.title;
        },
      },
    });
  }

  openDeleteDirDialog(event, node) {
    const results = this.modalService.open({
      id: 'directory-delete',
      width: '346px',
      backdropCloseable: true,
      component: DirectoryDeleteComponent,
      data: {
        title: this.translate.instant('app.common.operate.delete.confirm.title'),
        items: node,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.operableTree.treeFactory.deleteNodeById(node.id);
        },
      },
    });
  }
}
