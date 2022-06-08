import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { Dict, DICT_TYPE } from 'src/app/@core/data/app.data';
import { DiJobStepAttr, STEP_ATTR_TYPE } from 'src/app/@core/data/datadev.data';
import { SysDictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { DataSourceService } from 'src/app/@core/services/datadev/datasource.service';
import { JobService } from 'src/app/@core/services/datadev/job.service';
import { CustomValidate } from 'src/app/@core/validate/CustomValidate';

@Component({
  selector: 'app-source-table-step',
  templateUrl: './source-table-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SourceTableStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();
  dataSourceTypeList: Dict[] = [];
  dataSourceList: Dict[] = [];

  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';

  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    stepTitleRules: {
      validators: [{ required: true }],
    },
    dataSourceTypeRules: {
      validators: [{ required: true }],
    },
    dataSourceRules: {
      validators: [{ required: true }],
    },
    queryRules: {
      validators: [{ required: true }],
    },
  };

  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    dataSourceType: [null],
    dataSource: [null],
    query: [null],
  });

  constructor(
    private fb: FormBuilder,
    private translate: TranslateService,
    private dataSourceService: DataSourceService,
    private dictDataService: SysDictDataService,
    private jobService: JobService
  ) {}

  ngOnInit(): void {
    this.jobId = this.data.jobId;
    this.stepCode = this.data.item?.id;
    this.stepTitle = this.data.item?.data.title;
    this.dictDataService.listByType(DICT_TYPE.datasourceType).subscribe((d) => {
      this.dataSourceTypeList = d;
    });
    this.formGroup.patchValue({ stepTitle: this.stepTitle });
    this.jobService.listStepAttr(this.jobId, this.stepCode).subscribe((d) => {
      let list: DiJobStepAttr[] = d;
      let stepAttrMap: Map<string, string> = new Map();
      for (const stepAttr of list) {
        stepAttrMap.set(stepAttr.stepAttrKey, stepAttr.stepAttrValue);
      }
      if (stepAttrMap.get(STEP_ATTR_TYPE.dataSourceType)) {
        this.formGroup.patchValue({ dataSourceType: JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.dataSourceType)) });
        this.listDataSource(this.formGroup.get(STEP_ATTR_TYPE.dataSourceType).value, false);
      }
      if (stepAttrMap.get(STEP_ATTR_TYPE.dataSource)) {
        this.formGroup.patchValue({ dataSource: JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.dataSource)) });
      }
      this.formGroup.patchValue({ query: stepAttrMap.get(STEP_ATTR_TYPE.query) });
    });
  }

  listDataSource(event: Dict, reset: boolean) {
    if (reset) {
      this.formGroup.get(STEP_ATTR_TYPE.dataSource).setValue(null);
    }
    this.dataSourceService.listByType(event?.value).subscribe((d) => {
      this.dataSourceList = d;
    });
  }

  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.dataSourceType, this.formGroup.get(STEP_ATTR_TYPE.dataSourceType).value);
      stepAttrMap.set(STEP_ATTR_TYPE.dataSource, this.formGroup.get(STEP_ATTR_TYPE.dataSource).value);
      stepAttrMap.set(STEP_ATTR_TYPE.query, this.formGroup.get(STEP_ATTR_TYPE.query).value);
      this.onSave.emit(stepAttrMap);
    }
  }
}
