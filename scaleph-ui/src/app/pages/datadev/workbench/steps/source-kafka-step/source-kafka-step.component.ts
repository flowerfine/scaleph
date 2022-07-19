import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { Dict } from 'src/app/@core/data/app.data';
import { DiJobStepAttr, STEP_ATTR_TYPE } from 'src/app/@core/data/datadev.data';
import { SysDictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { DataSourceService } from 'src/app/@core/services/datadev/datasource.service';
import { JobService } from 'src/app/@core/services/datadev/job.service';
import { CustomValidate } from 'src/app/@core/validate/CustomValidate';

@Component({
  selector: 'app-source-kafka-step',
  templateUrl: './source-kafka-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SourceKafkaStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();

  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';

  dataSourceType: string = 'kafka';
  dataSourceList: Dict[] = [];
  offsetResetOptions: string[] = ['latest', 'earliest', 'specific'];
  formatTypeOptions: string[] = ['json', 'csv', 'avro'];

  specificShow: boolean = false;
  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    stepTitleRules: {
      validators: [{ required: true }],
    },
    consumerBootstrapServersRules: {
      validators: [{ required: true }],
    },
    topicsRules: {
      validators: [{ required: true }],
    },
    consumerGroupIdRules: {
      validators: [{ required: true }],
    },
    formatTypeRules: {
      validators: [{ required: true }],
    },
    schemaRules: {
      validators: [{ required: true }],
    },
  };

  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    consumer_bootstrap_servers: [null],
    topics: [null],
    consumer_group_id: [null],
    consumer_conf: [null],
    offset_reset: [null],
    offset_reset_specific: [null],
    rowtime_field: [null],
    watermark: [null],
    format_type: [null],
    format_conf: [null],
    schema: [null],
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
      if (stepAttrMap.get(STEP_ATTR_TYPE.consumerBootstrapServers)) {
        this.formGroup.patchValue({ consumer_bootstrap_servers: JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.consumerBootstrapServers)) });
      }
      this.formGroup.patchValue({ topics: stepAttrMap.get(STEP_ATTR_TYPE.topics) });
      this.formGroup.patchValue({ consumer_group_id: stepAttrMap.get(STEP_ATTR_TYPE.consumerGroupId) });
      this.formGroup.patchValue({ consumer_conf: stepAttrMap.get(STEP_ATTR_TYPE.consumerConf) });
      this.formGroup.patchValue({ offset_reset: stepAttrMap.get(STEP_ATTR_TYPE.offsetReset) });
      this.formGroup.patchValue({ offset_reset_specific: stepAttrMap.get(STEP_ATTR_TYPE.offsetResetSpecific) });
      this.formGroup.patchValue({ rowtime_field: stepAttrMap.get(STEP_ATTR_TYPE.rowtimeField) });
      this.formGroup.patchValue({ watermark: stepAttrMap.get(STEP_ATTR_TYPE.watermark) });
      this.formGroup.patchValue({ format_type: stepAttrMap.get(STEP_ATTR_TYPE.formatType) });
      this.formGroup.patchValue({ format_conf: stepAttrMap.get(STEP_ATTR_TYPE.formatConf) });
      this.formGroup.patchValue({ schema: stepAttrMap.get(STEP_ATTR_TYPE.schema) });
    });
  }

  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.consumerBootstrapServers, this.formGroup.get(STEP_ATTR_TYPE.consumerBootstrapServers).value);
      stepAttrMap.set(STEP_ATTR_TYPE.topics, this.formGroup.get(STEP_ATTR_TYPE.topics).value);
      stepAttrMap.set(STEP_ATTR_TYPE.consumerGroupId, this.formGroup.get(STEP_ATTR_TYPE.consumerGroupId).value);
      stepAttrMap.set(STEP_ATTR_TYPE.consumerConf, this.formGroup.get(STEP_ATTR_TYPE.consumerConf).value);
      stepAttrMap.set(STEP_ATTR_TYPE.offsetReset, this.formGroup.get(STEP_ATTR_TYPE.offsetReset).value);
      stepAttrMap.set(STEP_ATTR_TYPE.offsetResetSpecific, this.formGroup.get(STEP_ATTR_TYPE.offsetResetSpecific).value);
      stepAttrMap.set(STEP_ATTR_TYPE.rowtimeField, this.formGroup.get(STEP_ATTR_TYPE.rowtimeField).value);
      stepAttrMap.set(STEP_ATTR_TYPE.watermark, this.formGroup.get(STEP_ATTR_TYPE.watermark).value);
      stepAttrMap.set(STEP_ATTR_TYPE.formatType, this.formGroup.get(STEP_ATTR_TYPE.formatType).value);
      stepAttrMap.set(STEP_ATTR_TYPE.formatConf, this.formGroup.get(STEP_ATTR_TYPE.formatConf).value);
      stepAttrMap.set(STEP_ATTR_TYPE.schema, this.formGroup.get(STEP_ATTR_TYPE.schema).value);
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
