import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout, IFileOptions, IUploadOptions} from 'ng-devui';
import {ClusterCredentialService} from "../../../../../@core/services/resource/cluster-credential.service";

@Component({
  selector: 'app-cluster-credential-file-upload',
  templateUrl: './cluster-credential-file-upload.component.html',
  styleUrls: ['../cluster-credential-file.component.scss'],
})
export class ClusterCredentialFileUploadComponent implements OnInit {
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

  constructor(private elr: ElementRef, private translate: TranslateService, private clusterCredentialService: ClusterCredentialService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  submitForm({valid}) {
    if (valid && this.files) {
      this.clusterCredentialService.uploadFiles(this.data.flinkDeployConfig.id, this.files).subscribe((d) => {
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
    this.files = result
  }

  close(event) {
    this.data.onClose(event);
  }
}
