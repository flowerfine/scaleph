import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { Dict } from 'src/app/@core/data/app.data';
import { DiJobStepAttr, STEP_ATTR_TYPE } from 'src/app/@core/data/datadev.data';
import { DataSourceService } from 'src/app/@core/services/datadev/datasource.service';
import { JobService } from 'src/app/@core/services/datadev/job.service';
import { CustomValidate } from 'src/app/@core/validate/CustomValidate';

@Component({
  selector: 'app-sink-elasticsearch-step',
  templateUrl: './sink-elasticsearch-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SinkElasticsearchStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();
  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';

  dataSourceType: string = 'Elasticsearch';
  dataSourceList: Dict[] = [];

  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    stepTitleRules: {
      validators: [{ required: true }],
    },
    dataSourceRules: {
      validators: [{ required: true }],
    },
    indexRules: {
      validators: [{ required: false }],
    },
  };
  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    dataSource: [null],
    index: [null],
    index_time_format: [null],
  });

  constructor(
    private fb: FormBuilder,
    private translate: TranslateService,
    private dataSourceService: DataSourceService,
    private jobService: JobService
  ) {}

  ngOnInit(): void {
    this.jobId = this.data.jobId;
    this.stepCode = this.data.item?.id;
    this.stepTitle = this.data.item?.data.title;
    this.formGroup.patchValue({ stepTitle: this.stepTitle });
    this.dataSourceService.listByType(this.dataSourceType).subscribe((d) => {
      this.dataSourceList = d;
    });
    this.jobService.listStepAttr(this.jobId, this.stepCode).subscribe((d) => {
      let list: DiJobStepAttr[] = d;
      let stepAttrMap: Map<string, string> = new Map();
      for (const stepAttr of list) {
        stepAttrMap.set(stepAttr.stepAttrKey, stepAttr.stepAttrValue);
      }
      if (stepAttrMap.get(STEP_ATTR_TYPE.dataSource)) {
        this.formGroup.patchValue({ dataSource: JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.dataSource)) });
      }
      this.formGroup.patchValue({ index: stepAttrMap.get(STEP_ATTR_TYPE.index) });
      this.formGroup.patchValue({ index_time_format: stepAttrMap.get(STEP_ATTR_TYPE.indexTimeFormat) });
    });
  }
  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.dataSource, this.formGroup.get(STEP_ATTR_TYPE.dataSource).value);
      stepAttrMap.set(STEP_ATTR_TYPE.index, this.formGroup.get(STEP_ATTR_TYPE.index).value);
      stepAttrMap.set(STEP_ATTR_TYPE.indexTimeFormat, this.formGroup.get(STEP_ATTR_TYPE.indexTimeFormat).value);
      this.onSave.emit(stepAttrMap);
    }
  }
}
