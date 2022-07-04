import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ClusterInstanceService} from "../../../../@core/services/flink/cluster-instance.service";

@Component({
  selector: 'app-cluster-config-delete',
  templateUrl: './cluster-instance-shutdown.component.html',
  styleUrls: ['../cluster-instance.component.scss'],
})
export class ClusterInstanceShutdownComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private clusterInstanceService: ClusterInstanceService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.clusterInstanceService.shutdownBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
