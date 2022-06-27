import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout, IFileOptions, IUploadOptions} from 'ng-devui';
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";

@Component({
  selector: 'app-deploy-config-file-upload',
  templateUrl: './deploy-config-file-upload.component.html',
  styleUrls: ['../deploy-config-file.component.scss'],
})
export class DeployConfigFileUploadComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: {message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text'},
    fileRules: {
      validators: [{required: true}],
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
    if (valid && this.files) {
      this.deployConfigService.uploadFiles(this.data.flinkDeployConfig.id, this.files).subscribe((d) => {
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
