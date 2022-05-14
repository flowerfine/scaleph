import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'da-header-logo',
  templateUrl: './header-logo.component.html',
  styleUrls: ['./header-logo.component.scss'],
})
export class HeaderLogoComponent implements OnInit {
  @Input() shrink = false;

  constructor() {}

  ngOnInit(): void {}
}
