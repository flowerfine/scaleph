import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { ToastService } from 'ng-devui';
import { SysDictTypeService } from 'src/app/@core/services/admin/dict-type.service';

@Component({
  selector: 'app-dict-type-delete',
  templateUrl: './dict-type-delete.component.html',
  styleUrls: ['../dict.component.scss'],
})
export class DictTypeDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private dictTypeService: SysDictTypeService, private elr: ElementRef, private toastService: ToastService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.dictTypeService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
