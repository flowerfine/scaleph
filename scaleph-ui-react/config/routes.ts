import { PRIVILEGES } from "../src/constants";

/**
 * @name umi 的路由配置
 * @description 只支持 path,component,routes,redirect,wrappers,name,icon 的配置
 * @param path  path 只支持两种占位符配置，第一种是动态参数 :id 的形式，第二种是 * 通配符，通配符只能出现路由字符串的最后。
 * @param component 配置 location 和 path 匹配后用于渲染的 React 组件路径。可以是绝对路径，也可以是相对路径，如果是相对路径，会从 src/pages 开始找起。
 * @param routes 配置子路由，通常在需要为多个路径增加 layout 组件时使用。
 * @param redirect 配置路由跳转
 * @param wrappers 配置路由组件的包装组件，通过包装组件可以为当前的路由组件组合进更多的功能。 比如，可以用于路由级别的权限校验
 * @param name 配置路由的标题，默认读取国际化文件 menu.ts 中 menu.xxxx 的值，如配置 name 为 login，则读取 menu.ts 中 menu.login 的取值作为标题
 * @param icon 配置路由的图标，取值参考 https://ant.design/components/icon-cn， 注意去除风格后缀和大小写，如想要配置图标为 <StepBackwardOutlined /> 则取值应为 stepBackward 或 StepBackward，如想要配置图标为 <UserOutlined /> 则取值应为 user 或者 User
 * @doc https://umijs.org/docs/guides/routes
 */
export default [
  {
    path: "/user",
    layout: false,
    routes: [
      {
        name: "login",
        path: "/user/login",
        component: "./User/Login",
        exact: true,
      },
      {
        name: "register",
        path: "/user/register",
        component: "./User/Register",
        exact: true,
      },
    ],
  },
  {
    path: "/",
    redirect: "/studio",
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
        redirect: '/workspace/flink/kubernetes',
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
      {
        name: 'project.engine',
        path: '/workspace/engine',
        icon: 'deploymentUnit',
        routes: [
          {
            name: 'lake',
            path: '/workspace/engine/lake',
            icon: 'apartment',
            routes: [
              {
                name: 'iceberg',
                path: '/workspace/engine/lake/iceberg',
                icon: 'apartment',
                component: './Project/Workspace/Engine/Lake/Iceberg',
              },
              {
                name: 'paimon',
                path: '/workspace/engine/lake/paimon',
                icon: 'apartment',
                component: './Project/Workspace/Engine/Lake/Paimon',
              }
            ]
          },
          {
            name: 'olap',
            path: '/workspace/engine/olap',
            icon: 'apartment',
            routes: [
              {
                name: 'doris',
                path: '/workspace/engine/olap/doris',
                icon: 'apartment',
                component: './Project/Workspace/Engine/OLAP/Doris',
              },
              {
                name: 'starrocks',
                path: '/workspace/engine/olap/starrocks',
                icon: 'apartment',
                component: './Project/Workspace/Engine/OLAP/StarRocks',
              }
            ]
          },
          {
            name: 'compute',
            path: '/workspace/engine/compute',
            icon: 'apartment',
            routes: [
              {
                name: 'flink',
                path: '/workspace/engine/compute/flink',
                icon: 'apartment',
                component: './Project/Workspace/Engine/Compute/Flink',
              }
            ]
          },
        ]
      },
      {
        name: 'project.data-integration',
        path: '/workspace/data-integration',
        icon: 'deploymentUnit',
        routes: [
          {
            name: 'seatunnel',
            path: '/workspace/data-integration/seatunnel',
            icon: 'apartment',
            component: './Project/Workspace/DataIntegration/SeaTunnel',
          },
          {
            path: '/workspace/data-integration/seatunnel/dag',
            component: './Project/Workspace/DataIntegration/SeaTunnel/Dag',
          },
          {
            name: 'flink-cdc',
            path: '/workspace/data-integration/flink-cdc',
            icon: 'apartment',
            component: './Project/Workspace/DataIntegration/FlinkCDC',
          },
          {
            path: '/workspace/data-integration/flink-cdc/dag',
            component: './Project/Workspace/DataIntegration/FlinkCDC/Dag',
          }
        ]
      },
      {
        name: 'project.data-develop',
        path: '/workspace/data-develop',
        icon: 'deploymentUnit',
        routes: [
          {
            name: 'flink-jar',
            path: '/workspace/data-develop/flink/jar',
            icon: 'apartment',
            component: './Project/Workspace/DataDevelop/Flink/Jar',
          },
          {
            path: '/workspace/data-develop/flink/jar/history',
            component: './Project/Workspace/DataDevelop/Flink/Jar/History',
          },
          {
            name: 'flink-sql',
            path: '/workspace/data-develop/flink/sql',
            icon: 'apartment',
            component: './Project/Workspace/DataDevelop/Flink/SQL',
          },
          {
            path: '/workspace/data-develop/flink/sql/editor',
            component: './Project/Workspace/DataDevelop/Flink/SQL/CodeEditor',
          }
        ]
      },
      {
        name: 'project.dag-scheduler',
        path: '/workspace/dag-scheduler',
        icon: 'deploymentUnit',
      },
      {
        name: 'project.data-service',
        path: '/workspace/data-service',
        icon: 'solution',
        routes: [
          {
            name: 'config',
            path: '/workspace/data-service/config',
            component: './Project/Workspace/DataService/Config',
          },
          {
            path: '/workspace/data-service/config/steps',
            component: './Project/Workspace/DataService/Config/Steps',
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
    path: "/user/center",
    component: "./User",
    exact: true,
  },
  {
    path: "/403",
    component: "./Abnormal/403",
    layout: false,
    exact: true,
  },
  {
    path: "/404",
    component: "./Abnormal/404",
    layout: false,
    exact: true,
  },
  {
    path: "/500",
    component: "./Abnormal/500",
    layout: false,
    exact: true,
  },
  {
    path: "*",
    layout: false,
    component: "./Abnormal/404",
  },
];
