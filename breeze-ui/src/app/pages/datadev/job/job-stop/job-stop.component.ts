import { Component, Input, OnInit, ElementRef } from '@angular/core';
import { JobService } from 'src/app/@core/services/datadev/job.service';

@Component({
  selector: 'app-job-stop',
  templateUrl: './job-stop.component.html',
  styleUrls: ['../job.component.scss'],
})
export class JobStopComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private jobService: JobService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  stop() {
    this.jobService.stop(this.data.item?.id).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
