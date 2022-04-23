import { DOCUMENT } from '@angular/common';
import { Component, ElementRef, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout, LoadingService } from 'ng-devui';
import { Dict } from 'src/app/@core/data/app.data';
import { DiClusterService } from 'src/app/@core/services/di-cluster.service';
import { DiJobService } from 'src/app/@core/services/di-job.service';
import { DiResourceFileService } from 'src/app/@core/services/di-resource.service';

@Component({
  selector: 'app-job-start',
  templateUrl: './job-start.component.html',
  styleUrls: ['../job.component.scss'],
})
export class JobStartComponent implements OnInit {
  parent: HTMLElement;
  dataLoading: boolean = false;
  loadTarget: any;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  resourceFileList: Dict[] = [];
  clusterList: Dict[] = [];
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    clusterRules: {
      validators: [{ required: true }],
    },
  };

  formData = {
    cluster: null,
    resources: null,
  };

  constructor(
    private elr: ElementRef,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private jobService: DiJobService,
    private resourceService: DiResourceFileService,
    private clusterService: DiClusterService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.loadClusterList();
    this.loadResourceFileList();
    this.jobService.listResource(this.data.item?.id).subscribe((d) => {
      this.formData.resources = d;
    });
  }

  loadClusterList(): void {
    this.clusterService.listAll().subscribe((d) => {
      this.clusterList = d;
      let dict: Dict[] = this.clusterList.filter((d) => {
        return d.value == this.data.item?.clusterId;
      });
      this.formData.cluster = dict[0];
    });
  }
  loadResourceFileList(): void {
    this.resourceService.listByProjectId(this.data.item.projectId).subscribe((d) => {
      this.resourceFileList = d;
    });
  }

  submitForm({ valid }) {
    let info = {
      jobId: this.data.item?.id,
      clusterId: this.formData.cluster?.value,
      resources: this.formData.resources,
    };
    if (valid) {
      this.openRunJobLoading();
      this.jobService.run(info).subscribe((d) => {
        if (d.success) {
          this.data.refresh();
        }
        this.dataLoading = false;
        this.data.onClose();
      });
    }
  }

  openRunJobLoading() {
    const dc = this.doc.querySelector('#loadTarget');
    this.loadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.dataLoading = true;
  }
}
