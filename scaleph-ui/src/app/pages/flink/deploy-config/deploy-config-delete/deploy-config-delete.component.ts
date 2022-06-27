import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {DeployConfigService} from "../../../../@core/services/flink/deploy-config.service";

@Component({
  selector: 'app-deploy-config-delete',
  templateUrl: './deploy-config-delete.component.html',
  styleUrls: ['../deploy-config.component.scss'],
})
export class DeployConfigDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private deployConfigService: DeployConfigService) {
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
