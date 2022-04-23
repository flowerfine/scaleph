import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DFormGroupRuleDirective, DValidateRules, FormLayout } from 'ng-devui';
import { DiDirectory } from 'src/app/@core/data/studio.data';
import { DiDirectoryService } from 'src/app/@core/services/di-directory.service';

@Component({
  selector: 'app-directory-update',
  templateUrl: './directory-update.component.html',
  styleUrls: ['../job.component.scss'],
})
export class DirectoryUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  @ViewChild('form') formDir: DFormGroupRuleDirective;
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
  constructor(private elr: ElementRef, private translate: TranslateService, private directoryService: DiDirectoryService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData.directoryName = this.data.item?.data?.title;
  }

  submitForm({ valid }) {
    let dir: DiDirectory = {
      id: this.data.item?.id,
      directoryName: this.formData.directoryName,
    };
    if (valid) {
      this.directoryService.update(dir).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh({ id: dir.id, parentId: null, title: dir.directoryName });
        }
      });
    }
  }
}
