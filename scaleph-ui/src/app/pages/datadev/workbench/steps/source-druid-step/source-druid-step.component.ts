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
  selector: 'app-source-druid-step',
  templateUrl: './source-druid-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SourceDruidStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();

  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';

  dataSourceType: string = 'Druid';
  dataSourceList: Dict[] = [];

  specificShow: boolean = false;
  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    stepTitleRules: {
      validators: [{ required: true }],
    },
    dataSourceRules: {
      validators: [{ required: true }],
    },
    datasourceNameRules: {
      validators: [{ required: true }],
    },
    startDateRules: {
      validators: [{ required: false }],
    },
    endDateRules: {
      validators: [{ required: false }],
    }
  };

  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    dataSource: [null],
    datasourceName: [null],
    start_date: [null],
    end_date: [null],
    columns: [null],
    parallelism: [null],
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
      this.formGroup.patchValue({ datasourceName: stepAttrMap.get(STEP_ATTR_TYPE.datasourceName) });
      this.formGroup.patchValue({ start_date: stepAttrMap.get(STEP_ATTR_TYPE.startDate) });
      this.formGroup.patchValue({ end_date: stepAttrMap.get(STEP_ATTR_TYPE.endDate) });
      this.formGroup.patchValue({ columns: stepAttrMap.get(STEP_ATTR_TYPE.columns) });
      if (stepAttrMap.get(STEP_ATTR_TYPE.parallelism)) {
        this.formGroup.patchValue({ parallelism: stepAttrMap.get(STEP_ATTR_TYPE.parallelism) });
      } else {
        this.formGroup.patchValue({ parallelism: 1 });
      }
    });
  }

  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.dataSource, this.formGroup.get(STEP_ATTR_TYPE.dataSource).value);
      stepAttrMap.set(STEP_ATTR_TYPE.datasourceName, this.formGroup.get(STEP_ATTR_TYPE.datasourceName).value);
      stepAttrMap.set(STEP_ATTR_TYPE.startDate, this.formGroup.get(STEP_ATTR_TYPE.startDate).value);
      stepAttrMap.set(STEP_ATTR_TYPE.endDate, this.formGroup.get(STEP_ATTR_TYPE.endDate).value);
      stepAttrMap.set(STEP_ATTR_TYPE.columns, this.formGroup.get(STEP_ATTR_TYPE.columns).value);
      stepAttrMap.set(STEP_ATTR_TYPE.parallelism, this.formGroup.get(STEP_ATTR_TYPE.parallelism).value);
      this.onSave.emit(stepAttrMap);
    }
  }

  offsetResetChange(event: any) {
    if (event == 'specific') {
      this.specificShow = true;
    } else {
      this.specificShow = false;
    }
  }
}
