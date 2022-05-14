import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { MetaDataSetType } from 'src/app/@core/data/stdata.data';
import { RefdataService } from 'src/app/@core/services/stdata/refdata.service';

@Component({
  selector: 'app-refdata-type-update',
  templateUrl: './refdata-type-update.component.html',
  styleUrls: ['../refdata.component.scss'],
})
export class RefdataTypeUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    dataSetTypeCodeRules: {
      validators: [
        { required: true },
        { maxlength: 32 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    dataSetTypeNameRules: {
      validators: [{ required: true }, { maxlength: 120 }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    id: null,
    dataSetTypeCode: null,
    dataSetTypeName: null,
    remark: null,
  };
  constructor(private elr: ElementRef, private translate: TranslateService, private refdataService: RefdataService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      dataSetTypeCode: this.data.item.dataSetTypeCode,
      dataSetTypeName: this.data.item.dataSetTypeName,
      remark: this.data.item.remark,
    };
  }

  submitForm({ valid }) {
    let ds: MetaDataSetType = {
      id: this.formData.id,
      dataSetTypeCode: this.formData.dataSetTypeCode,
      dataSetTypeName: this.formData.dataSetTypeName,
      remark: this.formData.remark,
    };
    if (valid) {
      this.refdataService.updateType(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
