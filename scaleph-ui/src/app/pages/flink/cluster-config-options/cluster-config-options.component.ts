import {DOCUMENT} from '@angular/common';
import {Component, Inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {FormLayout, LoadingService, ModalService} from 'ng-devui';
import {Dict, DICT_TYPE, PRIVILEGE_CODE} from 'src/app/@core/data/app.data';
import {AuthService} from 'src/app/@core/services/auth.service';
import {ClusterConfigService} from "../../../@core/services/flink/cluster-config.service";
import {SysDictDataService} from "../../../@core/services/admin/dict-data.service";

@Component({
  selector: 'app-cluster-config-options',
  templateUrl: './cluster-config-options.component.html',
  styleUrls: ['./cluster-config-options.component.scss'],
})
export class ClusterConfigOptionsComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;

  layoutDirection: FormLayout = FormLayout.Horizontal;

  isCollapsed = true;

  flinkStateBackendList: Dict[] = []

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
    this.dictDataService.listByType(DICT_TYPE.flinkStateBackend).subscribe((d) => {
      this.flinkStateBackendList = d;
    });
  }


}
