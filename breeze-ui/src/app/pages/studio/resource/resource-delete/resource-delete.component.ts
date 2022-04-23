import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { DiResourceFileService } from 'src/app/@core/services/di-resource.service';

@Component({
  selector: 'app-resource-delete',
  templateUrl: './resource-delete.component.html',
  styleUrls: ['../resource.component.scss'],
})
export class ResourceDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private resourceService: DiResourceFileService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.resourceService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
