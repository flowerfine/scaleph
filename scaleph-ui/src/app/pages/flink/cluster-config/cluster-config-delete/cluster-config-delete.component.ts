import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ClusterConfigService} from "../../../../@core/services/flink/cluster-config.service";

@Component({
  selector: 'app-cluster-config-delete',
  templateUrl: './cluster-config-delete.component.html',
  styleUrls: ['../cluster-config.component.scss'],
})
export class ClusterConfigDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private clusterConfigService: ClusterConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.clusterConfigService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
