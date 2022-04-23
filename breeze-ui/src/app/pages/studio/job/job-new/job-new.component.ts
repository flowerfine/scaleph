import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout, ITreeItem } from 'ng-devui';
import { DiJob } from 'src/app/@core/data/studio.data';
import { DiDirectoryService } from 'src/app/@core/services/di-directory.service';
import { DiJobService } from 'src/app/@core/services/di-job.service';

@Component({
  selector: 'app-job-new',
  templateUrl: './job-new.component.html',
  styleUrls: ['../job.component.scss'],
})
export class JobNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  jobType: string;
  projectId: number;
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
    jobCode: null,
    jobName: null,
    directory: null,
    remark: null,
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private jobService: DiJobService,
    private directoryService: DiDirectoryService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.projectId = this.data.projectId;
    this.jobType = this.data.type;
    this.refreshDirList();
  }

  refreshDirList(): void {
    this.directoryService.listProjectDir(this.projectId).subscribe((d) => {
      this.dirList = d;
    });
  }

  submitForm({ valid }) {
    let job: DiJob = {
      projectId: this.projectId,
      jobCode: this.formData.jobCode,
      jobName: this.formData.jobName,
      directory: this.formData.directory,
      jobType: { label: '', value: this.jobType },
      remark: this.formData.remark,
    };
    if (valid) {
      this.jobService.add(job).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh(d.data?.id);
        }
      });
    }
  }
}
