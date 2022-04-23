import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { MetaSystem } from 'src/app/@core/data/meta.data';
import { MetaSystemService } from 'src/app/@core/services/meta-system.service';

@Component({
  selector: 'app-system-update',
  templateUrl: './system-update.component.html',
  styleUrls: ['../system.component.scss'],
})
export class SystemUpdateComponent implements OnInit {
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
    id: null,
    systemCode: null,
    systemName: null,
    contacts: null,
    contactsPhone: null,
    remark: null,
  };
  constructor(private elr: ElementRef, private translate: TranslateService, private metaSystemService: MetaSystemService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      systemCode: this.data.item.systemCode,
      systemName: this.data.item.systemName,
      contacts: this.data.item.contacts,
      contactsPhone: this.data.item.contactsPhone,
      remark: this.data.item.remark,
    };
  }

  submitForm({ valid }) {
    let ds: MetaSystem = {
      id: this.formData.id,
      systemCode: this.formData.systemCode,
      systemName: this.formData.systemName,
      contacts: this.formData.contacts,
      contactsPhone: this.formData.contactsPhone,
      remark: this.formData.remark,
    };
    if (valid) {
      this.metaSystemService.update(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
