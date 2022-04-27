import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { MetaDataElementService } from 'src/app/@core/services/meta-data-element.service';

@Component({
  selector: 'app-data-element-delete',
  templateUrl: './data-element-delete.component.html',
  styleUrls: ['../data-element.component.scss'],
})
export class DataElementDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private metaElementService: MetaDataElementService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.metaElementService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
