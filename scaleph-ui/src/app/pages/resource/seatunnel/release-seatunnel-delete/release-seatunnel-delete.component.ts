import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ReleaseService} from "../../../../@core/services/flink/release.service";
import {ReleaseSeaTunnelService} from "../../../../@core/services/resource/release-seatunnel.service";

@Component({
  selector: 'app-release-seatunnel-delete',
  templateUrl: './release-seatunnel-delete.component.html',
  styleUrls: ['../release-seatunnel.component.scss'],
})
export class ReleaseSeatunnelDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private releaseSeaTunnelService: ReleaseSeaTunnelService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.releaseSeaTunnelService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
