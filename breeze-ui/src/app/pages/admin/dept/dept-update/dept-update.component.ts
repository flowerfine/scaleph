import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { Dept } from 'src/app/@core/data/admin.data';
import { DeptService } from 'src/app/@core/services/dept.service';

@Component({
  selector: 'app-dept-update',
  templateUrl: './dept-update.component.html',
  styleUrls: ['../dept.component.scss'],
})
export class DeptUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    // deptCodeRules: {
    //   validators: [
    //     { required: true },
    //     { maxlength: 36 },
    //     { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
    //   ],
    // },
    roleNameRules: {
      validators: [{ required: true }, { maxlength: 60 }],
    },
    // pidRules: {},
  };

  formData: Dept = {
    id: null,
    deptCode: null,
    deptName: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private deptService: DeptService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData.id = this.data.item?.id;
    // this.formData.deptCode = this.data.item?.data?.originItem.deptCode;
    this.formData.deptName = this.data.item?.data?.title;
  }

  submitForm({ valid }) {
    if (valid) {
      let dept: Dept = {
        id: this.formData.id,
        deptName: this.formData.deptName,
      };
      this.deptService.update(dept).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh({
            id: dept.id,
            parentId: this.data.item?.parentId,
            index: this.data.index,
            data: { children: [], title: dept.deptName },
          });
        }
      });
    }
  }
}
