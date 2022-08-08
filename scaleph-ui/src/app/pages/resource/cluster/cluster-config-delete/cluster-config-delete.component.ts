import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ClusterCredentialService} from "../../../../@core/services/resource/cluster-credential.service";

@Component({
  selector: 'app-cluster-credential-delete',
  templateUrl: './cluster-config-delete.component.html',
  styleUrls: ['../cluster-config.component.scss'],
})
export class ClusterConfigDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private deployConfigService: ClusterCredentialService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.deployConfigService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
