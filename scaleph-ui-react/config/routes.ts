import {PRIVILEGE_CODE} from '../src/constant';

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
        redirect: '/workspace/artifact',
        pCode: PRIVILEGE_CODE.workspaceJobShow,
        access: 'normalRouteFilter',
      },
      {
        name: 'project.artifact',
        path: '/workspace/artifact',
        icon: 'code',
        pCode: PRIVILEGE_CODE.workspaceShow,
        access: 'normalRouteFilter',
        routes: [
          {
            path: '/workspace/artifact',
            redirect: '/workspace/artifact/jar',
            pCode: PRIVILEGE_CODE.workspaceJobShow,
            access: 'normalRouteFilter',
          },
          {
            name: 'jar',
            path: '/workspace/artifact/jar',
            exact: true,
            component: './Project/Workspace/Artifact/Jar',
            pCode: PRIVILEGE_CODE.workspaceJobArtifactShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/artifact/history',
            exact: true,
            component: './Project/Workspace/Artifact/Jar/History',
            pCode: PRIVILEGE_CODE.workspaceJobArtifactJarShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'sql',
            path: '/workspace/artifact/sql',
            exact: true,
            component: './Project/Workspace/Artifact/Sql',
            pCode: PRIVILEGE_CODE.workspaceJobSqlShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/artifact/editor',
            exact: true,
            component: './Project/Workspace/Artifact/Sql/CodeEditor',
            pCode: PRIVILEGE_CODE.workspaceJobSqlShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'seatunnel',
            path: '/workspace/artifact/seatunnel',
            exact: true,
            component: './Project/Workspace/Artifact/DI/DiJobView',
            pCode: PRIVILEGE_CODE.workspaceJobSeaTunnelShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/artifact/seatunnel/dag',
            exact: true,
            component: './Project/Workspace/Artifact/DI/DiJobFlow',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigOptionsShow,
            access: 'normalRouteFilter'
          },
        ]
      },
      {
        name: 'project.flink.kubernetes',
        path: '/workspace/flink/kubernetes',
        icon: 'deploymentUnit',
        pCode: PRIVILEGE_CODE.workspaceClusterShow,
        access: 'normalRouteFilter',
        routes: [
          {
            path: '/workspace/flink/kubernetes',
            redirect: '/workspace/flink/kubernetes/template',
            pCode: PRIVILEGE_CODE.workspaceJobShow,
            access: 'normalRouteFilter',
          },
          {
            name: 'template',
            path: '/workspace/flink/kubernetes/template',
            exact: true,
            component: './Project/Workspace/Kubernetes/Template',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/flink/kubernetes/template/detail',
            exact: true,
            component: './Project/Workspace/Kubernetes/Template/Detail',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'session-cluster',
            path: '/workspace/flink/kubernetes/session-cluster',
            exact: true,
            component: './Project/Workspace/Kubernetes/SessionCluster',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/flink/kubernetes/session-cluster/steps',
            exact: true,
            component: './Project/Workspace/Kubernetes/SessionCluster/Steps',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/flink/kubernetes/session-cluster/detail',
            exact: true,
            component: './Project/Workspace/Kubernetes/SessionCluster/Detail',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'deployment',
            path: '/workspace/flink/kubernetes/deployment',
            exact: true,
            component: './Project/Workspace/Kubernetes/Deployment',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/flink/kubernetes/deployment/steps',
            exact: true,
            component: './Project/Workspace/Kubernetes/Deployment/Steps',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/flink/kubernetes/deployment/detail',
            exact: true,
            component: './Project/Workspace/Kubernetes/Deployment/Detail',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'job',
            path: '/workspace/flink/kubernetes/job',
            exact: true,
            component: './Project/Workspace/Kubernetes/Job',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/flink/kubernetes/job/detail',
            exact: true,
            component: './Project/Workspace/Kubernetes/Job/Detail',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
        ]
      },
      {
        name: 'project.doris',
        path: '/workspace/doris',
        icon: 'solution',
        pCode: PRIVILEGE_CODE.workspaceClusterShow,
        access: 'normalRouteFilter',
        routes: [
          {
            path: '/workspace/doris',
            redirect: '/workspace/doris/template',
            pCode: PRIVILEGE_CODE.workspaceJobShow,
            access: 'normalRouteFilter',
          },
          {
            name: 'template',
            path: '/workspace/doris/template',
            exact: true,
            component: './Project/Workspace/Doris/Template',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/doris/template/steps',
            exact: true,
            component: './Project/Workspace/Doris/Template/Steps',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/doris/template/detail',
            exact: true,
            component: './Project/Workspace/Doris/Template/Detail',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            name: 'instance',
            path: '/workspace/doris/instance',
            exact: true,
            component: './Project/Workspace/Doris/Instance',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
          {
            path: '/workspace/doris/instance/steps',
            exact: true,
            component: './Project/Workspace/Doris/Instance/Steps',
            pCode: PRIVILEGE_CODE.workspaceClusterConfigShow,
            access: 'normalRouteFilter'
          },
        ]
      },
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
