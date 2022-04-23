import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DialogService, LoadingService } from 'ng-devui';
import { Message } from 'src/app/@core/data/admin.data';
import { DEFAULT_PAGE_PARAM } from 'src/app/@core/data/app.data';
import { MessageService } from 'src/app/@core/services/message.service';

@Component({
  selector: 'app-user-message',
  templateUrl: './user-message.component.html',
  styleUrls: ['../user-center.component.scss'],
})
export class UserMessageComponent implements OnInit {
  limit = 3 * 12 * 30 * 24 * 60 * 60; // tree years
  msgItems: Message[] = [];
  msgPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  LoadTarget: any;
  logLoading: boolean = false;

  constructor(
    private messageService: MessageService,
    private translate: TranslateService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private dialogService: DialogService
  ) {}

  ngOnInit(): void {
    this.refreshMessage();
  }

  getReadTag(item: Message) {
    return item.isRead.value == '1' ? this.translate.instant('userCenter.read') : this.translate.instant('userCenter.unRead');
  }

  getTagStyle(item: Message) {
    return item.isRead.value == '1' ? 'blue-w98' : 'pink-w98';
  }

  refreshMessage() {
    this.openLogLoading();
    let param = {
      pageSize: this.msgPager.pageSize,
      current: this.msgPager.pageIndex,
    };
    this.messageService.listByPage(param).subscribe((d) => {
      this.msgPager.total = d.total;
      this.msgItems = d.records;
      this.LoadTarget.loadingInstance.close();
      this.logLoading = false;
    });
  }

  openLogLoading() {
    const dc = this.doc.querySelector('#msgContent');
    this.LoadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.logLoading = true;
  }

  readMsg(event: boolean, item: Message) {
    if (event && item.isRead.value == '0') {
      item.isRead.value = '1';
      this.messageService.update(item).subscribe((d) => {});
    }
  }
}
