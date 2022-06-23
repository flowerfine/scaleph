import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout, IFileOptions, IUploadOptions} from 'ng-devui';
import {FlinkDeployConfigUploadParam} from "../../../../@core/data/flink.data";
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";

@Component({
  selector: 'app-project-new',
  templateUrl: './deploy-config-upload.component.html',
  styleUrls: ['../deploy-config.component.scss'],
})
export class DeployConfigUploadComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    versionRules: {
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
    multiple: true,
  };
  uploadedFiles: Array<Object> = [];
  uploadOptions: IUploadOptions = {
    uri: '',
  };
  files = null;

  formData = {
    configType: null,
    name: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private deployConfigService: DeployConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({valid}) {
    let uploadParam: FlinkDeployConfigUploadParam = {
      configType: this.formData.configType,
      files: this.files,
      name: this.formData.name,
      remark: this.formData.remark,
    };
    if (valid && this.files) {
      this.deployConfigService.upload(uploadParam).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  deleteUploadedFile(files) {
    this.files = null
  }

  onFileSelect(result) {
    console.log(result)
    this.files = result
  }

  close(event) {
    this.data.onClose(event);
  }
}
