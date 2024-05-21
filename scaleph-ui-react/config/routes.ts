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
        name: 'project.engine',
        path: '/workspace/engine',
        icon: 'oneToOne',
        routes: [
          {
            name: 'lake',
            path: '/workspace/engine/lake',
            icon: 'rest',
            routes: [
              {
                name: 'iceberg',
                path: '/workspace/engine/lake/iceberg',
                icon: 'robot',
                component: './Project/Workspace/Engine/Lake/Iceberg',
              },
              {
                name: 'paimon',
                path: '/workspace/engine/lake/paimon',
                icon: 'totateLeft',
                component: './Project/Workspace/Engine/Lake/Paimon',
              }
            ]
          },
          {
            name: 'olap',
            path: '/workspace/engine/olap',
            icon: 'rocket',
            routes: [
              {
                name: 'doris',
                path: '/workspace/engine/olap/doris',
                icon: 'apartment',
                routes: [
                  {
                    name: 'template',
                    path: '/workspace/engine/olap/doris/template',
                    component: './Project/Workspace/Engine/OLAP/Doris/OperatorTemplate',
                  },
                  {
                    path: '/workspace/engine/olap/doris/template/steps',
                    component: './Project/Workspace/Engine/OLAP/Doris/OperatorTemplate/Steps',
                  },
                  {
                    path: '/workspace/engine/olap/doris/template/detail',
                    component: './Project/Workspace/Engine/OLAP/Doris/OperatorTemplate/Detail',
                  },
                  {
                    name: 'instance',
                    path: '/workspace/engine/olap/doris/instance',
                    component: './Project/Workspace/Engine/OLAP/Doris/OperatorInstance',
                  },
                  {
                    path: '/workspace/engine/olap/doris/instance/steps',
                    component: './Project/Workspace/Engine/OLAP/Doris/OperatorInstance/Steps',
                  },
                  {
                    path: '/workspace/engine/olap/doris/instance/detail',
                    component: './Project/Workspace/Engine/OLAP/Doris/OperatorInstance/Detail',
                  },
                ]
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
            icon: 'sisternode',
            routes: [
              {
                name: 'flink',
                path: '/workspace/engine/compute/flink',
                icon: 'apartment',
                routes: [
                  {
                    name: 'template',
                    path: '/workspace/engine/compute/flink/template',
                    component: './Project/Workspace/Engine/Compute/Flink/Template',
                  },
                  {
                    path: '/workspace/engine/compute/flink/template/steps/new',
                    component: './Project/Workspace/Engine/Compute/Flink/Template/Steps/New',
                  },
                  {
                    path: '/workspace/engine/compute/flink/template/steps/update',
                    component: './Project/Workspace/Engine/Compute/Flink/Template/Steps/Update',
                  },
                  {
                    name: 'session-cluster',
                    path: '/workspace/engine/compute/flink/session-cluster',
                    component: './Project/Workspace/Engine/Compute/Flink/SessionCluster',
                  },
                  {
                    path: '/workspace/engine/compute/flink/session-cluster/steps/new',
                    component: './Project/Workspace/Engine/Compute/Flink/SessionCluster/Steps/New',
                  },
                  {
                    path: '/workspace/engine/compute/flink/session-cluster/steps/update',
                    component: './Project/Workspace/Engine/Compute/Flink/SessionCluster/Steps/Update',
                  },
                  {
                    path: '/workspace/engine/compute/flink/session-cluster/detail',
                    component: './Project/Workspace/Engine/Compute/Flink/SessionCluster/Detail',
                  },
                  {
                    name: 'deployment',
                    path: '/workspace/engine/compute/flink/deployment',
                    component: './Project/Workspace/Engine/Compute/Flink/Deployment',
                  },
                  {
                    path: '/workspace/engine/compute/flink/deployment/steps/new',
                    component: './Project/Workspace/Engine/Compute/Flink/Deployment/Steps/New',
                  },
                  {
                    path: '/workspace/engine/compute/flink/deployment/steps/update',
                    component: './Project/Workspace/Engine/Compute/Flink/Deployment/Steps/Update',
                  },
                  {
                    path: '/workspace/engine/compute/flink/deployment/detail',
                    component: './Project/Workspace/Engine/Compute/Flink/Deployment/Detail',
                  }
                ]
              }
            ]
          },
        ]
      },
      {
        name: 'project.data-integration',
        path: '/workspace/data-integration',
        icon: 'thunderbolt',
        routes: [
          {
            name: 'seatunnel',
            path: '/workspace/data-integration/seatunnel',
            icon: 'safety',
            component: './Project/Workspace/DataIntegration/SeaTunnel',
          },
          {
            path: '/workspace/data-integration/seatunnel/dag',
            component: './Project/Workspace/DataIntegration/SeaTunnel/Dag',
          },
          {
            name: 'flink-cdc',
            path: '/workspace/data-integration/flink-cdc',
            icon: 'sun',
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
        icon: 'consoleSql',
        routes: [
          {
            name: 'flink-jar',
            path: '/workspace/data-develop/flink/jar',
            icon: 'send',
            component: './Project/Workspace/DataDevelop/Flink/Jar',
          },
          {
            path: '/workspace/data-develop/flink/jar/history',
            component: './Project/Workspace/DataDevelop/Flink/Jar/History',
          },
          {
            name: 'flink-sql',
            path: '/workspace/data-develop/flink/sql',
            icon: 'transaction',
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
        icon: 'schedule',
      },
      {
        name: 'project.data-service',
        path: '/workspace/data-service',
        icon: 'read',
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
      {
        name: 'project.operation',
        path: '/workspace/operation',
        icon: 'solution',
        routes: [
          {
            name: 'flink',
            path: '/workspace/operation/compute/flink/job',
            component: './Project/Workspace/Engine/Compute/Flink/Job',
          },
          {
            path: '/workspace/operation/compute/flink/job/detail',
            component: './Project/Workspace/Engine/Compute/Flink/Job/Detail',
          }
        ]
      },
    ]
  },
  {
    name: 'oam',
    path: '/oam',
    icon: 'wallet',
    routes: [
      {
        name: 'definition',
        path: '/oam/definitin',
        icon: 'signature',
        component: './OAM/Definition',
      },
    ],
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
    name: 'metadata',
    path: '/metadata',
    icon: 'compass',
    routes: [
      {
        name: 'data-source',
        path: '/metadata/data-source',
        icon: 'fundView',
        routes: [
          {
            name: 'info',
            path: '/metadata/data-source/info',
            icon: 'hdd',
            component: './Metadata/DataSource/Info',
          },
          {
            path: '/metadata/data-source/info/stepForms',
            component: './Metadata/DataSource/Info/StepForms',
          }
        ]
      },
      {
        name: 'gravitino',
        path: '/metadata/gravitino',
        icon: 'gateway',
        routes: [
          {
            name: 'catalog',
            path: '/metadata/gravitino/catelog',
            icon: 'mergeCells',
            component: './Metadata/Gravitino/Catalog',
          },
        ]
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
