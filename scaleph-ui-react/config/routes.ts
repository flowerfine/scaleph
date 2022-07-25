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
    name: 'studio',
    path: 'studio',
    icon: 'codeSandbox',
    routes: [
      {
        name: 'databoard',
        path: 'databoard',
        icon: 'dashboard',
        component: './Studio/DataBoard',
      },
    ],
  },
  {
    name: 'datadev',
    path: 'datadev',
    icon: 'desktop',
    routes: [
      {
        name: 'datasource',
        path: 'datasource',
        icon: 'menu',
        component: './DI/DataSource',
      },
      {
        name: 'project',
        path: 'project',
        icon: 'menu',
        component: './DI/Project',
      },
      {
        name: 'resource',
        path: 'resource',
        icon: 'menu',
        component: './DI/Resource',
      },
      {
        name: 'cluster',
        path: 'cluster',
        icon: 'menu',
        component: './DI/Cluster',
      },
    ],
  },
  {
    name: 'opscenter',
    path: 'opscenter',
    icon: 'lineChart',
    routes: [
      {
        name: 'batch',
        path: 'batch',
        icon: 'menu',
        component: './OpsCenter/BatchJob',
      },
      {
        name: 'realtime',
        path: 'realtime',
        icon: 'menu',
        component: './OpsCenter/RealtimeJob',
      },
    ],
  },
  {
    name: 'stdata',
    path: 'stdata',
    icon: 'database',
    routes: [
      {
        name: 'dataElement',
        path: 'dataElement',
        icon: 'menu',
        component: './Stdata/DataElement',
      },
      {
        name: 'refdata',
        path: 'refdata',
        icon: 'menu',
        component: './Stdata/RefData',
      },
      {
        name: 'refdataMap',
        path: 'refdataMap',
        icon: 'menu',
        component: './Stdata/RefDataMap',
      },
      {
        name: 'system',
        path: 'system',
        icon: 'menu',
        component: './Stdata/System',
      },
    ],
  },
  {
    name: 'admin',
    path: 'admin',
    icon: 'setting',
    routes: [
      {
        name: 'user',
        path: 'user',
        icon: 'user',
        component: './Admin/User',
      },
      {
        name: 'privilege',
        path: 'privilege',
        icon: 'team',
        component: './Admin/Privilege',
      },
      {
        name: 'dict',
        path: 'dict',
        icon: 'menu',
        component: './Admin/Dict',
      },
      {
        name: 'setting',
        path: 'setting',
        icon: 'setting',
        component: './Admin/Setting',
      },
    ],
  },
  // {
  //   path: '/admin',
  //   name: 'admin',
  //   icon: 'crown',
  //   access: 'canAdmin',
  //   routes: [
  //     {
  //       path: '/admin/sub-page',
  //       name: 'sub-page',
  //       icon: 'smile',
  //       component: './Welcome',
  //     },
  //     {
  //       component: './404',
  //     },
  //   ],
  // },
  {
    component: './404',
  },
];
