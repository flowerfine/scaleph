import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ClusterCredentialService} from "../../../../@core/services/resource/cluster-credential.service";

@Component({
  selector: 'app-cluster-credential-delete',
  templateUrl: './cluster-credential-delete.component.html',
  styleUrls: ['../cluster-credential.component.scss'],
})
export class ClusterCredentialDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private clusterCredentialService: ClusterCredentialService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.clusterCredentialService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
