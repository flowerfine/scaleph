import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { ClusterService } from 'src/app/@core/services/datadev/cluster.service';

@Component({
  selector: 'app-cluster-update',
  templateUrl: './cluster-update.component.html',
  styleUrls: ['../cluster.component.scss'],
})
export class ClusterUpdateComponent implements OnInit {
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
    id: null,
    clusterName: null,
    clusterType: null,
    clusterHome: null,
    clusterVersion: null,
    clusterConf: null,
    remark: null,
  };
  constructor(private elr: ElementRef, private translate: TranslateService, private clusterService: ClusterService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.formData = {
      id: this.data.item.id,
      clusterName: this.data.item.clusterName,
      clusterType: this.data.item.clusterType,
      clusterHome: this.data.item.clusterHome,
      clusterVersion: this.data.item.clusterVersion,
      clusterConf: this.data.item.clusterConf,
      remark: this.data.item.remark,
    };
  }

  submitForm({ valid }) {
    if (valid) {
      this.clusterService.update(this.formData).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
