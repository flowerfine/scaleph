import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { DiDirectory } from 'src/app/@core/data/datadev.data';
import { DirectoryService } from 'src/app/@core/services/datadev/directory.service';

@Component({
  selector: 'app-directory-new',
  templateUrl: './directory-new.component.html',
  styleUrls: ['../job.component.scss'],
})
export class DirectoryNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
  projectId: number;
  pid: number;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    directoryNameRules: {
      validators: [{ required: true }, { maxlength: 30 }],
    },
  };

  formData = {
    directoryName: null,
  };
  constructor(private elr: ElementRef, private translate: TranslateService, private directoryService: DirectoryService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.pid = this.data.item?.id;
    this.projectId = this.data.projectId;
  }

  submitForm({ valid }) {
    let dir: DiDirectory = {
      projectId: this.projectId,
      pid: this.pid,
      directoryName: this.formData.directoryName,
    };
    if (valid) {
      this.directoryService.add(dir).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh({ id: d.data, parentId: dir.pid, title: dir.directoryName });
        }
      });
    }
  }
}
