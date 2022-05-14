import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { DaLayoutConfig } from './da-layout.type';
import { DEFAULT_LAYOUT_CONFIG } from './default-layout.config';

@Injectable({
  providedIn: 'root',
})
export class DaLayoutService {
  private _config: DaLayoutConfig = DEFAULT_LAYOUT_CONFIG;
  private layoutSubject = new ReplaySubject<DaLayoutConfig>(1);

  constructor() {
    const layoutConfig = localStorage.getItem('da-layout');
    if (layoutConfig) {
      this._config = JSON.parse(layoutConfig);
    }
    this.layoutSubject.next(this._config);
  }

  getLayoutConfig() {
    return this.layoutSubject.asObservable();
  }

  updateLayoutConfig(config: DaLayoutConfig) {
    localStorage.setItem('da-layout', JSON.stringify(config));
    this.layoutSubject.next(config);
  }
}
