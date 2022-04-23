import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { ToastService } from 'ng-devui';
import { DictTypeService } from 'src/app/@core/services/dict-type.service';

@Component({
  selector: 'app-dict-type-delete',
  templateUrl: './dict-type-delete.component.html',
  styleUrls: ['../dict.component.scss'],
})
export class DictTypeDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private dictTypeService: DictTypeService, private elr: ElementRef, private toastService: ToastService) {}

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
