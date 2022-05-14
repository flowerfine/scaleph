# Usage Example

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

#Invoking Example of Switching Layout

```ts
//We provide multiple default layout configurations: normal layout, waterfall layout, wide layout, top layout, left and right layout, and sidebar layout.
import {COMMON_LAYOUT_CONFIG, WATERFALL_LAYOUT_CONFIG, WIDE_LAYOUT_CONFIG} from'da-layout';
import {DaLayoutConfig} from'da-layout';

constructor(
private layoutService: DaLayoutService,
) {}

layoutService.updateLayoutConfig(config: DaLayoutConfig)
```

# Set the default layout.
+ Place the layout configuration you want in `default-layout.config.ts`.
