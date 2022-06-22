import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ReleaseService} from "../../../../@core/services/flink/release.service";

@Component({
  selector: 'app-release-delete',
  templateUrl: './release-delete.component.html',
  styleUrls: ['../release.component.scss'],
})
export class ReleaseDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private releaseService: ReleaseService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.releaseService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
