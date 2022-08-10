import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DValidateRules, FormLayout, IFileOptions, IUploadOptions} from 'ng-devui';
import {Dict, DICT_TYPE} from "../../../../@core/data/app.data";
import {SysDictDataService} from "../../../../@core/services/admin/dict-data.service";
import {SeaTunnelReleaseUploadParam} from "../../../../@core/data/resource.data";
import {SeatunnelReleaseService} from "../../../../@core/services/resource/seatunnel-release.service";

@Component({
  selector: 'app-seatunnel-release-upload',
  templateUrl: './release-seatunnel-upload.component.html',
  styleUrls: ['../release-seatunnel.component.scss'],
})
export class ReleaseSeatunnelUploadComponent implements OnInit {
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
  seatunnelVersionList: Dict[] = []
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
    version: null,
    file: null,
    remark: null,
  };

  constructor(private elr: ElementRef, private translate: TranslateService, private dictDataService: SysDictDataService, private releaseSeaTunnelService: SeatunnelReleaseService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.seatunnelVersion).subscribe((d) => {
      this.seatunnelVersionList = d;
    });
  }

  submitForm({valid}) {
    let uploadParam: SeaTunnelReleaseUploadParam = {
      version: this.formData.version ? this.formData.version.value : '',
      file: this.file,
      remark: this.formData.remark || '',
    };
    if (valid && this.file) {
      this.releaseSeaTunnelService.upload(uploadParam).subscribe((d) => {
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
