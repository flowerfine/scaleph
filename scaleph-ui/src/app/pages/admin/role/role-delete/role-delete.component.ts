import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { RoleService } from 'src/app/@core/services/admin/role.service';

@Component({
  selector: 'app-role-delete',
  templateUrl: './role-delete.component.html',
  styleUrls: ['../role.component.scss'],
})
export class RoleDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private roleService: RoleService, private elr: ElementRef) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.roleService.delete(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
