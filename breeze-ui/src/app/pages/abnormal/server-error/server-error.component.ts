import { Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'da-server-error',
  templateUrl: './server-error.component.html',
  styleUrls: ['../abnormal.component.scss'],
})
export class ServerErrorComponent implements OnInit, OnDestroy {
  themeService: any;
  darkMode = '';
  isDark: boolean;

  constructor() {}

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
