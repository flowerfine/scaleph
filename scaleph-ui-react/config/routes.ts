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
        component: './DI/DataSource',
      },
      {
        name: 'project',
        path: 'project',
        component: './DI/Project',
      },
      {
        name: 'resource',
        path: 'resource',
        component: './DI/Resource',
      },
      {
        name: 'cluster',
        path: 'cluster',
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
        component: './OpsCenter/BatchJob',
      },
      {
        name: 'realtime',
        path: 'realtime',
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
        component: './Stdata/DataElement',
      },
      {
        name: 'refdata',
        path: 'refdata',
        component: './Stdata/RefData',
      },
      {
        name: 'refdataMap',
        path: 'refdataMap',
        component: './Stdata/RefDataMap',
      },
      {
        name: 'system',
        path: 'system',
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
        component: './Admin/User',
      },
      {
        name: 'privilege',
        path: 'privilege',
        component: './Admin/Privilege',
      },
      {
        name: 'dict',
        path: 'dict',
        component: './Admin/Dict',
      },
      {
        name: 'setting',
        path: 'setting',
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
