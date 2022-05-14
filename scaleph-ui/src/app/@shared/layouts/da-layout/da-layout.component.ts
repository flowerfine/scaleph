import { Component, HostBinding, Input, OnDestroy, ViewEncapsulation } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DaLayoutService } from './da-layout.service';
import { DaLayoutConfig } from './da-layout.type';

@Component({
  selector: 'da-layout-header',
  template: '<ng-content></ng-content>',
})
export class DaLayoutHeaderComponent implements OnDestroy {
  private destroy$ = new Subject();

  @HostBinding('class.da-layout-header') default = true;
  @HostBinding('style.height')
  get height() {
    return this?.config?.height + 'px';
  }

  @HostBinding('style.z-index')
  get zIndex() {
    return this?.config?.zIndex;
  }

  @HostBinding('style.display')
  get display() {
    return this?.config?.hidden ? 'none' : null;
  }

  @Input() config: DaLayoutConfig['header'];

  constructor(private layoutService: DaLayoutService) {
    this.layoutService
      .getLayoutConfig()
      .pipe(takeUntil(this.destroy$))
      .subscribe((config: DaLayoutConfig) => {
        this.config = config!.header!.firHeader;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(null);
    this.destroy$.complete();
  }
}

@Component({
  selector: 'da-layout-sec-header',
  template: '<ng-content></ng-content>',
})
export class DaLayoutSecHeaderComponent implements OnDestroy {
  private destroy$ = new Subject();

  @HostBinding('class.da-layout-sec-header') default = true;
  @HostBinding('style.height')
  get height() {
    return this?.config?.height + 'px';
  }

  @HostBinding('style.z-index')
  get zIndex() {
    return this?.config?.zIndex;
  }

  @HostBinding('style.display')
  get display() {
    return this?.config?.hidden ? 'none' : null;
  }

  @Input() config: DaLayoutConfig['header'];

  constructor(private layoutService: DaLayoutService) {
    this.layoutService
      .getLayoutConfig()
      .pipe(takeUntil(this.destroy$))
      .subscribe((config: DaLayoutConfig) => {
        this.config = config!.header!.secHeader;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(null);
    this.destroy$.complete();
  }
}

@Component({
  selector: 'da-layout-sidebar',
  template: '<ng-content></ng-content>',
})
export class DaLayoutSidebarComponent implements OnDestroy {
  private destroy$ = new Subject();

  @HostBinding('class.da-layout-sidebar') default = true;
  @HostBinding('style.width')
  get width() {
    return this?.config?.width + 'px';
  }

  @HostBinding('style.z-index')
  get zIndex() {
    return this?.config?.zIndex;
  }

  @HostBinding('style.display')
  get display() {
    return this?.config?.hidden ? 'none' : null;
  }

  @Input() config: DaLayoutConfig['sidebar']['firSidebar'];

  constructor(private layoutService: DaLayoutService) {
    this.layoutService
      .getLayoutConfig()
      .pipe(takeUntil(this.destroy$))
      .subscribe((config: DaLayoutConfig) => {
        this.config = config.sidebar.firSidebar;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(null);
    this.destroy$.complete();
  }
}

@Component({
  selector: 'da-layout-sec-sidebar',
  template: '<ng-content></ng-content>',
})
export class DaLayoutSecSidebarComponent implements OnDestroy {
  private destroy$ = new Subject();

  @HostBinding('class.da-layout-sec-sidebar') default = true;
  @HostBinding('style.width')
  get width() {
    return this?.config?.width + 'px';
  }

  @HostBinding('style.z-index')
  get zIndex() {
    return this?.config?.zIndex;
  }

  @HostBinding('style.display')
  get display() {
    return this?.config?.hidden ? 'none' : null;
  }

  @Input() config: DaLayoutConfig['sidebar']['secSidebar'];

  constructor(private layoutService: DaLayoutService) {
    this.layoutService
      .getLayoutConfig()
      .pipe(takeUntil(this.destroy$))
      .subscribe((config: DaLayoutConfig) => {
        this.config = config.sidebar.secSidebar;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(null);
    this.destroy$.complete();
  }
}

@Component({
  selector: 'da-layout-footer',
  template: '<ng-content></ng-content>',
})
export class DaLayoutFooterComponent implements OnDestroy {
  private destroy$ = new Subject();

  @HostBinding('class.da-layout-footer') default = true;
  @HostBinding('style.width')
  get height() {
    return this?.config?.height + 'px';
  }

  @HostBinding('style.display')
  get display() {
    return this?.config?.hidden ? 'none' : null;
  }

  @Input() config: DaLayoutConfig['footer'];

  constructor(private layoutService: DaLayoutService) {
    this.layoutService
      .getLayoutConfig()
      .pipe(takeUntil(this.destroy$))
      .subscribe((config: DaLayoutConfig) => {
        this.config = config.footer;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(null);
    this.destroy$.complete();
  }
}

@Component({
  selector: 'da-layout',
  templateUrl: './da-layout.component.html',
  styleUrls: ['./da-layout.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DaLayoutComponent implements OnDestroy {
  @HostBinding('class.da-layout') default = true;
  private destroy$ = new Subject();

  @Input() config: DaLayoutConfig;

  getSidebarWidth(): string {
    let width = 0;

    if (this.config.sidebar.hidden) {
      return width + 'px';
    }

    if (!this.config!.sidebar!.firSidebar!.hidden) {
      width += this.config!.sidebar!.firSidebar!.width!;
    }

    if (!this.config!.sidebar!.secSidebar!.hidden) {
      width += this.config!.sidebar!.secSidebar!.width!;
    }

    return width + 'px';
  }

  getHeaderHeight(): string {
    let height = 0;

    if (this.config!.header!.hidden) {
      return height + 'px';
    }

    if (!this.config!.header!.firHeader!.hidden) {
      height += this.config!.header!.firHeader!.height!;
    }

    if (!this.config!.header!.secHeader!.hidden) {
      height += this.config!.header!.secHeader!.height!;
    }

    return height + 'px';
  }

  constructor(private layoutService: DaLayoutService) {
    this.layoutService
      .getLayoutConfig()
      .pipe(takeUntil(this.destroy$))
      .subscribe((config: DaLayoutConfig) => {
        this.config = config;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(null);
    this.destroy$.complete();
  }
}
