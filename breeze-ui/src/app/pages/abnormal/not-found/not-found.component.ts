import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'da-not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['../abnormal.component.scss'],
})
export class NotFoundComponent implements OnInit, OnDestroy {
  themeService: any;
  darkMode = '';
  isDark: boolean;

  constructor(private translate: TranslateService) {}

  ngOnInit(): void {
    this.themeService = (window as { [key: string]: any })['devuiThemeService'];
    if (this.themeService) {
      this.themeChange();
    }
    if (this.themeService && this.themeService.eventBus) {
      this.themeService.eventBus.add('themeChanged', this.themeChange);
    }
  }

  getDarkModeStatus() {
    return this.themeService && this.themeService.currentTheme.isDark;
  }

  themeChange = () => {
    this.isDark = this.getDarkModeStatus();
    if (this.isDark) {
      this.darkMode = '-dark';
    } else {
      this.darkMode = '';
    }
  };

  ngOnDestroy() {
    if (this.themeService && this.themeService.eventBus) {
      this.themeService.eventBus.remove('themeChanged', this.themeChange);
    }
  }
}
