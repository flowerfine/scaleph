import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ReleaseFlinkService} from "../../../../@core/services/resource/release-flink.service";

@Component({
  selector: 'app-release-flink-delete',
  templateUrl: './release-flink-delete.component.html',
  styleUrls: ['../release-flink.component.scss'],
})
export class ReleaseFlinkDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private releaseFlinkService: ReleaseFlinkService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.releaseFlinkService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
