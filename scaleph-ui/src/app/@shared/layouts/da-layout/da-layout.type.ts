export interface DaLayoutConfig {
  id: string;
  mode?: 'sidebarTop' | 'headerTop';
  header?: {
    fixed?: boolean;
    hidden?: boolean;
    zIndex?: number;
    firHeader?: {
      height?: number;
      hidden?: boolean;
      zIndex?: number;
    };
    secHeader?: {
      height?: number;
      hidden?: boolean;
      zIndex?: number;
    };
    [key: string]: any;
  };
  sidebar: {
    fixed?: boolean;
    hidden?: boolean;
    zIndex?: number;
    firSidebar?: {
      width?: number;
      hidden?: boolean;
      zIndex?: number;
    };
    secSidebar?: {
      width?: number;
      hidden?: boolean;
      zIndex?: number;
    };
    [key: string]: any;
  };
  footer?: {
    height?: number;
    hidden?: boolean;
  };
  [key: string]: any;
}
