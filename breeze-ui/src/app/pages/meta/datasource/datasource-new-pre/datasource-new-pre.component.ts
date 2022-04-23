import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ModalService } from 'ng-devui';
import { DatasourceNewComponent } from '../datasource-new/datasource-new.component';

@Component({
  selector: 'app-datasource-new-pre',
  templateUrl: './datasource-new-pre.component.html',
  styleUrls: ['../datasource.component.scss'],
})
export class DatasourceNewPreComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  constructor(private elr: ElementRef, private translate: TranslateService, private modalService: ModalService) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
  }

  openAddDataSourceDialog(type: string) {
    const results = this.modalService.open({
      id: 'datasource-new',
      width: '580px',
      backdropCloseable: true,
      component: DatasourceNewComponent,
      data: {
        title: { name: this.translate.instant('meta.datasource') },
        data: type,
        onClose: (event: any) => {
          results.modalInstance.hide();
        },
        refresh: () => {
          this.data.refresh();
        },
      },
    });
    this.data.onClose();
  }
}
