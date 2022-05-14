import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TransferDirection } from 'ng-devui';
import { TransferData } from 'src/app/@core/data/app.data';
import { RoleService } from 'src/app/@core/services/admin/role.service';
import { UserService } from 'src/app/@core/services/admin/user.service';

@Component({
  selector: 'app-role-grant',
  templateUrl: './role-grant.component.html',
  styleUrls: ['../role.component.scss'],
})
export class RoleGrantComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  targetOpt: TransferData[] = [];
  sourceOpt: TransferData[] = [];

  constructor(private elr: ElementRef, private roleService: RoleService, private userService: UserService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.refreshData();
  }

  refreshData() {
    this.userService.listByUserNameAndRole('', this.data.items.id, String(TransferDirection.TARGET)).subscribe((d) => {
      this.targetOpt = d;
    });
    this.userService.listByUserNameAndRole('', this.data.items.id, String(TransferDirection.SOURCE)).subscribe((d) => {
      this.sourceOpt = d;
    });
  }

  search(data: { direction: number; keyword: string }) {
    this.userService.listByUserNameAndRole(data.keyword, this.data.items.id, String(data.direction)).subscribe((d) => {
      if (data.direction === TransferDirection.SOURCE) {
        this.sourceOpt = d;
      } else {
        this.targetOpt = d;
      }
    });
  }

  transferToTarget(event) {
    event.sourceOption.forEach((d, i) => {
      if (d.checked) {
        this.targetOpt.push(d);
      }
    });
    this.submit();
  }

  transferToSource(event) {
    event.targetOption.forEach((d, i) => {
      if (d.checked) {
        this.targetOpt.splice(i, 1);
      }
    });
    this.submit();
  }

  submit() {
    let userIds: string[] = [];
    this.targetOpt.forEach((d) => {
      userIds.push(d.value);
    });
    this.roleService.grant(this.data.items.id, userIds).subscribe((d) => {
      this.refreshData();
    });
  }
}
