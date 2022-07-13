import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout, IFileOptions, IUploadOptions} from 'ng-devui';
import {ArtifactService} from "../../../../@core/services/job/artifact.service";
import {FlinkArtifactUploadParam} from "../../../../@core/data/job.data";

@Component({
  selector: 'app-job-artifact-upload',
  templateUrl: './artifact-upload.component.html',
  styleUrls: ['../artifact.component.scss'],
})
export class ArtifactUploadComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    nameRules: {
      validators: [
        {required: true},
        {maxlength: 30}
      ],
    },
    entryClassRules: {
      validators: [
        {required: true},
        {maxlength: 30}
      ],
    },
    fileRules: {
      validators: [{required: true}],
    },
    remarkRules: {
      validators: [{maxlength: 200}],
    },
  };

  fileOptions: IFileOptions = {
    multiple: false,
  };
  uploadedFiles: Array<Object> = [];
  uploadOptions: IUploadOptions = {
    uri: '',
  };
  successFlag = false;
  file = null;

  formData = {
    name: null,
    entryClass: null,
    file: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private artifactService: ArtifactService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({valid}) {
    let uploadParam: FlinkArtifactUploadParam = {
      name: this.formData.name || '',
      entryClass: this.formData.entryClass || '',
      file: this.file,
      remark: this.formData.remark || '',
    };
    if (valid && this.file) {
      this.artifactService.upload(uploadParam).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  deleteUploadedFile(file) {
    this.file = null
  }

  onFileSelect(result) {
    this.file = result
  }

  close(event) {
    this.data.onClose(event);
  }
}
