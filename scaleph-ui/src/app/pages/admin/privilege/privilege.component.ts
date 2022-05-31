import { Component, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { IButtonStyle, ITreeItem, OperableTreeComponent, TreeComponent, TreeNode } from 'ng-devui';
import { SecPrivilege, SecRole } from 'src/app/@core/data/admin.data';
import { PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { DeptService } from 'src/app/@core/services/admin/dept.service';
import { RoleService } from 'src/app/@core/services/admin/role.service';
import { PrivilegeService } from 'src/app/@core/services/admin/privilege.service';

@Component({
  selector: 'app-privilege',
  templateUrl: './privilege.component.html',
  styleUrls: ['./privilege.component.scss'],
})
export class PrivilegeComponent implements OnInit {
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  @ViewChild('operableTree', { static: false }) operableTree: OperableTreeComponent;
  @ViewChild('privilegeTree', { static: false }) privilegeTree: OperableTreeComponent;
  roleList: SecRole[] = [];
  roleTab: string = 'role';
  deptTab: string = 'dept';
  tabId: string = this.roleTab;
  roleBtnStyle: IButtonStyle;
  deptBtnStyle: IButtonStyle;
  deptList: ITreeItem[];
  searchTip;
  grantRoleList: SecRole[] = [];
  grantedRoleList: SecRole[] = [];
  checkedItemId: number = 0;
  menuPrivilegeTab: string = 'menuPrivilege';
  optPrivilegeTab: string = 'optPrivilege';
  dataPrivilegeTab: string = 'dataPrivilege';
  privilegeList: ITreeItem[];
  checkedPrivilegeList: SecPrivilege[] = [];
  activeTabId;
  tabItems = [
    {
      id: 'menuPrivilege',
      title: this.translate.instant('admin.menuPrivilege'),
    },
    {
      id: 'optPrivilege',
      title: this.translate.instant('admin.optPrivilege'),
    },
    {
      id: 'dataPrivilege',
      title: this.translate.instant('admin.dataPrivilege'),
    },
  ];
  constructor(
    private roleService: RoleService,
    private translate: TranslateService,
    private deptService: DeptService,
    private privilegeService: PrivilegeService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.searchTip = this.translate.instant('app.common.operate.query.tip');
    this.refreshDeptList();
    this.refreshRoleList();
  }

  refreshPrivilege(roleId: string, resourceType: string) {
    this.privilegeService.listAll(resourceType).subscribe((d) => {
      this.privilegeList = d;
      this.privilegeService.listByRole(roleId, resourceType).subscribe((d) => {
        if (d != null && d != undefined) {
          this.checkedPrivilegeList = d;
          this.checkedPrivilegeList.forEach((item) => {
            this.privilegeTree.operableTree.treeFactory.checkNodesById(item.id, true, 'none');
            this.privilegeTree.operableTree.treeFactory.openNodesById(item.id);
          });
        }
      });
    });
  }

  refreshGrantRole(deptId: string) {
    this.roleService.listGrantRoleByDept(deptId).subscribe((d) => {
      this.grantRoleList = d;
    });
    this.roleService.listRoleByDept(deptId).subscribe((d) => {
      this.grantedRoleList = d;
    });
  }

  refreshRoleList() {
    this.roleService.listAll().subscribe((d) => {
      this.roleList = d;
    });
    this.switchBtnStyle();
  }

  refreshDeptList() {
    this.deptService.listAll().subscribe((d) => {
      this.deptList = d;
    });
    this.switchBtnStyle();
  }

  switchBtnStyle() {
    if (this.tabId === this.deptTab) {
      this.roleBtnStyle = 'common';
      this.deptBtnStyle = 'primary';
    } else if (this.tabId === this.roleTab) {
      this.roleBtnStyle = 'primary';
      this.deptBtnStyle = 'common';
    }
    this.checkedItemId = 0;
  }

  showDept() {
    this.tabId = this.deptTab;
    this.grantRoleList = [];
    this.grantedRoleList = [];
    this.refreshDeptList();
  }
  showRole() {
    this.tabId = this.roleTab;
    this.refreshRoleList();
  }

  searchDeptTree(event) {
    this.operableTree.operableTree.treeFactory.searchTree(event, true);
  }

  searchPrivilegeTree(event) {
    this.privilegeTree.operableTree.treeFactory.searchTree(event, true);
  }

  isRoleSelect(item: SecRole): boolean {
    return item.id == this.checkedItemId && this.tabId === this.roleTab;
  }

  showPrivilegeByRole(item: SecRole) {
    this.checkedItemId = item.id;
    this.activeTabChange(this.activeTabId);
  }

  showRoleByDept(item: TreeNode) {
    this.checkedItemId = item.id;
    this.refreshGrantRole(String(item.id));
  }

  activeTabChange(tabId) {
    this.activeTabId = tabId;
    if (this.menuPrivilegeTab === tabId) {
      this.refreshPrivilege(String(this.checkedItemId), '0');
    } else if (this.optPrivilegeTab === tabId) {
      this.refreshPrivilege(String(this.checkedItemId), '1');
    } else if (this.dataPrivilegeTab === tabId) {
      this.refreshPrivilege(String(this.checkedItemId), '2');
    }
  }

  privilegeNodeChecked(nodes: Array<ITreeItem>) {
    let privilegeIds: string[] = [];
    nodes.forEach((d) => {
      privilegeIds.push(d.id as string);
    });
    if (this.menuPrivilegeTab === this.activeTabId) {
      this.privilegeService.grant(String(this.checkedItemId), privilegeIds, '0').subscribe((d) => {});
    } else if (this.optPrivilegeTab === this.activeTabId) {
      this.privilegeService.grant(String(this.checkedItemId), privilegeIds, '1').subscribe((d) => {});
    } else if (this.dataPrivilegeTab === this.activeTabId) {
      this.privilegeService.grant(String(this.checkedItemId), privilegeIds, '2').subscribe((d) => {});
    }
  }

  grantDeptRole(role: SecRole) {
    this.roleService.grantDeptRole(this.checkedItemId, role.id).subscribe((d) => {
      this.refreshGrantRole(String(this.checkedItemId));
    });
  }
  revokeDeptRole(role: SecRole) {
    this.roleService.revokeDeptRole(this.checkedItemId, role.id).subscribe((d) => {
      this.refreshGrantRole(String(this.checkedItemId));
    });
  }
}
