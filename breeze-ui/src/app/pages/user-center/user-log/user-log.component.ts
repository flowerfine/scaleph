import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LoadingService } from 'ng-devui';
import { LoginLog } from 'src/app/@core/data/admin.data';
import { DEFAULT_PAGE_PARAM } from 'src/app/@core/data/app.data';
import { LogService } from 'src/app/@core/services/admin/log.service';
@Component({
  selector: 'app-user-log',
  templateUrl: './user-log.component.html',
  styleUrls: ['../user-center.component.scss'],
})
export class UserLogComponent implements OnInit {
  limit = 3 * 12 * 30 * 24 * 60 * 60; // tree years
  logItems: LoginLog[] = [];
  logPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  LoadTarget: any;
  logLoading: boolean = false;

  constructor(
    private logService: LogService,
    private translate: TranslateService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService
  ) {}

  ngOnInit(): void {
    this.refreshLogItems();
  }

  refreshLogItems() {
    this.openLogLoading();
    let param = {
      pageSize: this.logPager.pageSize,
      current: this.logPager.pageIndex,
    };
    this.logService.listByPage(param).subscribe((d) => {
      this.logPager.total = d.total;
      this.logItems = d.records;
      this.LoadTarget.loadingInstance.close();
      this.logLoading = false;
    });
  }

  openLogLoading() {
    const dc = this.doc.querySelector('#logContent');
    this.LoadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.logLoading = true;
  }
}
