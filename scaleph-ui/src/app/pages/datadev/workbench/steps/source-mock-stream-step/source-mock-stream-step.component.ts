import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, DValidateRules, FormLayout, TableExpandConfig } from 'ng-devui';
import { DiJobStepAttr, MockData, MOCK_DATA_TYPE, STEP_ATTR_TYPE } from 'src/app/@core/data/datadev.data';
import { JobService } from 'src/app/@core/services/datadev/job.service';
import { CustomValidate } from 'src/app/@core/validate/CustomValidate';

@Component({
  selector: 'app-source-mock-stream-step',
  templateUrl: './source-mock-stream-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SourceMockStreamStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();
  @ViewChild('dataTable', { static: true }) dataTable: DataTableComponent;
  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';

  mockDataType = MOCK_DATA_TYPE;
  dataTableDs: MockData[] = [];

  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    stepTitleRules: {
      validators: [{ required: true }],
    },
  };

  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    mock_data_interval: [1],
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
      if (stepAttrMap.get(STEP_ATTR_TYPE.mockDataInterval)) {
        this.formGroup.patchValue({ mock_data_interval: stepAttrMap.get(STEP_ATTR_TYPE.mockDataInterval) });
      } else {
        this.formGroup.patchValue({ mock_data_interval: 1 });
      }
      this.dataTableDs = JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.mockDataSchema));
    });
  }

  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.mockDataInterval, this.formGroup.get(STEP_ATTR_TYPE.mockDataInterval).value);
      stepAttrMap.set(STEP_ATTR_TYPE.mockDataSchema, JSON.stringify(this.dataTableDs));
      this.onSave.emit(stepAttrMap);
    }
  }

  deleteMockData(item: any, index: any) {
    this.dataTableDs.splice(index, 1);
  }

  addMockData() {
    let rowData: MockData = {
      name: 'col_new',
      type: MOCK_DATA_TYPE[0],
      minValue: null,
      maxValue: null,
      valueSeed: null,
    };
    this.dataTableDs.push(rowData);
  }
}
