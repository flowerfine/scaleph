import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-base-node',
  templateUrl: './base-node.component.html',
  styleUrls: ['./base-node.component.scss'],
})
export class BaseNodeComponent implements OnInit {
  @Input() title: string;
  constructor() {}

  ngOnInit(): void {}
}
