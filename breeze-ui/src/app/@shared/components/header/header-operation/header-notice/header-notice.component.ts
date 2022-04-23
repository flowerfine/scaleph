import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Message, MessageParam } from 'src/app/@core/data/admin.data';
import { DEFAULT_PAGE_PARAM } from 'src/app/@core/data/app.data';
import { MessageService } from 'src/app/@core/services/message.service';

@Component({
  selector: 'da-header-notice',
  templateUrl: './header-notice.component.html',
  styleUrls: ['./header-notice.component.scss'],
})
export class HeaderNoticeComponent implements OnInit {
  @Output() countEvent = new EventEmitter<number>();

  tabActiveID: string | number = 'notice';

  tabTitles;
  messages: Message[] = [];

  constructor(private messageService: MessageService, private translate: TranslateService, private router: Router) {}

  ngOnInit() {
    this.tabTitles = {
      notice: this.translate.instant('header.msg.notice'),
    };
    this.refreshMessages();
  }

  refreshMessages() {
    let param: MessageParam = {
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      current: DEFAULT_PAGE_PARAM.pageIndex,
      isRead: '0',
    };
    this.messageService.listByPage(param).subscribe((d) => {
      this.messages = d.records;
    });
    this.messageService.countUnReadMessage().subscribe((d) => {
      this.countEvent.emit(d);
    });
  }

  handleNoticeClick(message: Message, type: string) {
    if (type === 'notice') {
      this.messageService.update(message).subscribe((d) => {
        if (d.success) {
          this.refreshMessages();
        }
      });
    }
  }

  handleClean(type: string) {
    if (type === 'notice') {
      this.messageService.readAll().subscribe((d) => {
        if (d.success) {
          this.refreshMessages();
        }
      });
    }
  }

  handleMore(type: string) {
    if (type === 'notice') {
      this.router.navigate(['/breeze', 'user-center'], {
        queryParams: {
          menu: 2,
        },
      });
    }
  }
}
