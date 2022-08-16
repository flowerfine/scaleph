export default [
  {
    path: '/',
    redirect: '/studio/databoard',
  },
  {
    path: '/login',
    layout: false,
    component: './User/Login',
  },
  {
    path: '/register',
    layout: false,
    component: './User/Register',
  },
  {
    path: '/user/center',
    component: './User',
  },
  {
    name: 'studio',
    path: '/studio',
    icon: 'codeSandbox',
    routes: [
      {
        path: '/studio',
        redirect: '/studio/databoard',
      },
      {
        name: 'databoard',
        path: '/studio/databoard',
        icon: 'dashboard',
        exact: true,
        component: './Studio/DataBoard',
      },
    ],
  },
  {
    name: 'datadev',
    path: '/datadev',
    icon: 'desktop',
    routes: [
      {
        path: '/datadev',
        redirect: '/datadev/datasource',
      },
      {
        name: 'datasource',
        path: '/datadev/datasource',
        icon: 'link',
        exact: true,
        component: './DI/DataSource',
      },
      {
        name: 'project',
        path: '/datadev/project',
        icon: 'project',
        exact: true,
        component: './DI/Project',
      },
      {
        name: 'resource',
        path: '/datadev/resource',
        icon: 'fileText',
        exact: true,
        component: './DI/Resource',
      },
      {
        name: 'cluster',
        path: '/datadev/cluster',
        icon: 'cluster',
        exact: true,
        component: './DI/Cluster',
      },
    ],
  },
  {
    name: 'opscenter',
    path: '/opscenter',
    icon: 'lineChart',
    routes: [
      {
        path: '/opscenter',
        redirect: '/opscenter/batch',
      },
      {
        name: 'batch',
        path: '/opscenter/batch',
        icon: 'menu',
        exact: true,
        component: './OpsCenter/BatchJob',
      },
      {
        name: 'realtime',
        path: '/opscenter/realtime',
        icon: 'menu',
        exact: true,
        component: './OpsCenter/RealtimeJob',
      },
    ],
  },
  {
    name: 'stdata',
    path: '/stdata',
    icon: 'database',
    routes: [
      {
        path: '/stdata',
        redirect: '/stdata/dataElement',
      },
      {
        name: 'dataElement',
        path: '/stdata/dataElement',
        icon: 'menu',
        exact: true,
        component: './Stdata/DataElement',
      },
      {
        name: 'refdata',
        path: '/stdata/refdata',
        icon: 'menu',
        exact: true,
        component: './Stdata/RefData',
      },
      {
        name: 'refdataMap',
        path: '/stdata/refdataMap',
        icon: 'menu',
        exact: true,
        component: './Stdata/RefDataMap',
      },
      {
        name: 'system',
        path: '/stdata/system',
        icon: 'menu',
        exact: true,
        component: './Stdata/System',
      },
    ],
  },
  {
    name: 'admin',
    path: '/admin',
    icon: 'setting',
    routes: [
      {
        path: '/admin',
        redirect: '/admin/user',
      },
      {
        name: 'user',
        path: '/admin/user',
        icon: 'user',
        exact: true,
        component: './Admin/User',
      },
      {
        name: 'privilege',
        path: '/admin/privilege',
        icon: 'team',
        exact: true,
        component: './Admin/Privilege',
      },
      {
        name: 'dict',
        path: '/admin/dict',
        icon: 'table',
        exact: true,
        component: './Admin/Dict',
      },
      {
        name: 'setting',
        path: '/admin/setting',
        icon: 'setting',
        exact: true,
        component: './Admin/Setting',
      },
    ],
  },
  {
    component: './404',
  },
];
