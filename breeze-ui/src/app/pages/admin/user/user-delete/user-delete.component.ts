import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { User } from 'src/app/@core/data/admin.data';
import { UserService } from 'src/app/@core/services/user.service';

@Component({
  selector: 'app-user-delete',
  templateUrl: './user-delete.component.html',
  styleUrls: ['./user-delete.component.scss'],
})
export class UserDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private userService: UserService, private elr: ElementRef) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    let user: User = {
      userName: this.data.item.userName,
      id: this.data.item.id,
      userStatus: { value: '91', label: '' },
      email: this.data.item.email,
    };
    this.userService.update(user).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
