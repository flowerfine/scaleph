import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { DiProject } from 'src/app/@core/data/datadev.data';
import { ProjectService } from 'src/app/@core/services/datadev/project.service';

@Component({
  selector: 'app-project-update',
  templateUrl: './project-update.component.html',
  styleUrls: ['../project.component.scss'],
})
export class ProjectUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
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
    id: null,
    projectCode: null,
    projectName: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private projectService: ProjectService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      projectCode: this.data.item.projectCode,
      projectName: this.data.item.projectName,
      remark: this.data.item.remark,
    };
  }

  submitForm({ valid }) {
    let ds: DiProject = {
      id: this.formData.id,
      projectCode: this.formData.projectCode,
      projectName: this.formData.projectName,
      remark: this.formData.remark,
    };
    if (valid) {
      this.projectService.update(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
