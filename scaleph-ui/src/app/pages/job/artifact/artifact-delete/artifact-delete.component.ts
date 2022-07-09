import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {ArtifactService} from "../../../../@core/services/job/artifact.service";

@Component({
  selector: 'app-job-artifact-delete',
  templateUrl: './artifact-delete.component.html',
  styleUrls: ['../artifact.component.scss'],
})
export class ArtifactDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;

  constructor(private elr: ElementRef, private artifactService: ArtifactService) {
  }

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.artifactService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
