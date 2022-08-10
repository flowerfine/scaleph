import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ClusterCredentialService} from "../../../../../@core/services/resource/cluster-credential.service";

@Component({
  selector: 'app-cluster-credential-file-delete',
  templateUrl: './cluster-credential-file-delete.component.html',
  styleUrls: ['../cluster-credential-file.component.scss'],
})
export class ClusterCredentialFileDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private clusterCredentialService: ClusterCredentialService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.clusterCredentialService.deleteFiles(this.data.id, this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
