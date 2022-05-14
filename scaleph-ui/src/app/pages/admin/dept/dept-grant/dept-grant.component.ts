import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TransferDirection } from 'ng-devui';
import { TransferData } from 'src/app/@core/data/app.data';
import { DeptService } from 'src/app/@core/services/admin/dept.service';
import { UserService } from 'src/app/@core/services/admin/user.service';

@Component({
  selector: 'app-dept-grant',
  templateUrl: './dept-grant.component.html',
  styleUrls: ['../dept.component.scss'],
})
export class DeptGrantComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  targetOpt: TransferData[] = [];
  sourceOpt: TransferData[] = [];

  constructor(private elr: ElementRef, private deptSerice: DeptService, private userService: UserService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.refreshData();
  }

  refreshData() {
    this.userService.listByUserNameAndDept('', this.data.items.id, String(TransferDirection.TARGET)).subscribe((d) => {
      this.targetOpt = d;
    });
    this.userService.listByUserNameAndDept('', this.data.items.id, String(TransferDirection.SOURCE)).subscribe((d) => {
      this.sourceOpt = d;
    });
  }

  search(data: { direction: number; keyword: string }) {
    this.userService.listByUserNameAndDept(data.keyword, this.data.items.id, String(data.direction)).subscribe((d) => {
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
    this.deptSerice.grant(this.data.items.id, userIds).subscribe((d) => {
      this.refreshData();
    });
  }
}
