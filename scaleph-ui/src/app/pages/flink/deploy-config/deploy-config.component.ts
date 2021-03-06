import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {FlinkDeployConfig, FlinkDeployConfigParam} from 'src/app/@core/data/flink.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {DeployConfigService} from "../../../@core/services/flink/deploy-config.service";
import {DeployConfigNewComponent} from "./deploy-config-new/deploy-config-new.component";
import {DeployConfigDeleteComponent} from "./deploy-config-delete/deploy-config-delete.component";
import {DeployConfigUpdateComponent} from "./deploy-config-update/deploy-config-update.component";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";

@Component({
  selector: 'app-deploy-config',
  templateUrl: './deploy-config.component.html',
  styleUrls: ['./deploy-config.component.scss'],
})
export class DeployConfigComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: FlinkDeployConfig[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };
  searchFormConfig = {configType: null, name: ''};

  flinkDeployConfigTypeList: Dict[] = []

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private deployConfigService: DeployConfigService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.flinkDeployConfigType).subscribe((d) => {
      this.flinkDeployConfigTypeList = d;
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: FlinkDeployConfigParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      configType: this.searchFormConfig.configType ? this.searchFormConfig.configType.value : '',
      name: this.searchFormConfig.name,
    };

    this.deployConfigService.list(param).subscribe((d) => {
      this.pager.total = d.total;
      this.dataTableDs = d.records;
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

  reset() {
    this.searchFormConfig = {configType: '', name: ''};
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openAddDeployConfigDialog() {
    const results = this.modalService.open({
      id: 'deploy-config-add',
      width: '580px',
      backdropCloseable: true,
      component: DeployConfigNewComponent,
      data: {
        title: {name: this.translate.instant('flink.deploy-config.name_')},
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeployConfig(row: FlinkDeployConfig) {
    this.router.navigate(['/scaleph', 'flink', 'deploy-config-file'], {
      queryParams: {
        id: row.id
      }
    });
  }

  openEditDeployConfigDialog(item: FlinkDeployConfig) {
    const results = this.modalService.open({
      id: 'deploy-config-edit',
      width: '580px',
      backdropCloseable: true,
      component: DeployConfigUpdateComponent,
      data: {
        title: {name: this.translate.instant('flink.deploy-config.name_')},
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteDeployConfigDialog(items: FlinkDeployConfig[]) {
    const results = this.modalService.open({
      id: 'deploy-config-delete',
      width: '346px',
      backdropCloseable: true,
      component: DeployConfigDeleteComponent,
      data: {
        title: this.translate.instant('app.common.operate.delete.confirm.title'),
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
