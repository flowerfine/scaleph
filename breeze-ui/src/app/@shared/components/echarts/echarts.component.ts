import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  HostBinding,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import * as echarts from 'echarts';
import { fromEvent } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

const DEFAULT_TEXT_COLOR = '#252b3a';
const DEFAULT_LINE_COLOR = '#adb0b8';

@Component({
  selector: 'd-echarts',
  template: '',
  changeDetection: ChangeDetectionStrategy.OnPush,
  exportAs: 'echarts',
  preserveWhitespaces: false,
})
export class EchartsComponent implements AfterViewInit, OnChanges, OnDestroy, OnInit {
  echart: any;
  @Input() options: any;
  @Input() notMerge: boolean;
  @Input() lazyUpdate: boolean;
  /**
   * echarts 主题
   */
  @Input() theme: string | Object;
  /**
   * 当echarts初始化完成后，会返回echarts实例
   */
  @Output() chartReady: EventEmitter<any> = new EventEmitter<any>();
  @Input() width = '100%';
  @Input() height = '400px';
  @Input() autoResize = true;
  @HostBinding('style.display') display = 'inline-block';
  @HostBinding('style.width') get hostWidth() {
    return this.width;
  }
  @HostBinding('style.height') get hostHeight() {
    return this.height;
  }
  resizeSub: any;
  textColor = DEFAULT_TEXT_COLOR;
  linecolor: string;
  // 主题色色盘
  themeColorArray = [
    '#5E7CE0',
    '#6CBFFF',
    '#50D4AB',
    '#A6DD82',
    '#FAC20A',
    '#FA9841',
    '#F66F6A',
    '#F3689A',
    '#A97AF8',
    '#207AB3',
    '#169E6C',
    '#7EBA50',
    '#B58200',
    '#B54E04',
    '#344899',
    '#572DB3',
    '#FFD4E3',
    '#B8E0FF',
    '#ACF2DC',
    '#D8FCC0',
    '#FFE794',
    '#FFD0A6',
    '#D8C2FF',
    '#BECCFA',
  ];
  constructor(private elementRef: ElementRef) {}

  get axisCommon() {
    return {
      axisLine: {
        lineStyle: {
          color: this.textColor,
        },
      },
      axisTick: {
        lineStyle: {
          color: this.textColor,
        },
      },
      axisLabel: {
        textStyle: {
          color: this.textColor,
        },
      },
      splitLine: {
        lineStyle: {
          type: 'dashed',
          color: this.linecolor,
        },
      },
      splitArea: {
        areaStyle: {
          color: this.textColor,
        },
      },
    };
  }

  get darkTheme() {
    return {
      // color: ['#dd6b66', '#759aa0', '#e69d87', '#8dc1a9', '#ea7e53', '#eedd78', '#73a373', '#73b9bc', '#7289ab', '#91ca8c', '#f49f42'],
      color: this.themeColorArray,
      title: {
        textStyle: {
          color: this.textColor,
        },
      },
      legend: {
        textStyle: {
          color: this.textColor,
        },
      },
      tooltip: {
        axisPointer: {
          lineStyle: {
            color: this.textColor,
          },
          crossStyle: {
            color: this.textColor,
          },
        },
      },
      timeAxis: this.axisCommon,
      logAxis: this.axisCommon,
      valueAxis: this.axisCommon,
      categoryAxis: this.axisCommon,
    };
  }

  get lightTheme() {
    return {
      color: this.themeColorArray,
      title: {
        textStyle: {
          color: this.textColor,
        },
      },
      legend: {
        textStyle: {
          color: this.textColor,
        },
      },
      tooltip: {
        axisPointer: {
          lineStyle: {
            color: this.textColor,
          },
          crossStyle: {
            color: this.textColor,
          },
        },
      },
      timeAxis: this.axisCommon,
      logAxis: this.axisCommon,
      valueAxis: this.axisCommon,
      categoryAxis: this.axisCommon,
    };
  }

  themeService: any;

  ngOnInit() {
    this.themeService = (window as { [key: string]: any })['devuiThemeService'];
  }

  ngAfterViewInit(): void {
    if (!this.theme && this.themeService && this.themeService.eventBus) {
      this.themeService.eventBus.add('themeChanged', this.themeChange);
    }
    this.initTheme();
    this.echart = (<any>echarts).init(this.elementRef.nativeElement, this.theme);
    this.updateChartData(this.options);
    this.chartReady.emit(this.echart);
    // 根据浏览器大小变化自动调整echarts
    if (this.autoResize) {
      this.resizeSub = fromEvent(window, 'resize')
        .pipe(debounceTime(100))
        .subscribe(() => {
          this.echart.resize();
        });
    }
  }

  initTheme() {
    if (this.themeService) {
      this.themeChange();
    } else {
      this.theme = this.theme || this.lightTheme;
    }
  }

  themeChange = () => {
    if (this.themeService.currentTheme.data) {
      this.textColor = this.themeService.currentTheme.data['devui-text'] || DEFAULT_TEXT_COLOR;
      this.linecolor = this.themeService.currentTheme.data['devui-line'] || DEFAULT_LINE_COLOR;
    }
    this.theme = this.themeService.currentTheme.isDark ? this.darkTheme : this.lightTheme;
    if (this.echart) {
      this.echart.dispose();
      this.echart = (<any>echarts).init(this.elementRef.nativeElement, this.theme);
      this.updateChartData(this.options);
      this.chartReady.emit(this.echart);
    }
  };

  ngOnChanges(changes: SimpleChanges) {
    if (this.echart && changes['options']) {
      const currentValue = changes['options'].currentValue;
      this.updateChartData(currentValue);
    }
  }

  ngOnDestroy(): void {
    if (this.echart) {
      this.echart.clear();
      this.echart.dispose();
    }

    if (this.themeService && this.themeService.eventBus) {
      this.themeService.eventBus.remove('themeChanged', this.themeChange);
    }

    if (this.resizeSub) {
      this.resizeSub.unsubscribe();
    }
  }

  updateChartData(options: any) {
    if (options) {
      this.echart.setOption(options, this.notMerge || false, this.lazyUpdate || false);
    }
  }
}
