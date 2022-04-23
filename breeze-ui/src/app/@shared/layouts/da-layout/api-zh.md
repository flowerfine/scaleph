# 使用示例

```html
<da-layout>
  <da-layout-header> header </da-layout-header>
  <da-layout-sec-header> secHeader </da-layout-sec-header>
  <da-layout-sidebar> sidebar </da-layout-sidebar>
  <da-layout-sec-sidebar> secSidebar </da-layout-sec-sidebar>
  <div>
    <router-outlet></router-outlet>
  </div>
  <da-layout-footer>
    <da-footer></da-footer>
  </da-layout-footer>
</da-layout>
```

# 切换布局调用示例

```ts
// 我们提供了多种默认布局配置：常规布局、瀑布式布局、宽幅布局、顶部布局、左右布局、sidebar布局等
import { COMMON_LAYOUT_CONFIG, WATERFALL_LAYOUT_CONFIG, WIDE_LAYOUT_CONFIG } from 'da-layout';
import { DaLayoutConfig } from 'da-layout';

constructor(
    private layoutService: DaLayoutService,
  ) {}

layoutService.updateLayoutConfig(config: DaLayoutConfig)
```

# 设置默认布局

- 将你需要的布局配置放在 `default-layout.config.ts` 即可。
