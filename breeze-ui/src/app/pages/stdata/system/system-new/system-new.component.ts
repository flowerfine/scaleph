import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { MetaSystem } from 'src/app/@core/data/stdata.data';
import { SystemService } from 'src/app/@core/services/stdata/system.service';

@Component({
  selector: 'app-system-new',
  templateUrl: './system-new.component.html',
  styleUrls: ['../system.component.scss'],
})
export class SystemNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    systemCodeRules: {
      validators: [
        { required: true },
        { maxlength: 60 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    systemNameRules: {
      validators: [{ required: true }, { maxlength: 120 }],
    },
    contactsRules: {
      validators: [{ maxlength: 20 }],
    },
    contactsPhone: {
      validators: [{ maxlength: 15 }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    systemCode: null,
    systemName: null,
    contacts: null,
    contactsPhone: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private metaSystemService: SystemService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({ valid }) {
    let ds: MetaSystem = {
      systemCode: this.formData.systemCode,
      systemName: this.formData.systemName,
      contacts: this.formData.contacts,
      contactsPhone: this.formData.contactsPhone,
      remark: this.formData.remark,
    };
    if (valid) {
      this.metaSystemService.add(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
