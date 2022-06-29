import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {FlinkClusterConfig, FlinkClusterConfigParam} from 'src/app/@core/data/flink.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterConfigService} from "../../../@core/services/flink/cluster-config.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {ClusterConfigNewComponent} from "../cluster-config/cluster-config-new/cluster-config-new.component";
import {ClusterConfigUpdateComponent} from "../cluster-config/cluster-config-update/cluster-config-update.component";
import {ClusterConfigDeleteComponent} from "../cluster-config/cluster-config-delete/cluster-config-delete.component";

@Component({
  selector: 'app-cluster-config-options',
  templateUrl: './cluster-config-options.component.html',
  styleUrls: ['./cluster-config-options.component.scss'],
})
export class ClusterConfigOptionsComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: FlinkClusterConfig[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };



  isCollapsed = true;

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private clusterConfigService: ClusterConfigService,
    private router: Router
  ) {
  }

  ngOnInit(): void {

  }


}
