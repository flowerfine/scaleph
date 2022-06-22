import { Input } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { IFileOptions, IUploadOptions, SingleUploadComponent } from 'ng-devui';
import { DValidateRules, FormLayout } from 'ng-devui';
import {ReleaseService} from "../../../../@core/services/flink/release.service";
import {FlinkReleaseUploadParam} from "../../../../@core/data/flink.data";

@Component({
  selector: 'app-project-new',
  templateUrl: './release-upload.component.html',
  styleUrls: ['../release.component.scss'],
})
export class ReleaseUploadComponent implements OnInit {
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
    version: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private releaseService: ReleaseService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({ valid }) {
    let ds: FlinkReleaseUploadParam = {
      version: this.formData.version,
      remark: this.formData.remark,
    };
    if (valid) {
      this.releaseService.add(ds).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
