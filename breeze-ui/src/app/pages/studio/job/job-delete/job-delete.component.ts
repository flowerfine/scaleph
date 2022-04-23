import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { ToastService } from 'ng-devui';
import { DiJobService } from 'src/app/@core/services/di-job.service';

@Component({
  selector: 'app-job-delete',
  templateUrl: './job-delete.component.html',
  styleUrls: ['../job.component.scss'],
})
export class JobDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private toastService: ToastService, private jobService: DiJobService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.jobService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
