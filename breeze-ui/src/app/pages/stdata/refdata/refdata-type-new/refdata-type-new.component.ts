import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { MetaDataSetType } from 'src/app/@core/data/meta.data';
import { RefdataService } from 'src/app/@core/services/refdata.service';

@Component({
  selector: 'app-refdata-type-new',
  templateUrl: './refdata-type-new.component.html',
  styleUrls: ['../refdata.component.scss'],
})
export class RefdataTypeNewComponent implements OnInit {
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
    dataSetTypeCode: null,
    dataSetTypeName: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private refdataService: RefdataService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({ valid }) {
    let ds: MetaDataSetType = {
      dataSetTypeCode: this.formData.dataSetTypeCode,
      dataSetTypeName: this.formData.dataSetTypeName,
      remark: this.formData.remark,
    };
    if (valid) {
      this.refdataService.addType(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
