import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent, IButtonStyle, ITreeItem, LoadingService, ModalService, OperableTreeComponent, TreeNode } from 'ng-devui';
import { Role, User, UserParam } from 'src/app/@core/data/admin.data';
import { DEFAULT_PAGE_PARAM, Dict, DICT_TYPE, PRIVILEGE_CODE } from 'src/app/@core/data/app.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { DeptService } from 'src/app/@core/services/admin/dept.service';
import { DeptDeleteComponent } from '../dept/dept-delete/dept-delete.component';
import { DeptGrantComponent } from '../dept/dept-grant/dept-grant.component';
import { DeptNewComponent } from '../dept/dept-new/dept-new.component';
import { DeptUpdateComponent } from '../dept/dept-update/dept-update.component';
import { RoleDeleteComponent } from '../role/role-delete/role-delete.component';
import { RoleGrantComponent } from '../role/role-grant/role-grant.component';
import { RoleNewComponent } from '../role/role-new/role-new.component';
import { RoleUpdateComponent } from '../role/role-update/role-update.component';
import { UserDeleteComponent } from './user-delete/user-delete.component';
import { UserNewComponent } from './user-new/user-new.component';
import { UserUpdateComponent } from './user-update/user-update.component';
import { RoleService } from 'src/app/@core/services/admin/role.service';
import { UserService } from 'src/app/@core/services/admin/user.service';
import { DictDataService } from 'src/app/@core/services/admin/dict-data.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit {
  @ViewChild('operableTree', { static: false }) operableTree: OperableTreeComponent;
  @ViewChild('userTable', { static: true }) userTable: DataTableComponent;
  PRIVILEGE_CODE = PRIVILEGE_CODE;
  roleList: Role[] = [];
  roleTab: string = 'role';
  deptTab: string = 'dept';
  tabId: string = this.roleTab;
  roleBtnStyle: IButtonStyle;
  deptBtnStyle: IButtonStyle;
  deptList: ITreeItem[];
  searchTip;
  searchFormConfig = { userName: '', nickName: '', userStatus: null, email: '', deptId: '', roleId: '' };
  userStatusList: Dict[] = [];
  userTableChecked: boolean = false;
  userTableDs: User[] = [];
  userLoadTarget: any;
  userLoading: boolean = false;
  userPager = {
    total: 0,
    pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
    pageSize: DEFAULT_PAGE_PARAM.pageSize,
    pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
  };

  constructor(
    private roleService: RoleService,
    @Inject(DOCUMENT) private doc: any,
    private loadingService: LoadingService,
    private translate: TranslateService,
    private modalService: ModalService,
    private deptService: DeptService,
    private userService: UserService,
    private dictDataService: DictDataService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.searchTip = this.translate.instant('app.common.operate.query.tip');
    this.refreshRoleList();
    this.refreshDeptList();
    this.refreshUserTable();
    this.dictDataService.listByType(DICT_TYPE.userStatus).subscribe((d) => {
      this.userStatusList = d;
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
      this.searchFormConfig.roleId = '';
    } else if (this.tabId === this.roleTab) {
      this.roleBtnStyle = 'primary';
      this.deptBtnStyle = 'common';
      this.searchFormConfig.deptId = '';
    }
  }

  showDept() {
    this.tabId = this.deptTab;
    this.refreshDeptList();
  }
  showRole() {
    this.tabId = this.roleTab;
    this.refreshRoleList();
  }

  mover(node: Role) {
    node.showOpIcon = true;
  }

  mleave(node: Role) {
    node.showOpIcon = false;
  }

  isRoleSelect(item: Role): boolean {
    return String(item.id) == this.searchFormConfig.roleId;
  }
  openAddDialog() {
    if (this.tabId === this.roleTab) {
      this.openAddRoleDialog();
    } else if (this.tabId === this.deptTab) {
      this.openAddDeptDialog(null, null);
    }
  }

  openAddRoleDialog() {
    const results = this.modalService.open({
      id: 'role-new',
      width: '580px',
      backdropCloseable: true,
      component: RoleNewComponent,
      data: {
        title: { name: this.translate.instant('admin.role') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshRoleList();
        },
      },
    });
  }

  openEditRoleDialog(item: Role) {
    const results = this.modalService.open({
      id: 'role-edit',
      width: '580px',
      backdropCloseable: true,
      component: RoleUpdateComponent,
      data: {
        title: { name: this.translate.instant('admin.role') },
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshRoleList();
        },
      },
    });
  }

  openDeleteRoleDialog(items: Role) {
    const results = this.modalService.open({
      id: 'role-delete',
      width: '346px',
      backdropCloseable: true,
      component: RoleDeleteComponent,
      data: {
        title: this.translate.instant('app.common.operate.delete.confirm.title'),
        items: items,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshRoleList();
        },
      },
    });
  }

  openGrantRoleDialog(items: Role) {
    const results = this.modalService.open({
      id: 'role-grant',
      width: '680px',
      backdropCloseable: true,
      component: RoleGrantComponent,
      data: {
        title: { name: this.translate.instant('admin.user') },
        items: items,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshUserTable();
        },
      },
    });
  }

  searchUserByRole(item: Role) {
    this.searchFormConfig.roleId = String(item.id);
    this.refreshUserTable();
  }

  openAddDeptDialog(event, node) {
    const results = this.modalService.open({
      id: 'dept-new',
      width: '580px',
      backdropCloseable: true,
      component: DeptNewComponent,
      data: {
        title: { name: this.translate.instant('admin.dept') },
        item: node,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: (node: TreeNode) => {
          this.operableTree.treeFactory.addNode(node);
          if (node.parentId != undefined) {
            this.operableTree.treeFactory.openNodesById(node.id);
            this.operableTree.treeFactory.activeNodeById(node.id);
          }
        },
      },
    });
  }

  openEditDeptDialog(event, node) {
    const results = this.modalService.open({
      id: 'dept-edit',
      width: '580px',
      backdropCloseable: true,
      component: DeptUpdateComponent,
      data: {
        title: { name: this.translate.instant('admin.dept') },
        item: node,
        index: this.operableTree.treeFactory.getNodeIndex(node),
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: (node) => {
          const children: TreeNode[] = this.operableTree.treeFactory.getChildrenById(node.id);
          this.operableTree.treeFactory.deleteNodeById(node.id);
          if (children.length > 0) {
            this.operableTree.treeFactory.addNode(
              {
                id: node.id,
                parentId: node.parentId,
                title: node.data.title,
              },
              node.index
            );
            this.operableTree.treeFactory.startLoading(node.id);
            this.deptService.listChilds(node.id).subscribe((d) => {
              this.operableTree.treeFactory.endLoading(node.id);
              this.operableTree.treeFactory.mapTreeItems({
                treeItems: d,
                parentId: node.id,
                treeNodeIdKey: 'deptId',
                treeNodeTitleKey: 'deptName',
                treeNodeChildrenKey: 'children',
              });
            });
          } else {
            this.operableTree.treeFactory.addNode({ id: node.id, parentId: node.parentId, title: node.data.title }, node.index);
          }
        },
      },
    });
  }

  loadChildren(node: TreeNode) {
    if (this.operableTree.treeFactory.getChildrenById(node.id).length === 0 && node.data.isParent) {
      if (!this.operableTree.treeFactory.nodes[node.id].data.loading) {
        this.operableTree.treeFactory.startLoading(node.id);
        this.deptService.listChilds(node.id).subscribe((d) => {
          this.operableTree.treeFactory.endLoading(node.id);
          this.operableTree.treeFactory.mapTreeItems({ treeItems: d, parentId: node.id });
        });
      }
    }
  }

  openDeleteDeptDialog(event, node) {
    const results = this.modalService.open({
      id: 'dept-delete',
      width: '346px',
      backdropCloseable: true,
      component: DeptDeleteComponent,
      data: {
        title: this.translate.instant('app.common.operate.delete.confirm.title'),
        items: node,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.operableTree.treeFactory.deleteNodeById(node.id);
        },
      },
    });
  }

  openGrantDeptDialog(event, node) {
    const results = this.modalService.open({
      id: 'dept-grant',
      width: '680px',
      backdropCloseable: true,
      component: DeptGrantComponent,
      data: {
        title: { name: this.translate.instant('admin.dept') },
        items: node,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshUserTable();
        },
      },
    });
  }

  beforeNodeDrop = (dragNodeId, dropNodeId, dropType) => {
    let dragNode = this.operableTree.treeFactory.getNodeById(dragNodeId);
    let dropNode = this.operableTree.treeFactory.getNodeById(dropNodeId);
    this.deptService
      .update({ id: dragNode.originItem.deptId, pid: dropType == 'inner' ? dropNodeId : dropNode.originItem.pid, deptName: dragNode.title })
      .subscribe((d) => {});
    return new Promise((resovle) => {
      resovle(undefined);
    });
  };

  searchDeptTree(event) {
    this.operableTree.operableTree.treeFactory.searchTree(event, true);
  }

  searchUserByDept(item: TreeNode) {
    this.searchFormConfig.deptId = item.id;
    this.refreshUserTable();
  }

  reset() {
    this.searchFormConfig = { userName: '', nickName: '', userStatus: null, email: '', deptId: '', roleId: '' };
    this.userPager = {
      total: 0,
      pageIndex: DEFAULT_PAGE_PARAM.pageIndex,
      pageSize: DEFAULT_PAGE_PARAM.pageSize,
      pageSizeOptions: DEFAULT_PAGE_PARAM.pageParams,
    };
    this.refreshUserTable();
  }

  refreshUserTable() {
    this.openUserLoading();
    let param: UserParam = {
      pageSize: this.userPager.pageSize,
      current: this.userPager.pageIndex,
      userName: this.searchFormConfig.userName,
      nickName: this.searchFormConfig.nickName,
      email: this.searchFormConfig.email,
      userStatus: this.searchFormConfig.userStatus ? this.searchFormConfig.userStatus.value : '',
      deptId: this.searchFormConfig.deptId,
      roleId: this.searchFormConfig.roleId,
    };
    this.userService.listByPage(param).subscribe((d) => {
      (this.userPager.total = d.total), (this.userTableDs = d.records), this.userLoadTarget.loadingInstance.close();
      this.userLoading = false;
      this.userTable.setTableCheckStatus({ pageAllChecked: false });
      this.getUserCheckedStatus();
    });
  }

  openUserLoading() {
    const dc = this.doc.querySelector('#userContent');
    this.userLoadTarget = this.loadingService.open({
      target: dc,
      message: this.translate.instant('app.common.loading'),
      positionType: 'relative',
      zIndex: 1,
    });
    this.userLoading = true;
  }

  openAddUserDialog() {
    const results = this.modalService.open({
      id: 'user-new',
      width: '700px',
      backdropCloseable: true,
      component: UserNewComponent,
      data: {
        title: { name: this.translate.instant('admin.user') },
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshUserTable();
        },
      },
    });
  }
  openEditUserDialog(item: User) {
    const results = this.modalService.open({
      id: 'user-edit',
      width: '700px',
      backdropCloseable: true,
      component: UserUpdateComponent,
      data: {
        title: { name: this.translate.instant('admin.user') },
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshUserTable();
        },
      },
    });
  }

  openDeleteUserDialog(item: User) {
    const results = this.modalService.open({
      id: 'user-delete',
      width: '346px',
      backdropCloseable: true,
      component: UserDeleteComponent,
      data: {
        title: this.translate.instant('app.common.operate.forbid.confirm.title'),
        item: item,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.refreshUserTable();
        },
      },
    });
  }

  enableUser(item: User) {
    let user: User = {
      userName: item.userName,
      id: item.id,
      userStatus: { value: '10', label: '' },
      email: item.email,
    };
    this.userService.update(user).subscribe((d) => {
      if (d.success) {
        this.refreshUserTable();
      }
    });
  }

  getUserCheckedStatus() {
    if (this.userTable.getCheckedRows().length > 0) {
      this.userTableChecked = true;
    } else {
      this.userTableChecked = false;
    }
  }
}
