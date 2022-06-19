import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
@Component({
  selector: 'app-datasource-update',
  templateUrl: './datasource-update.component.html',
  styleUrls: ['../datasource.component.scss'],
})
export class DatasourceUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  datasourceType: string;
  @ViewChild('datasource') datasource;

  constructor(private elr: ElementRef) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.datasourceType = this.data.item.datasourceType.value;
  }
}
