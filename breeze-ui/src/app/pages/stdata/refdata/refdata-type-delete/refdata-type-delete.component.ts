import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { RefdataService } from 'src/app/@core/services/refdata.service';

@Component({
  selector: 'app-refdata-type-delete',
  templateUrl: './refdata-type-delete.component.html',
  styleUrls: ['../refdata.component.scss'],
})
export class RefdataTypeDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private refdataService: RefdataService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.refdataService.deleteTypeBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
