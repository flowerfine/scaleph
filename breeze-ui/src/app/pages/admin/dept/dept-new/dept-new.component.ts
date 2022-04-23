import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { Dept } from 'src/app/@core/data/admin.data';
import { DeptService } from 'src/app/@core/services/dept.service';

@Component({
  selector: 'app-dept-new',
  templateUrl: './dept-new.component.html',
  styleUrls: ['../dept.component.scss'],
})
export class DeptNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    deptCodeRules: {
      validators: [
        { required: true },
        { maxlength: 36 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    roleNameRules: {
      validators: [{ required: true }, { maxlength: 60 }],
    },
    pidRules: {},
  };

  formData: Dept = {
    deptCode: null,
    deptName: null,
    pid: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private deptService: DeptService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData.pid = this.data.item?.data?.title;
  }

  submitForm({ valid }) {
    if (valid) {
      let dept: Dept = {
        deptCode: this.formData.deptCode,
        deptName: this.formData.deptName,
        pid: this.data.item?.id || undefined,
      };
      this.deptService.add(dept).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh({ id: d.data, parentId: dept.pid, title: dept.deptName });
        }
      });
    }
  }
}
