import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { RefdataService } from 'src/app/@core/services/refdata.service';

@Component({
  selector: 'app-refdata-data-delete',
  templateUrl: './refdata-data-delete.component.html',
  styleUrls: ['../refdata.component.scss'],
})
export class RefdataDataDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private refdataService: RefdataService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.refdataService.deleteDataBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
