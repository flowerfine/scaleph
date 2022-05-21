import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective } from 'ng-devui';
import { DiJob } from 'src/app/@core/data/datadev.data';
import { JobService } from 'src/app/@core/services/datadev/job.service';

@Component({
  selector: 'app-job-crontab-setting',
  templateUrl: './job-crontab-setting.component.html',
  styleUrls: ['../job.component.scss'],
})
export class JobCrontabSettingComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  minutes: number[] = [];
  hours: number[] = [];
  days: number[] = [];
  months: number[] = [];
  weeks: number[] = [];
  fireTimeList: Date[] = [];
  crontabStr: string = '';
  activeTabId: string = '';
  cron_minute = {
    type: 1,
    value: '*',
    min: 0,
    max: 59,
    intervalFrom: 0,
    intervalTo: 0,
    loopFrom: 0,
    loopTo: 0,
    custom: [],
  };
  cron_hour = {
    type: 1,
    value: '*',
    min: 0,
    max: 23,
    intervalFrom: 0,
    intervalTo: 0,
    loopFrom: 0,
    loopTo: 0,
    custom: [],
  };
  cron_day = {
    type: 1,
    value: '*',
    min: 1,
    max: 31,
    intervalFrom: 1,
    intervalTo: 1,
    loopFrom: 1,
    loopTo: 1,
    custom: [],
  };
  cron_month = {
    type: 1,
    value: '*',
    min: 1,
    max: 12,
    intervalFrom: 1,
    intervalTo: 1,
    loopFrom: 1,
    loopTo: 1,
    custom: [],
  };
  cron_week = {
    type: 0,
    value: '?',
    min: 1,
    max: 7,
    intervalFrom: 1,
    intervalTo: 1,
    custom: [],
  };
  cron_custom = '*';
  value: number = 0;
  constructor(private elr: ElementRef, private translate: TranslateService, private jobService: JobService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    for (let i = 0; i < 60; i++) {
      this.minutes.push(i);
    }
    for (let i = 0; i < 24; i++) {
      this.hours.push(i);
    }
    for (let i = 1; i <= 31; i++) {
      this.days.push(i);
    }
    for (let i = 1; i <= 12; i++) {
      this.months.push(i);
    }
    for (let i = 1; i <= 7; i++) {
      this.weeks.push(i);
    }
    this.buildCrontab();
    this.crontabStr = this.data.item.jobCrontab;
  }

  buildCrontab() {
    if (this.activeTabId != 'custom') {
      //minute
      if (this.cron_minute.type === 1) {
        this.cron_minute.value = '*';
      } else if (this.cron_minute.type === 2) {
        this.cron_minute.value = this.cron_minute.intervalFrom + '-' + this.cron_minute.intervalTo;
      } else if (this.cron_minute.type === 3) {
        this.cron_minute.value = this.cron_minute.loopFrom + '/' + this.cron_minute.loopTo;
      } else if (this.cron_minute.type === 4) {
        this.cron_minute.value = this.cron_minute.custom.join();
      }
      //hour
      if (this.cron_hour.type === 1) {
        this.cron_hour.value = '*';
      } else if (this.cron_hour.type === 2) {
        this.cron_hour.value = this.cron_hour.intervalFrom + '-' + this.cron_hour.intervalTo;
      } else if (this.cron_hour.type === 3) {
        this.cron_hour.value = this.cron_hour.loopFrom + '/' + this.cron_hour.loopTo;
      } else if (this.cron_hour.type === 4) {
        this.cron_hour.value = this.cron_hour.custom.join();
      }
      //day
      if (this.cron_day.type === 0) {
        this.cron_day.value = '?';
      } else if (this.cron_day.type === 1) {
        this.cron_day.value = '*';
      } else if (this.cron_day.type === 2) {
        this.cron_day.value = 'L';
      } else if (this.cron_day.type === 3) {
        this.cron_day.value = this.cron_day.intervalFrom + '-' + this.cron_day.intervalTo;
      } else if (this.cron_day.type === 4) {
        this.cron_day.value = this.cron_day.loopFrom + '/' + this.cron_day.loopTo;
      } else if (this.cron_day.type === 5) {
        this.cron_day.value = this.cron_day.custom.join();
      }
      //month
      if (this.cron_month.type === 1) {
        this.cron_month.value = '*';
      } else if (this.cron_month.type === 2) {
        this.cron_month.value = this.cron_month.intervalFrom + '-' + this.cron_month.intervalTo;
      } else if (this.cron_hour.type === 3) {
        this.cron_month.value = this.cron_month.loopFrom + '/' + this.cron_month.loopTo;
      } else if (this.cron_month.type === 4) {
        this.cron_month.value = this.cron_month.custom.join();
      }
      //week
      if (this.cron_week.type === 0) {
        this.cron_week.value = '?';
      } else if (this.cron_week.type === 1) {
        this.cron_week.value = '*';
      } else if (this.cron_week.type === 2) {
        this.cron_week.value = this.cron_week.intervalFrom + '-' + this.cron_week.intervalTo;
      } else if (this.cron_week.type === 3) {
        this.cron_week.value = this.cron_week.custom.join();
      }
      this.crontabStr =
        '0 ' +
        this.cron_minute.value +
        ' ' +
        this.cron_hour.value +
        ' ' +
        this.cron_day.value +
        ' ' +
        this.cron_month.value +
        ' ' +
        this.cron_week.value;
    }
    this.jobService.listNext5FireTime(this.crontabStr).subscribe((d) => {
      this.fireTimeList = d;
    });
  }

  activeTabChange(id) {
    this.activeTabId = id;
  }

  submitForm() {
    if (this.crontabStr != '' || this.crontabStr != undefined || this.crontabStr != null) {
      let job: DiJob = {
        id: this.data.item.id,
        jobCrontab: this.crontabStr,
        projectId: this.data.item.projectId,
        jobCode: this.data.item.jobCode,
        jobName: this.data.item.jobName,
        directory: this.data.item.directory,
      };
      this.jobService.update(job).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
