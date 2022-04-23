import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { ToastService } from 'ng-devui';
import { MetaSystemService } from 'src/app/@core/services/meta-system.service';

@Component({
  selector: 'app-system-delete',
  templateUrl: './system-delete.component.html',
  styleUrls: ['../system.component.scss'],
})
export class SystemDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private toastService: ToastService, private metaSystemService: MetaSystemService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.metaSystemService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
