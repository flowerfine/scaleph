import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { DictDataService } from 'src/app/@core/services/dict-data.service';

@Component({
  selector: 'app-dict-data-delete',
  templateUrl: './dict-data-delete.component.html',
  styleUrls: ['../dict.component.scss'],
})
export class DictDataDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private dictDataService: DictDataService, private elr: ElementRef) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.dictDataService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
