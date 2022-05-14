import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { ToastService } from 'ng-devui';
import { DirectoryService } from 'src/app/@core/services/datadev/directory.service';

@Component({
  selector: 'app-directory-delete',
  templateUrl: './directory-delete.component.html',
  styleUrls: ['../job.component.scss'],
})
export class DirectoryDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private toastService: ToastService, private directoryService: DirectoryService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.directoryService.delete(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
