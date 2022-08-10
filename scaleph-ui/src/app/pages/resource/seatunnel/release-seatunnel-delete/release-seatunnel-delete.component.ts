import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {SeatunnelReleaseService} from "../../../../@core/services/resource/seatunnel-release.service";

@Component({
  selector: 'app-seatunnel-release-delete',
  templateUrl: './release-seatunnel-delete.component.html',
  styleUrls: ['../release-seatunnel.component.scss'],
})
export class ReleaseSeatunnelDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private releaseSeaTunnelService: SeatunnelReleaseService) {
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
