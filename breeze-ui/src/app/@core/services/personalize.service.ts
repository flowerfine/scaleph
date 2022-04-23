import { Injectable, OnInit } from '@angular/core';
import { Theme } from 'ng-devui/theme';
import { ReplaySubject } from 'rxjs';
import { ThemeType } from 'src/app/@shared/models/theme';
import { LARGE_RADIUS, LARGE_SIZE, MEDIUM_RADIUS, MEDIUM_SIZE, NORMAL_RADIUS, NORMAL_SIZE, NO_RADIUS } from 'src/config/custom-theme';
import { CustomThemeService, IThemeData } from './custom-theme.service';

export interface ThemeConfigItem {
  name: string;
  id: string;
  data: {
    'devui-border-radius'?: string;
    'devui-border-radius-card'?: string;
    'devui-font-size'?: string;
    'devui-font-size-card-title'?: string;
    'devui-font-size-page-title'?: string;
    'devui-font-size-modal-title'?: string;
    'devui-font-size-price'?: string;
    'devui-font-size-data-overview'?: string;
    'devui-font-size-icon'?: string;
    'devui-font-size-sm'?: string;
    'devui-font-size-md'?: string;
    'devui-font-size-lg'?: string;
  };
}

export interface ThemeConfig {
  name: 'themes' | 'font' | 'radius';
  icon: string;
  items: ThemeConfigItem[];
}

@Injectable()
export class PersonalizeService {
  themes: ThemeConfigItem[] = [];

  private _themeChange = new ReplaySubject<any>(1); // 主题切换

  configs: ThemeConfig[] = [
    {
      name: 'themes',
      icon: 'icon-color',
      items: [],
    },
    {
      name: 'font',
      icon: 'icon-font',
      items: [
        {
          name: 'normal',
          id: 'normal',
          data: NORMAL_SIZE,
        },
        {
          name: 'medium',
          id: 'medium',
          data: MEDIUM_SIZE,
        },
        {
          name: 'large',
          id: 'large',
          data: LARGE_SIZE,
        },
      ],
    },
    {
      name: 'radius',
      icon: 'icon-quick-stop',
      items: [
        {
          name: 'no_radius',
          id: 'no_radius',
          data: NO_RADIUS,
        },
        {
          name: 'normal',
          id: 'normal',
          data: NORMAL_RADIUS,
        },
        {
          name: 'medium',
          id: 'medium',
          data: MEDIUM_RADIUS,
        },
        {
          name: 'large',
          id: 'large',
          data: LARGE_RADIUS,
        },
      ],
    },
  ];

  defaultCustom = {
    brand: '#343a40',
    isDark: false,
  };

  constructor(private customThemeService: CustomThemeService) {}
  initTheme() {
    if ((window as { [key: string]: any })['devuiThemes']) {
      //TODO 组件库开源版本这个色值错误
      (window as { [key: string]: any })['devuiThemes'].devuiDarkTheme.data['devui-placeholder'] = '#8A8A8A';
      this.themes = Object.values((window as { [key: string]: any })['devuiThemes']);
      const { brand, isDark } = localStorage.getItem('user-custom-theme-config')
        ? JSON.parse(localStorage.getItem('user-custom-theme-config')!)
        : this.defaultCustom;
      const themeData = this.getCustomThemeData(brand, isDark);
      this.setTheme((window as { [key: string]: any })['devuiThemes']['customTheme'], themeData, isDark);
      this.configs[0].items = this.themes;
      // 主题设置
      const themeId = localStorage.getItem('theme') || this.themes[0].id;
      // 字号设置
      const fontId = localStorage.getItem('font') || this.configs[1].items[0].id;
      const radiusId = localStorage.getItem('radius') || this.configs[2].items[0].id;

      this.changeTheme(themeId, fontId, radiusId);
    }
  }

  changeTheme(themeId: string, fId?: string, rId?: string) {
    let theme: Theme;
    const themes = (this.configs[0].items as any).filter((i: ThemeConfigItem) => {
      return i.id === themeId;
    });
    const { radiusId, fontId, fontData, radiusData } = this.getSizeAndRadiusData(fId, rId);
    const customData = Object.assign({}, fontData, radiusData);
    if (themes.length) {
      theme = themes[0];
    } else {
      // 重置默认
      theme = this.configs[0].items[0];
    }
    theme.data = Object.assign(theme.data, customData);
    (window as { [key: string]: any })['devuiThemeService'].applyTheme(theme);
    localStorage.setItem('theme', theme.id);
    localStorage.setItem('font', fontId);
    localStorage.setItem('radius', radiusId);
  }

  getCustomThemeData(color: string, isDark: boolean): IThemeData {
    const themeData = this.customThemeService.genThemeData(
      [
        {
          colorName: 'devui-brand',
          color: color,
        },
      ],
      isDark,
      'hsl'
    );
    return themeData;
  }

  setTheme(theme: any, themeData: any, isDark: boolean) {
    Object.assign(theme, {
      data: themeData,
      isDark,
    });
  }

  getSizeAndRadiusData(fId?: string, rId?: string) {
    const fontId = fId || localStorage.getItem('font') || this.configs[1].items[0].id;
    const radiusId = rId || localStorage.getItem('radius') || this.configs[2].items[0].id;
    const fonts = (this.configs[1].items as any).filter((font: any) => {
      return font.id === fontId;
    });
    const radiusList = (this.configs[2].items as any).filter((radius: any) => {
      return radius.id === radiusId;
    });
    return {
      fontId,
      radiusId,
      fontData: fonts[0].data,
      radiusData: radiusList[0].data,
    };
  }

  setCustomThemeData(themeData: any, color: string, isDark: boolean) {
    const len = this.configs[0].items.length;
    const theme = this.configs[0].items[len - 1];
    const { fontData, radiusData } = this.getSizeAndRadiusData();
    Object.assign(themeData, fontData, radiusData);
    this.setTheme(theme, color, isDark);
    theme.data = Object.assign(theme.data, themeData);
    (window as { [key: string]: any })['devuiThemeService'].applyTheme(theme);
    localStorage.setItem('user-custom-theme-config', JSON.stringify({ brand: color, isDark }));
    localStorage.setItem('theme', ThemeType.Custom);
  }

  setUiTheme() {
    const currentTheme = (window as { [key: string]: any })['devuiCurrentTheme'] || ThemeType.Default;
    this._themeChange.next(currentTheme);
  }

  getUiTheme() {
    const themeService = (window as { [key: string]: any })['devuiThemeService'];
    this.setUiTheme();
    if (!(themeService && themeService.eventBus)) {
    }
    const themeChangedFunc = () => {
      this.setUiTheme();
    };
    themeService.eventBus.add('themeChanged', themeChangedFunc);
    return this._themeChange.asObservable();
  }

  setRefTheme(themeId: string) {
    const devuiThemes = Object.values((window as { [key: string]: any })['devuiThemes']);
    const themes = devuiThemes.filter((i: Theme | unknown) => (i as Theme).id === themeId);
    if (themes.length) {
      (window as { [key: string]: any })['devuiThemeService'].applyTheme(themes[0]);
    }
  }
}
