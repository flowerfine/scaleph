import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-user-center',
  templateUrl: './user-center.component.html',
  styleUrls: ['./user-center.component.scss'],
})
export class UserCenterComponent implements OnInit {
  menus = [
    {
      isActive: true,
      title: this.translate.instant('userCenter.profile'),
    },
    {
      isActive: false,
      title: this.translate.instant('userCenter.security'),
    },
    {
      isActive: false,
      title: this.translate.instant('userCenter.message'),
    },
    {
      isActive: false,
      title: this.translate.instant('userCenter.log'),
    },
  ];
  constructor(private translate: TranslateService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      let menu = params['menu'];
      if (menu != null && menu != undefined && menu != '') {
        this.itemClickFn(this.menus[menu]);
      }
    });
  }

  itemClickFn(clickedItem) {
    this.menus.forEach((item) => {
      item.isActive = false;
    });
    clickedItem.isActive = true;
  }
}
