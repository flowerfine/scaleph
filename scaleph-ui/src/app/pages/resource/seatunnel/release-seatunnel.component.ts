import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {DataTableComponent, LoadingService, ModalService} from 'ng-devui';
import {DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE, USER_AUTH} from 'src/app/@core/data/app.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ReleaseSeatunnelUploadComponent} from "./release-seatunnel-upload/release-seatunnel-upload.component";
import {ReleaseSeatunnelDeleteComponent} from "./release-seatunnel-delete/release-seatunnel-delete.component";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";
import {ReleaseSeaTunnelService} from "../../../@core/services/resource/release-seatunnel.service";
import {ReleaseSeaTunnel, ReleaseSeaTunnelParam} from "../../../@core/data/resource.data";

@Component({
  selector: 'app-release-seatunnel',
  templateUrl: './release-seatunnel.component.html',
  styleUrls: ['./release-seatunnel.component.scss'],
})
export class ReleaseSeatunnelComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('dataTable', {static: true}) dataTable: DataTableComponent;
  dataLoading: boolean = false;
  dataTableChecked: boolean = false;
  loadTarget: any;
  dataTableDs: ReleaseSeaTunnel[] = [];
  pager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  searchFormConfig = {version: null, fileName: ''};

  seatunnelVersionList: Dict[] = []

  constructor(
    public authService: AuthService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private dictDataService: SysDictDataService,
    private releaseSeaTunnelService: ReleaseSeaTunnelService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.refreshTable();
    this.dictDataService.listByType(DICT_TYPE.seatunnelVersion).subscribe((d) => {
      this.seatunnelVersionList = d;
    });
  }

  refreshTable() {
    this.openDataTableLoading();
    let param: ReleaseSeaTunnelParam = {
      pageSize: this.pager.pageSize,
      current: this.pager.pageIndex,
      version: this.searchFormConfig.version ? this.searchFormConfig.version.value : '',
      fileName: this.searchFormConfig.fileName,
    };

    this.releaseSeaTunnelService.list(param).subscribe((d) => {
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
    this.searchFormConfig = {version: null, fileName: ''};
    this.pager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshTable();
  }

  openUploadReleaseDialog() {
    const results = this.modalService.open({
      id: 'release-seatunnel-upload',
      width: '580px',
      backdropCloseable: true,
      component: ReleaseSeatunnelUploadComponent,
      data: {
        title: {name: this.translate.instant('resource.release.name')},
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshTable();
        },
      },
    });
  }

  openDeleteReleaseDialog(items: ReleaseSeaTunnel[]) {
    const results = this.modalService.open({
      id: 'release-seatunnel-delete',
      width: '346px',
      backdropCloseable: true,
      component: ReleaseSeatunnelDeleteComponent,
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

  downloadRelease(item: ReleaseSeaTunnel) {
    let url: string =
      'api/resource/release/seatunnel/download/' + item.id +
      '?' +
      USER_AUTH.token +
      '=' +
      localStorage.getItem(USER_AUTH.token);
    const a = document.createElement('a');
    a.href = url;
    a.download = item.fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
