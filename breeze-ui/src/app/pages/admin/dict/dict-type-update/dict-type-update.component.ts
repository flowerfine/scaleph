import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { DictType } from 'src/app/@core/data/admin.data';
import { DictTypeService } from 'src/app/@core/services/admin/dict-type.service';

@Component({
  selector: 'app-dict-type-update',
  templateUrl: './dict-type-update.component.html',
  styleUrls: ['../dict.component.scss'],
})
export class DictTypeUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout=FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    id: {},
    dictTypeCodeRules: {
      validators: [
        { required: true },
        { maxlength: 30 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    dictTypeNameRules: {
      validators: [{ required: true }, { maxlength: 100 }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData: DictType = {
    id: null,
    dictTypeCode: null,
    dictTypeName: null,
    remark: null,
  };
  constructor(private dictTypeService: DictTypeService, private elr: ElementRef, private translate: TranslateService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      dictTypeCode: this.data.item.dictTypeCode,
      dictTypeName: this.data.item.dictTypeName,
      remark: this.data.item.remark,
    };
  }

  submitForm({ valid }) {
    if (valid) {
      this.dictTypeService.update(this.formData).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
