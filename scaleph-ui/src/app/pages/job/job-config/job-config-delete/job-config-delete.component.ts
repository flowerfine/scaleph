import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {JobConfigService} from "../../../../@core/services/job/job-config.service";

@Component({
  selector: 'app-job-config-delete',
  templateUrl: './job-config-delete.component.html',
  styleUrls: ['../job-config.component.scss'],
})
export class JobConfigDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private jobConfigService: JobConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.jobConfigService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
