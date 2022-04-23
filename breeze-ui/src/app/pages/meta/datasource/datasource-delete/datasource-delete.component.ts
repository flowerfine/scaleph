import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { ToastService } from 'ng-devui';
import { DataSourceService } from 'src/app/@core/services/datasource.service';

@Component({
  selector: 'app-datasource-delete',
  templateUrl: './datasource-delete.component.html',
  styleUrls: ['../datasource.component.scss'],
})
export class DatasourceDeleteComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private toastService: ToastService, private dataSourceService: DataSourceService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }
  delete() {
    this.dataSourceService.deleteBatch(this.data.items).subscribe((d) => {
      if (d.success) {
        this.data.refresh();
      }
      this.data.onClose();
    });
  }
}
