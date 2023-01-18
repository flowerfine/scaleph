import { PRIVILEGE_CODE } from '../src/constant';

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
    pCode: PRIVILEGE_CODE.studioShow,
    access: 'normalRouteFilter',
    routes: [
      {
        path: '/studio',
        redirect: '/studio/databoard',
        pCode: PRIVILEGE_CODE.studioShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'databoard',
        path: '/studio/databoard',
        icon: 'dashboard',
        exact: true,
        component: './Studio/DataBoard',
        pCode: PRIVILEGE_CODE.studioDataBoardShow,
        access: 'normalRouteFilter',
      },
    ],
  },
  {
    name: 'project',
    path: '/project',
    icon: 'project',
    pCode: PRIVILEGE_CODE.projectShow,
    routes: [
      {
        path: '/project',
        exact: true,
        component: './Project',
        pCode: PRIVILEGE_CODE.projectShow,
        access: 'normalRouteFilter'
      },
    ],
  },
  {
    path: '/workspace',
    pCode: PRIVILEGE_CODE.workspaceShow,
    access: 'normalRouteFilter',
    routes: [
      {
        path: '/workspace',
        redirect: '/workspace/job',
        pCode: PRIVILEGE_CODE.workspaceJobShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'project.job',
        path: '/workspace/job',
        icon: 'code',
        pCode: PRIVILEGE_CODE.workspaceShow,
        access: 'normalRouteFilter',
        routes: [
          {
            path: '/workspace/job',
            redirect: '/workspace/job/list',
            pCode: PRIVILEGE_CODE.workspaceJobShow,
            access: 'normalRouteFilter',
          },
          {
            name: 'list',
            path: '/workspace/job/list',
            exact: true,
            component: './Project/Workspace/Job',
            pCode: PRIVILEGE_CODE.workspaceJobShow,
            access: 'normalRouteFilter',
          },
          {
            path: '/workspace/job/detail',
            exact: true,
            component: './Project/Workspace/Job/Detail',
            pCode: PRIVILEGE_CODE.workspaceJobDetailShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'artifact',
            path: '/workspace/job/artifact',
            exact: true,
            component: './Project/Workspace/Job/Artifact',
            pCode: PRIVILEGE_CODE.workspaceJobArtifactShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/job/artifact/jar',
            exact: true,
            component: './Project/Workspace/Job/Artifact/Jar',
            pCode: PRIVILEGE_CODE.workspaceJobArtifactJarShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'sql',
            path: '/workspace/job/sql',
            exact: true,
            component: './Project/Workspace/Job/Sql',
            pCode: PRIVILEGE_CODE.workspaceJobSqlShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'seatunnel',
            path: '/workspace/job/seatunnel',
            exact: true,
            component: './Project/Workspace/Job/DI/DiJobView',
            pCode: PRIVILEGE_CODE.workspaceJobSeaTunnelShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/job/seatunnel/dag',
            exact: true,
            component: './Project/Workspace/Job/DI/DiJobFlow',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigOptionsShow,
            access: 'normalRouteFilter'
          },
        ]
      },
      {
        name: 'project.cluster',
        path: '/workspace/cluster',
        icon: 'deploymentUnit',
        pCode: PRIVILEGE_CODE.workspaceClusterShow,
        access: 'normalRouteFilter',
        routes: [
          {
            name: 'config',
            path: '/workspace/cluster/config',
            exact: true,
            component: './Project/Workspace/Cluster/Config',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/cluster/config/options',
            exact: true,
            component: './Project/Workspace/Cluster/Config/Options',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigOptionsShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'instance',
            path: '/workspace/cluster/instance',
            exact: true,
            component: './Project/Workspace/Cluster/Instance',
            pCode: PRIVILEGE_CODE.workspaceClusterInstanceShow,
            access: 'normalRouteFilter'
          }
        ]
      },
      {
        name: '流程可视化',
        path: '/workspace/xflow',
        icon: 'smile',
        component: './Project/Workspace/Job/DI/Xflow',
        pCode: PRIVILEGE_CODE.workspaceClusterConfigOptionsShow,
        access: 'normalRouteFilter'
      }
    ]
  },
  {
    name: 'resource',
    path: '/resource',
    icon: 'fileText',
    pCode: PRIVILEGE_CODE.resourceShow,
    access: 'normalRouteFilter',
    routes: [
      {
        path: '/resource',
        redirect: '/resource/jar',
        pCode: PRIVILEGE_CODE.resourceJarShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'jar',
        path: '/resource/jar',
        exact: true,
        component: './Resource/Jar',
        pCode: PRIVILEGE_CODE.resourceJarShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'flinkRelease',
        path: '/resource/flink-release',
        exact: true,
        component: './Resource/FlinkRelease',
        pCode: PRIVILEGE_CODE.resourceFlinkReleaseShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'seatunnelRelease',
        path: '/resource/seatunnel-release',
        exact: true,
        component: './Resource/SeaTunnelRelease',
        pCode: PRIVILEGE_CODE.resourceSeaTunnelReleaseShow,
        access: 'normalRouteFilter'
      },
      {
        path: '/resource/seatunnel-release/connectors',
        exact: true,
        component: './Resource/SeaTunnelConnector',
        pCode: PRIVILEGE_CODE.resourceSeaTunnelReleaseShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'kerberos',
        path: '/resource/kerberos',
        exact: true,
        component: './Resource/Kerberos',
        pCode: PRIVILEGE_CODE.resourceKerberosShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'clusterCredential',
        path: '/resource/cluster-credential',
        exact: true,
        component: './Resource/ClusterCredential',
        pCode: PRIVILEGE_CODE.resourceClusterCredentialShow,
        access: 'normalRouteFilter'
      },
      {
        path: '/resource/cluster-credential/file',
        exact: true,
        component: './Resource/CredentialFile',
        pCode: PRIVILEGE_CODE.resourceClusterCredentialShow,
        access: 'normalRouteFilter'
      }
    ]
  },
  {
    name: 'dataSource',
    path: '/dataSource',
    icon: 'compass',
    pCode: PRIVILEGE_CODE.dataSourceShow,
    access: 'normalRouteFilter',
    routes: [
      {
        path: '/dataSource',
        exact: true,
        component: './DataSource',
        pCode: PRIVILEGE_CODE.dataSourceShow,
        access: 'normalRouteFilter'
      },
      {
        path: '/dataSource/stepForms',
        exact: true,
        component: './DataSource/StepForms',
        pCode: PRIVILEGE_CODE.dataSourceShow,
        access: 'normalRouteFilter'
      }
    ]
  },
  // {
  //   name: 'dataService',
  //   path: '/dataService',
  //   icon: 'function',
  //   pCode: PRIVILEGE_CODE.stdataShow,
  //   access: 'normalRouteFilter',
  //   routes: [
  //     {
  //       path: '/dataService',
  //       redirect: '/dataService/schema',
  //       pCode: PRIVILEGE_CODE.stdataShow,
  //       access: 'normalRouteFilter'
  //     },
  //     {
  //       name: 'schema',
  //       path: '/dataService/schema',
  //       icon: 'insertRowAbove',
  //       exact: true,
  //       component: './DataService',
  //       pCode: PRIVILEGE_CODE.stdataSystemShow,
  //       access: 'normalRouteFilter'
  //     }
  //   ]
  // },
  {
    name: 'stdata',
    path: '/stdata',
    icon: 'database',
    pCode: PRIVILEGE_CODE.stdataShow,
    access: 'normalRouteFilter',
    routes: [
      {
        path: '/stdata',
        redirect: '/stdata/system',
        pCode: PRIVILEGE_CODE.stdataShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'system',
        path: '/stdata/system',
        icon: 'group',
        exact: true,
        component: './Stdata/System',
        pCode: PRIVILEGE_CODE.stdataSystemShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'dataElement',
        path: '/stdata/dataElement',
        icon: 'hdd',
        exact: true,
        component: './Stdata/DataElement',
        pCode: PRIVILEGE_CODE.stdataDataElementShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'refdata',
        path: '/stdata/refdata',
        icon: 'profile',
        exact: true,
        component: './Stdata/RefData',
        pCode: PRIVILEGE_CODE.stdataRefDataShow,
        access: 'normalRouteFilter'
      },
      {
        path: '/stdata/refdata/value',
        exact: true,
        component: './Stdata/RefData/Value',
        pCode: PRIVILEGE_CODE.stdataRefDataShow,
        access: 'normalRouteFilter'
      },
      {
        name: 'refdataMap',
        path: '/stdata/refdataMap',
        icon: 'oneToOne',
        exact: true,
        component: './Stdata/RefDataMap',
        pCode: PRIVILEGE_CODE.stdataRefDataMapShow,
        access: 'normalRouteFilter'
      }
    ]
  },
  {
    name: 'admin',
    path: '/admin',
    icon: 'setting',
    pCode: PRIVILEGE_CODE.adminShow,
    access: 'normalRouteFilter',
    routes: [
      {
        path: '/admin',
        redirect: '/admin/dept',
        pCode: PRIVILEGE_CODE.adminShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'dept',
        path: '/admin/dept',
        icon: 'apartment',
        exact: true,
        component: './Admin/Dept',
        pCode: PRIVILEGE_CODE.deptShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'role',
        path: '/admin/role',
        icon: 'safety',
        exact: true,
        component: './Admin/Role',
        pCode: PRIVILEGE_CODE.roleShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'user',
        path: '/admin/user',
        icon: 'user',
        exact: true,
        component: './Admin/User',
        pCode: PRIVILEGE_CODE.userShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'resource.web',
        path: '/admin/resource/web',
        icon: 'team',
        exact: true,
        component: './Admin/Resource/Web',
        pCode: PRIVILEGE_CODE.resourceWebShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'privilege',
        path: '/admin/privilege',
        icon: 'team',
        exact: true,
        component: './Admin/Privilege',
        pCode: PRIVILEGE_CODE.privilegeShow,
        access: 'normalRouteFilter',
      },

      {
        name: 'quartz',
        path: '/admin/workflow/quartz',
        icon: 'fieldTime',
        exact: true,
        component: './Workflow/Definition/Quartz',
        pCode: PRIVILEGE_CODE.workflowQuartzShow,
        access: 'normalRouteFilter',
      },
      {
        path: '/admin/workflow/quartz/task',
        exact: true,
        component: './Workflow/Definition/Quartz/Task',
        pCode: PRIVILEGE_CODE.workflowQuartzShow,
        access: 'normalRouteFilter',
      },
      {
        path: '/admin/workflow/schedule',
        exact: true,
        component: './Workflow/Schedule',
        pCode: PRIVILEGE_CODE.workflowQuartzShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'dict',
        path: '/admin/dict',
        icon: 'table',
        exact: true,
        component: './Admin/Dict',
        pCode: PRIVILEGE_CODE.dictShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'setting',
        path: '/admin/setting',
        icon: 'setting',
        exact: true,
        component: './Admin/Setting',
        pCode: PRIVILEGE_CODE.settingShow,
        access: 'normalRouteFilter',
      },
    ],
  },
  {
    component: './404',
  },
];
