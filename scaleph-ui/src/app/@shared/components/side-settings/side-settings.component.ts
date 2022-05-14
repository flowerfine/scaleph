import { Component, Input, OnDestroy } from '@angular/core';
import { Clipboard } from '@angular/cdk/clipboard';
import { DaLayoutService } from '../../layouts/da-layout/da-layout.service';
import { DaLayoutConfig, LEFT_RIGHT_LAYOUT_CONFIG, SIDEBAR_LAYOUT_CONFIG, TOP_NAV_LAYOUT_CONFIG } from '../../layouts/da-layout';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { TranslateService, TranslationChangeEvent } from '@ngx-translate/core';

@Component({
  selector: 'da-side-settings',
  templateUrl: './side-settings.component.html',
  styleUrls: ['./side-settings.component.scss'],
})
export class SideSettingsComponent implements OnDestroy {
  @Input() close: any;

  private destroy$ = new Subject();

  layoutConfig: DaLayoutConfig;
  layout: string | null;
  helpContent: string;
  msgs: Array<Object> = [];

  i18nValues: any;

  sidebarNotice: any = {};

  private change: number;
  private compare: { [key: string]: number };

  constructor(
    private clipboard: Clipboard,
    private layoutService: DaLayoutService,
    // private mediaQueryService: DaScreenMediaQueryService,
    private translate: TranslateService
  ) {
    // this.mediaQueryService
    //   .getPoint()
    //   .pipe(takeUntil(this.destroy$))
    //   .subscribe(({ currentPoint, change, compare }) => {
    //     this.change = change;
    //     this.compare = compare;
    //   });
  }

  refreshReactiveLayout() {
    /* ml：sidebar shrink breakpoint */
    if (this.change <= 0 && this.compare['ml'] <= 0) {
      this.sidebarShrink(true);
    } else if (this.change >= 0 && this.compare['ml'] > 0) {
      this.sidebarShrink(false);
    }

    /* mm：sidebar hidden breakpoint */
    if (this.change <= 0 && this.compare['mm'] <= 0) {
      this.sidebarFold(true);
    } else if (this.change >= 0 && this.compare['mm'] > 0) {
      this.sidebarFold(false);
    }
  }

  ngOnInit(): void {
    this.initLayoutConfig();

    if (localStorage.getItem('da-layout-id')) {
      this.layout = localStorage.getItem('da-layout-id');
    } else {
      this.layout = 'left-right';
    }
  }

  handleLayoutClicked(layout: string) {
    this.layout = layout;
    localStorage.setItem('da-layout-id', layout);
    if (layout === 'topNav') {
      this.layoutConfig = TOP_NAV_LAYOUT_CONFIG;
    } else if (layout === 'sidebar') {
      this.layoutConfig = SIDEBAR_LAYOUT_CONFIG;
    } else if (layout === 'left-right') {
      this.layoutConfig = LEFT_RIGHT_LAYOUT_CONFIG;
    }

    this.refreshReactiveLayout();
    this.layoutService.updateLayoutConfig(this.layoutConfig);
    window.dispatchEvent(new Event('resize'));
  }

  initLayoutConfig() {
    this.layoutService.getLayoutConfig().subscribe((layout) => {
      this.layoutConfig = layout;
    });
  }

  onCopyClicked() {
    let isSucceeded = false;
    const isSupported = !!document.queryCommandSupported && !!document.queryCommandSupported('copy') && !!window;
    if (isSupported) {
      isSucceeded = this.clipboard.copy(JSON.stringify(this.layoutConfig, null, 2));
      if (isSucceeded) {
        this.msgs = [
          {
            severity: 'success',
            summary: this.translate.instant('side-setting.copy-summary'),
            content: this.translate.instant('side-setting.copy-content'),
          },
        ];
      }
    }
  }

  updateLayout() {
    this.layoutService.updateLayoutConfig(this.layoutConfig);
    window.dispatchEvent(new Event('resize'));
  }

  sidebarShrink(isShrink: boolean) {
    if (this.layoutConfig.sidebar.firSidebar) {
      this.layoutConfig.sidebar.firSidebar.width = isShrink ? 54 : 240;
    }
    this.layoutConfig.sidebar.shrink = isShrink;
  }

  sidebarFold(isFold: boolean) {
    if (this.layoutConfig.sidebar.firSidebar) {
      this.layoutConfig.sidebar.firSidebar.hidden = isFold;
    }
  }
  
  ngOnDestroy() {
    this.destroy$.next(null);
    this.destroy$.complete();
  }
}
