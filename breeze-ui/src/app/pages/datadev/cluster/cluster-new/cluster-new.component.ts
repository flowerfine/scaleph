import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { Dict, DICT_TYPE } from 'src/app/@core/data/app.data';
import { DictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { ClusterService } from 'src/app/@core/services/datadev/cluster.service';

@Component({
  selector: 'app-cluster-new',
  templateUrl: './cluster-new.component.html',
  styleUrls: ['../cluster.component.scss'],
})
export class ClusterNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  formLayout = FormLayout.Horizontal;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    clusterNameRules: {
      validators: [{ required: true }, { maxlength: 128 }],
    },
    clusterTypeRules: {
      validators: [{ required: true }],
    },
    clusterHomeRules: {
      validators: [{ maxlength: 256 }],
    },
    clusterVersionRules: {
      validators: [{ maxlength: 60 }],
    },
    clusterConfRules: {
      validators: [{ required: true }],
    },
    remarkRules: {
      validators: [{ maxlength: 200 }],
    },
  };

  formData = {
    clusterName: null,
    clusterType: null,
    clusterHome: null,
    clusterVersion: null,
    clusterConf: null,
    remark: null,
  };

  clusterTypeList: Dict[] = [];

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private clusterService: ClusterService,
    private dictDataService: DictDataService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.clusterType).subscribe((d) => {
      this.clusterTypeList = d;
    });
  }

  submitForm({ valid }) {
    if (valid) {
      this.clusterService.add(this.formData).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
