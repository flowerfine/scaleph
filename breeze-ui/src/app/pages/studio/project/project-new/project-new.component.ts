import { Input } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { DiProject } from 'src/app/@core/data/studio.data';
import { DiProjectService } from 'src/app/@core/services/di-project.service';

@Component({
  selector: 'app-project-new',
  templateUrl: './project-new.component.html',
  styleUrls: ['../project.component.scss'],
})
export class ProjectNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    projectCodeRules: {
      validators: [
        { required: true },
        { maxlength: 30 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
    },
    projectNameRules: {
      validators: [{ required: true }, { maxlength: 60 }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    projectCode: null,
    projectName: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private projectService: DiProjectService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({ valid }) {
    let ds: DiProject = {
      projectCode: this.formData.projectCode,
      projectName: this.formData.projectName,
      remark: this.formData.remark,
    };
    if (valid) {
      this.projectService.add(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
