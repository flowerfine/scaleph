import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, FilterConfig, LoadingService, SortDirection, SortEventArg } from 'ng-devui';
import { DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { DiJobLog, DiJobLogParam } from 'src/app/@core/data/opscenter.data';
import { DictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { AuthService } from 'src/app/@core/services/auth.service';
import { ProjectService } from 'src/app/@core/services/datadev/project.service';
import { JobLogService } from 'src/app/@core/services/opscenter/job-log.service';
import { ClusterService } from 'src/app/@core/services/datadev/cluster.service';

import * as moment from 'moment';

@Component({
  selector: 'app-batch-job',
  templateUrl: './batch-job.component.html',
  styleUrls: ['./batch-job.component.scss'],
})
export class BatchJobComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: DiJobLog[] = [];
  projectList: Dict[] = [];
  clusterList: FilterConfig[] = [];
  jobInstanceStateList: Dict[] = [];
  sortedColumn: SortEventArg[] = [
    {
      field: 'startTime',
      direction: SortDirection.DESC,
    },
  ];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    sorter: DEFAULT_PAGE_PARAM.sorter,
  };
  searchFormConfig = {
    project: null,
    jobCode: '',
    clusterId: '',
    jobInstanceId: '',
    timeRange: [moment(0, 'HH').add(-1, 'days').toDate(), moment().toDate()],
    jobInstanceState: null,
  };
  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private jobLogService: JobLogService,
    private projectService: ProjectService,
    private clusterService: ClusterService,
    private dictDataService: DictDataService,
  ) {}

  ngOnInit(): void {
    this.dictDataService.listByType(DICT_TYPE.jobInstanceState).subscribe((d) => {
      this.jobInstanceStateList = d;
    });
    this.projectService.listAll().subscribe((d) => {
      this.projectList = d;
    });
    this.clusterService.listAll().subscribe((d: Dict[]) => {
      d.forEach((item) => {
        this.clusterList.push({ name: item.label, value: item.value });
      });
    });
    this.refreshTable();
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: DiJobLogParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      sorter: this.pager.sorter,
      jobType: 'b',
      projectId: this.searchFormConfig.project?.value ? this.searchFormConfig.project?.value : '',
      jobCode: this.searchFormConfig.jobCode,
      clusterId: this.searchFormConfig.clusterId,
      jobInstanceId: this.searchFormConfig.jobInstanceId,
      startTime: this.searchFormConfig.timeRange[0].getTime(),
      endTime: this.searchFormConfig.timeRange[1].getTime(),
      jobInstanceState: this.searchFormConfig.jobInstanceState?.value ? this.searchFormConfig.jobInstanceState?.value : '',
    };

    this.jobLogService.listBatchByPage(param).subscribe((d) => {
      this.pager.total = d.total;
      this.dataTableDs = d.records;
      this.loadTarget.loadingInstance.close();
      this.dataLoading = false;
      this.dataTable.setTableCheckStatus({ pageAllChecked: false });
      this.getDataTableCheckedStatus();
    });
  }

  clusterFilter($event) {
    this.searchFormConfig.clusterId = $event?.value;
    this.refreshTable();
  }

  sortChange(multiSort) {
    this.pager.sorter = multiSort;
    this.refreshTable();
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
    this.searchFormConfig = {
      project: null,
      jobCode: '',
      clusterId: '',
      jobInstanceId: '',
      timeRange: [moment(0, 'HH').add(-1, 'days').toDate(), moment().toDate()],
      jobInstanceState: null,
    };
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
      sorter: DEFAULT_PAGE_PARAM.sorter,
    };
    this.refreshTable();
  }
}
