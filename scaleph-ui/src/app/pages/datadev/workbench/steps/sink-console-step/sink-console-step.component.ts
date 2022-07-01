import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { DiJobStepAttr, STEP_ATTR_TYPE } from 'src/app/@core/data/datadev.data';
import { JobService } from 'src/app/@core/services/datadev/job.service';
import { CustomValidate } from 'src/app/@core/validate/CustomValidate';

@Component({
  selector: 'app-sink-console-step',
  templateUrl: './sink-console-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SinkConsoleStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();

  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';
  defaultBatchSize: number = 1024;

  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    stepTitleRules: {
      validators: [{ required: true }],
    },
  };

  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    limit: [null],
  });

  constructor(private fb: FormBuilder, private translate: TranslateService, private jobService: JobService) {}

  ngOnInit(): void {
    this.jobId = this.data.jobId;
    this.stepCode = this.data.item?.id;
    this.stepTitle = this.data.item?.data.title;
    this.formGroup.patchValue({ stepTitle: this.stepTitle });
    this.jobService.listStepAttr(this.jobId, this.stepCode).subscribe((d) => {
      let list: DiJobStepAttr[] = d;
      let stepAttrMap: Map<string, string> = new Map();
      for (const stepAttr of list) {
        stepAttrMap.set(stepAttr.stepAttrKey, stepAttr.stepAttrValue);
      }
      this.formGroup.patchValue({ limit: stepAttrMap.get(STEP_ATTR_TYPE.limit) });
    });
  }

  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.limit, this.formGroup.get(STEP_ATTR_TYPE.limit).value);
      this.onSave.emit(stepAttrMap);
    }
  }
}
