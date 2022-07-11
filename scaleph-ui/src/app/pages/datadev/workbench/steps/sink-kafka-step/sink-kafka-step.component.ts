import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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
  selector: 'app-sink-kafka-step',
  templateUrl: './sink-kafka-step.component.html',
  styleUrls: ['../../workbench.component.scss'],
})
export class SinkKafkaStepComponent implements OnInit {
  @Input() data;
  @Output() onSave = new EventEmitter<any>();

  jobId: string = '';
  stepCode: string = '';
  stepTitle: string = '';

  dataSourceType: string = 'Kafka';
  dataSourceList: Dict[] = [];
  semanticList: Dict[] = [];

  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    stepTitleRules: {
      validators: [{ required: true }],
    },
    producerBootstrapServersRules: {
      validators: [{ required: true }],
    },
    topicsRules: {
      validators: [{ required: true }],
    },
  };

  formGroup: FormGroup = this.fb.group({
    stepTitle: [null],
    producer_bootstrap_servers: [null],
    topics: [null],
    semantic: [null],
    producer_conf: [null],
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
    this.formGroup.patchValue({ stepTitle: this.stepTitle });
    this.dictDataService.listByType(DICT_TYPE.flinkSemantic).subscribe((d) => {
      this.semanticList = d;
    });
    this.dataSourceService.listByType(this.dataSourceType).subscribe((d) => {
      this.dataSourceList = d;
    });
    this.jobService.listStepAttr(this.jobId, this.stepCode).subscribe((d) => {
      let list: DiJobStepAttr[] = d;
      let stepAttrMap: Map<string, string> = new Map();
      for (const stepAttr of list) {
        stepAttrMap.set(stepAttr.stepAttrKey, stepAttr.stepAttrValue);
      }
      if (stepAttrMap.get(STEP_ATTR_TYPE.producerBootstrapServers)) {
        this.formGroup.patchValue({ producer_bootstrap_servers: JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.producerBootstrapServers)) });
      }
      if (stepAttrMap.get(STEP_ATTR_TYPE.semantic)) {
        this.formGroup.patchValue({ semantic: JSON.parse(stepAttrMap.get(STEP_ATTR_TYPE.semantic)) });
      }
      this.formGroup.patchValue({ topics: stepAttrMap.get(STEP_ATTR_TYPE.topics) });
      this.formGroup.patchValue({ producer_conf: stepAttrMap.get(STEP_ATTR_TYPE.producerConf) });
    });
  }

  submitForm() {
    CustomValidate.validateForm(this.formGroup);
    if (this.formGroup.valid) {
      let stepAttrMap: Map<string, string> = new Map();
      stepAttrMap.set(STEP_ATTR_TYPE.stepTitle, this.formGroup.get(STEP_ATTR_TYPE.stepTitle).value);
      stepAttrMap.set(STEP_ATTR_TYPE.topics, this.formGroup.get(STEP_ATTR_TYPE.topics).value);
      stepAttrMap.set(STEP_ATTR_TYPE.producerBootstrapServers, this.formGroup.get(STEP_ATTR_TYPE.producerBootstrapServers).value);
      stepAttrMap.set(STEP_ATTR_TYPE.producerConf, this.formGroup.get(STEP_ATTR_TYPE.producerConf).value);
      stepAttrMap.set(STEP_ATTR_TYPE.semantic, this.formGroup.get(STEP_ATTR_TYPE.semantic).value);
      this.onSave.emit(stepAttrMap);
    }
  }
}
