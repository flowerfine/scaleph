import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, FormLayout } from 'ng-devui';
import { DiJob } from 'src/app/@core/data/datadev.data';
import { JobService } from 'src/app/@core/services/datadev/job.service';

@Component({
  selector: 'app-job-propertity',
  templateUrl: './job-propertity.component.html',
  styleUrls: ['../workbench.component.scss'],
})
export class JobPropertityComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  job: DiJob;
  formLayout = FormLayout.Horizontal;

  formData = {
    jobId: null,
    jobAttr: null,
    jobProp: null,
    engineProp: null,
  };
  constructor(private elr: ElementRef, private translate: TranslateService, private jobService: JobService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.job = this.data.item;
    this.jobService.listJobAttr(this.job.id).subscribe((d) => {
      this.formData = d;
    });
  }

  submitForm({ valid }) {
    if (valid) {
      this.jobService.saveJobAttr(this.formData).subscribe((d) => {
        if (d.success) {
          this.data.refresh(d.data);
          this.data.onClose();
        }
      });
    }
  }
}
