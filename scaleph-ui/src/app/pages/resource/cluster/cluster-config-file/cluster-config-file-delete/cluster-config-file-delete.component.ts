import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {DeployConfigService} from "../../../../../@core/services/resource/deploy-config.service";

@Component({
  selector: 'app-cluster-config-file-delete',
  templateUrl: './cluster-config-file-delete.component.html',
  styleUrls: ['../cluster-config-file.component.scss'],
})
export class ClusterConfigFileDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private deployConfigService: DeployConfigService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.deployConfigService.deleteFiles(this.data.id, this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
