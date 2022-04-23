import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.component.html',
  styleUrls: ['./setting.component.scss'],
})
export class SettingComponent implements OnInit {
  menus = [
    {
      isActive: true,
      title: '基本设置',
    },
    {
      isActive: false,
      title: '邮箱设置',
    },
  ];
  constructor() {}

  ngOnInit(): void {}

  itemClickFn(clickedItem) {
    this.menus.forEach((item) => {
      item.isActive = false;
    });
    clickedItem.isActive = true;
  }
}
