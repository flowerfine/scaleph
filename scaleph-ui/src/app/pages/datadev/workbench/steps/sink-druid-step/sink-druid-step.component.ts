import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout} from 'ng-devui';
import {DiJobStepAttr, STEP_ATTR_TYPE} from 'src/app/@core/data/datadev.data';
import {JobService} from 'src/app/@core/services/datadev/job.service';
import {CustomValidate} from 'src/app/@core/validate/CustomValidate';

@Component({
  selector: 'app-sink-druid-step',
  templateUrl: './sink-druid-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SinkDruidStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();
  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';

  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    stepTitleRules: {
      validators: [{required: true}],
    },
    coordinatorUrlRules: {
      validators: [{required: true}],
    },
    datasourceNameRules: {
      validators: [{required: false}],
    },
  };
  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    dataSource: [null],
    coordinator_url: [null],
    datasourceName: [null],
    timestamp_column: [null],
    timestamp_format: [null],
    timestamp_missing_value: [null],
    parallelism: [null],
  });

  constructor(
    private fb: FormBuilder,
    private translate: TranslateService,
    private jobService: JobService
  ) {
  }

  ngOnInit(): void {
    this.jobId = this.data.jobId;
    this.stepCode = this.data.item?.id;
    this.stepTitle = this.data.item?.data.title;
    this.formGroup.patchValue({stepTitle: this.stepTitle});
    this.jobService.listStepAttr(this.jobId, this.stepCode).subscribe((d) => {
      let list: DiJobStepAttr[] = d;
      let stepAttrMap: Map<string, string> = new Map();
      for (const stepAttr of list) {
        stepAttrMap.set(stepAttr.stepAttrKey, stepAttr.stepAttrValue);
      }
      if (stepAttrMap.get(STEP_ATTR_TYPE.dataSource)) {
        this.formGroup.patchValue({dataSource: JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.dataSource))});
      }
      this.formGroup.patchValue({coordinator_url: stepAttrMap.get(STEP_ATTR_TYPE.coordinatorUrl)});
      this.formGroup.patchValue({datasourceName: stepAttrMap.get(STEP_ATTR_TYPE.datasourceName)});
      this.formGroup.patchValue({timestamp_column: stepAttrMap.get(STEP_ATTR_TYPE.timestampColumn)});
      this.formGroup.patchValue({timestamp_format: stepAttrMap.get(STEP_ATTR_TYPE.timestampFormat)});
      this.formGroup.patchValue({timestamp_missing_value: stepAttrMap.get(STEP_ATTR_TYPE.timestampMissingValue)});
      if (stepAttrMap.get(STEP_ATTR_TYPE.parallelism)) {
        this.formGroup.patchValue({parallelism: stepAttrMap.get(STEP_ATTR_TYPE.parallelism)});
      } else {
        this.formGroup.patchValue({parallelism: 1});
      }
    });
  }

  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.coordinatorUrl, this.formGroup.get(STEP_ATTR_TYPE.coordinatorUrl).value);
      stepAttrMap.set(STEP_ATTR_TYPE.datasourceName, this.formGroup.get(STEP_ATTR_TYPE.datasourceName).value);
      stepAttrMap.set(STEP_ATTR_TYPE.timestampColumn, this.formGroup.get(STEP_ATTR_TYPE.timestampColumn).value);
      stepAttrMap.set(STEP_ATTR_TYPE.timestampFormat, this.formGroup.get(STEP_ATTR_TYPE.timestampFormat).value);
      stepAttrMap.set(STEP_ATTR_TYPE.timestampMissingValue, this.formGroup.get(STEP_ATTR_TYPE.timestampMissingValue).value);
      stepAttrMap.set(STEP_ATTR_TYPE.parallelism, this.formGroup.get(STEP_ATTR_TYPE.parallelism).value);
      this.onSave.emit(stepAttrMap);
    }
  }
}
