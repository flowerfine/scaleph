import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { DiClusterService } from 'src/app/@core/services/di-cluster.service';

@Component({
  selector: 'app-cluster-delete',
  templateUrl: './cluster-delete.component.html',
  styleUrls: ['../cluster.component.scss'],
})
export class ClusterDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private clusterService: DiClusterService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.clusterService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
