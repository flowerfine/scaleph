import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ReleaseService} from "../../../../@core/services/flink/release.service";
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";

@Component({
  selector: 'app-release-delete',
  templateUrl: './deploy-config-file-delete.component.html',
  styleUrls: ['../deploy-config-file.component.scss'],
})
export class DeployConfigFileDeleteComponent implements OnInit {
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
