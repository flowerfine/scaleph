import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { ToastService } from 'ng-devui';
import { DiProjectService } from 'src/app/@core/services/di-project.service';

@Component({
  selector: 'app-project-delete',
  templateUrl: './project-delete.component.html',
  styleUrls: ['../project.component.scss'],
})
export class ProjectDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private toastService: ToastService, private projectService: DiProjectService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  delete() {
    this.projectService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
