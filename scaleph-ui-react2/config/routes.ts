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
        component: './Studio/DataBoard',
      },
    ],
  },
  {
    name: 'project',
    path: '/project',
    icon: 'project',
    routes: [
      {
        path: '/project',
        component: './Project',
      },
    ],
  },
  {
    path: '/workspace',
    routes: [
      {
        path: '/workspace',
        redirect: '/workspace/artifact',
      },
      {
        name: 'project.artifact',
        path: '/workspace/artifact',
        icon: 'code',
        routes: [
          {
            path: '/workspace/artifact',
            redirect: '/workspace/artifact/jar',
          },
          {
            name: 'jar',
            path: '/workspace/artifact/jar',
            component: './Project/Workspace/Artifact/Jar',
          },
          {
            path: '/workspace/artifact/history',
            component: './Project/Workspace/Artifact/Jar/History',
          },
          {
            name: 'sql',
            path: '/workspace/artifact/sql',
            component: './Project/Workspace/Artifact/Sql',
          },
          {
            path: '/workspace/artifact/editor',
            component: './Project/Workspace/Artifact/Sql/CodeEditor',
          },
          {
            name: 'seatunnel',
            path: '/workspace/artifact/seatunnel',
            component: './Project/Workspace/Artifact/DI/DiJobView',
          },
          {
            path: '/workspace/artifact/seatunnel/dag',
            component: './Project/Workspace/Artifact/DI/DiJobFlow',
          },
        ]
      },
      {
        name: 'project.flink.kubernetes',
        path: '/workspace/flink/kubernetes',
        icon: 'deploymentUnit',
        routes: [
          {
            path: '/workspace/flink/kubernetes',
            redirect: '/workspace/flink/kubernetes/template',
          },
          {
            name: 'template',
            path: '/workspace/flink/kubernetes/template',
            component: './Project/Workspace/Kubernetes/Template',
          },
          {
            path: '/workspace/flink/kubernetes/template/steps/new',
            component: './Project/Workspace/Kubernetes/Template/Steps/New',
          },
          {
            path: '/workspace/flink/kubernetes/template/steps/update',
            component: './Project/Workspace/Kubernetes/Template/Steps/Update',
          },
          {
            name: 'session-cluster',
            path: '/workspace/flink/kubernetes/session-cluster',
            component: './Project/Workspace/Kubernetes/SessionCluster',
          },
          {
            path: '/workspace/flink/kubernetes/session-cluster/steps/new',
            component: './Project/Workspace/Kubernetes/SessionCluster/Steps/New',
          },
          {
            path: '/workspace/flink/kubernetes/session-cluster/steps/update',
            component: './Project/Workspace/Kubernetes/SessionCluster/Steps/Update',
          },
          {
            path: '/workspace/flink/kubernetes/session-cluster/detail',
            component: './Project/Workspace/Kubernetes/SessionCluster/Detail',
          },
          {
            name: 'deployment',
            path: '/workspace/flink/kubernetes/deployment',
            component: './Project/Workspace/Kubernetes/Deployment',
          },
          {
            path: '/workspace/flink/kubernetes/deployment/steps/new',
            component: './Project/Workspace/Kubernetes/Deployment/Steps/New',
          },
          {
            path: '/workspace/flink/kubernetes/deployment/steps/update',
            component: './Project/Workspace/Kubernetes/Deployment/Steps/Update',
          },
          {
            path: '/workspace/flink/kubernetes/deployment/detail',
            component: './Project/Workspace/Kubernetes/Deployment/Detail',
          },
          {
            name: 'job',
            path: '/workspace/flink/kubernetes/job',
            component: './Project/Workspace/Kubernetes/Job',
          },
          {
            path: '/workspace/flink/kubernetes/job/detail',
            component: './Project/Workspace/Kubernetes/Job/Detail',
          },
        ]
      },
      {
        name: 'project.doris',
        path: '/workspace/doris',
        icon: 'solution',
        routes: [
          {
            path: '/workspace/doris',
            redirect: '/workspace/doris/template',
          },
          {
            name: 'template',
            path: '/workspace/doris/template',
            component: './Project/Workspace/Doris/OperatorTemplate',
          },
          {
            path: '/workspace/doris/template/steps',
            component: './Project/Workspace/Doris/OperatorTemplate/Steps',
          },
          {
            path: '/workspace/doris/template/detail',
            component: './Project/Workspace/Doris/OperatorTemplate/Detail',
          },
          {
            name: 'instance',
            path: '/workspace/doris/instance',
            component: './Project/Workspace/Doris/OperatorInstance',
          },
          {
            path: '/workspace/doris/instance/steps',
            component: './Project/Workspace/Doris/OperatorInstance/Steps',
          },
          {
            path: '/workspace/doris/instance/detail',
            component: './Project/Workspace/Doris/OperatorInstance/Detail',
          },
        ]
      },
    ]
  },
  {
    name: 'resource',
    path: '/resource',
    icon: 'fileText',
    routes: [
      {
        path: '/resource',
        redirect: '/resource/jar',
      },
      {
        name: 'jar',
        path: '/resource/jar',
        component: './Resource/Jar',
      },
      {
        name: 'flinkRelease',
        path: '/resource/flink-release',
        component: './Resource/FlinkRelease',
      },
      {
        name: 'seatunnelRelease',
        path: '/resource/seatunnel-release',
        component: './Resource/SeaTunnelRelease',
      },
      {
        path: '/resource/seatunnel-release/connectors',
        component: './Resource/SeaTunnelConnector',
      },
      {
        name: 'kerberos',
        path: '/resource/kerberos',
        component: './Resource/Kerberos',
      },
      {
        name: 'clusterCredential',
        path: '/resource/cluster-credential',
        component: './Resource/ClusterCredential',
      }
    ]
  },
  {
    name: 'dataSource',
    path: '/dataSource',
    icon: 'compass',
    routes: [
      {
        path: '/dataSource',
        component: './DataSource',
      },
      {
        path: '/dataSource/stepForms',
        component: './DataSource/StepForms',
      }
    ]
  },
  {
    name: 'stdata',
    path: '/stdata',
    icon: 'database',
    routes: [
      {
        path: '/stdata',
        redirect: '/stdata/system',
      },
      {
        name: 'system',
        path: '/stdata/system',
        icon: 'group',
        component: './Stdata/System',
      },
      {
        name: 'dataElement',
        path: '/stdata/dataElement',
        icon: 'hdd',
        component: './Stdata/DataElement',
      },
      {
        name: 'refdata',
        path: '/stdata/refdata',
        icon: 'profile',
        component: './Stdata/RefData',
      },
      {
        path: '/stdata/refdata/value',
        component: './Stdata/RefData/Value',
      },
      {
        name: 'refdataMap',
        path: '/stdata/refdataMap',
        icon: 'oneToOne',
        component: './Stdata/RefDataMap',
      }
    ]
  },
  {
    name: 'admin',
    path: '/admin',
    icon: 'setting',
    routes: [
      {
        path: '/admin',
        redirect: '/admin/dept',
      },
      {
        name: 'dept',
        path: '/admin/dept',
        icon: 'apartment',
        component: './Admin/Dept',
      },
      {
        name: 'role',
        path: '/admin/role',
        icon: 'safety',
        component: './Admin/Role',
      },
      {
        name: 'user',
        path: '/admin/user',
        icon: 'user',
        component: './Admin/User',
      },
      {
        name: 'resource.web',
        path: '/admin/resource/web',
        icon: 'team',
        component: './Admin/Resource/Web',
      },
      {
        name: 'privilege',
        path: '/admin/privilege',
        icon: 'team',
        component: './Admin/Privilege',
      },
      {
        name: 'quartz',
        path: '/admin/workflow/quartz',
        icon: 'fieldTime',
        component: './Workflow/Definition/Quartz',
      },
      {
        path: '/admin/workflow/quartz/task',
        component: './Workflow/Definition/Quartz/Task',
      },
      {
        path: '/admin/workflow/schedule',
        component: './Workflow/Schedule',
      },
      {
        name: 'dict',
        path: '/admin/dict',
        icon: 'table',
        component: './Admin/Dict',
      },
      {
        name: 'setting',
        path: '/admin/setting',
        icon: 'setting',
        component: './Admin/Setting',
      },
    ],
  },
  {
    component: './404',
  },
];
