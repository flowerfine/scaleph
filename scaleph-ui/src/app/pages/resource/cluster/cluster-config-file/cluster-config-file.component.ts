import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {PRIVILEGE_CODE, USER_AUTH} from 'src/app/@core/data/app.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {SysDictDataService} from "../../../../@core/services/admin/dict-data.service";
import {DeployConfigService} from "../../../../@core/services/resource/deploy-config.service";
import {FileStatus} from "../../../../@core/data/flink.data";
import {ClusterConfigFileUploadComponent} from "./cluster-config-file-upload/cluster-config-file-upload.component";
import {ClusterConfigFileDeleteComponent} from "./cluster-config-file-delete/cluster-config-file-delete.component";

@Component({
  selector: 'app-cluster-config-file',
  templateUrl: './cluster-config-file.component.html',
  styleUrls: ['./cluster-config-file.component.scss'],
})
export class ClusterConfigFileComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs = null;

  flinkDeployConfig = null

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private deployConfigService: DeployConfigService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.deployConfigService.selectOne(params.id).subscribe((d) => {
        this.flinkDeployConfig = d
        this.refreshTable();
      });
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    this.deployConfigService.getFiles(this.flinkDeployConfig?.id).subscribe((d) => {
      this.dataTableDs = d;
      this.loadTarget.loadingInstance.close();
      this.dataLoading = false;
      this.dataTable.setTableCheckStatus({pageAllChecked: false});
      this.getDataTableCheckedStatus();
    });
  }

  openDataTableLoading() {
    const dc = this.doc.querySelector('#dataTableContent');
    this.loadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.dataLoading = true;
  }

  getDataTableCheckedStatus() {
    if (this.dataTable.getCheckedRows().length > 0) {
      this.dataTableChecked = true;
    } else {
      this.dataTableChecked = false;
    }
  }

  openUploadDeployConfigFileDialog() {
    const results = this.modalService.open({
      id: 'cluster-config-file-upload',
      width: '580px',
      backdropCloseable: true,
      component: ClusterConfigFileUploadComponent,
      data: {
        title: {name: this.translate.instant('resource.cluster-config-file.name_')},
        flinkDeployConfig: this.flinkDeployConfig,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  downloadDeployConfigFile(item: FileStatus) {
    let url: string =
      'api/flink/deploy-config/' + this.flinkDeployConfig.id + '/file/' + item.name
    '?' +
    USER_AUTH.token +
    '=' +
    localStorage.getItem(USER_AUTH.token);
    const a = document.createElement('a');
    a.href = url;
    a.download = item.name;
    a.click();
    window.URL.revokeObjectURL(url);
  }

  openDeleteDeployConfigFileDialog(items: FileStatus[]) {
    const results = this.modalService.open({
      id: 'cluster-config-file-delete',
      width: '346px',
      backdropCloseable: true,
      component: ClusterConfigFileDeleteComponent,
      data: {
        title: this.translate.instant('app.common.operate.delete.confirm.title'),
        id: this.flinkDeployConfig.id,
        items: items,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }
}
