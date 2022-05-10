import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout, ITreeItem } from 'ng-devui';
import { DiJob } from 'src/app/@core/data/datadev.data';
import { DirectoryService } from 'src/app/@core/services/datadev/directory.service';
import { JobService } from 'src/app/@core/services/datadev/job.service';

@Component({
  selector: 'app-job-update',
  templateUrl: './job-update.component.html',
  styleUrls: ['../job.component.scss'],
})
export class JobUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  dirList: ITreeItem[];
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    jobCodeRules: {
      validators: [
        { required: true },
        { maxlength: 120 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    jobNameRules: {
      validators: [{ required: true }, { maxlength: 200 }],
    },
    directoryRules: {
      validators: [{ required: true }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    id: null,
    jobCode: null,
    jobName: null,
    directory: null,
    remark: null,
  };
  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private jobService: JobService,
    private directoryService: DirectoryService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      jobCode: this.data.item.jobCode,
      jobName: this.data.item.jobName,
      directory: this.data.item.directory,
      remark: this.data.item.remark,
    };
    this.refreshDirList();
  }

  refreshDirList(): void {
    this.directoryService.listProjectDir(this.data.item.projectId).subscribe((d) => {
      this.dirList = d;
    });
  }

  submitForm({ valid }) {
    let job: DiJob = {
      id: this.data.item.id,
      projectId: this.data.item.projectId,
      jobCode: this.formData.jobCode,
      jobName: this.formData.jobName,
      directory: this.formData.directory,
      remark: this.formData.remark,
    };
    if (valid) {
      this.jobService.update(job).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
