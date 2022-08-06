import { PRIVILEGE_CODE, USER_AUTH } from '../@core/data/app.data';
interface Menu {
  title: string;
  link: string;
  menuIcon?: string;
  pCode?: string;
  children?: Menu[];
}

function hasMenu(code: string, pCodes: string[]): boolean {
  if (pCodes != null && pCodes != undefined) {
    return pCodes.includes(USER_AUTH.roleSysAdmin) || pCodes.includes(code);
  } else {
    return false;
  }
}

export default function (values) {
  let menus: Menu[] = [];
  let pCodes: string[] = JSON.parse(localStorage.getItem(USER_AUTH.pCodes));
  let menuList: Menu[] = [
    {
      title: values['studio']['title'],
      link: '/scaleph/studio',
      menuIcon: 'icon icon-build-with-tool',
      pCode: PRIVILEGE_CODE.studioShow,
      children: [
        {
          title: values['studio']['databoard'],
          link: '/scaleph/studio/databoard',
          pCode: PRIVILEGE_CODE.studioDataBoardShow,
        },
      ],
    },
    {
      title: values['flink']['title'],
      link: '/scaleph/flink',
      menuIcon: 'icon icon-system',
      pCode: PRIVILEGE_CODE.flinkShow,
      children: [
        {
          title: values['flink']['deploy-config'],
          link: '/scaleph/flink/deploy-config',
          pCode: PRIVILEGE_CODE.flinkReleaseShow,
        },
        {
          title: values['flink']['cluster-config'],
          link: '/scaleph/flink/cluster-config',
          pCode: PRIVILEGE_CODE.flinkReleaseShow,
        },
        {
          title: values['flink']['cluster-instance'],
          link: '/scaleph/flink/cluster-instance',
          pCode: PRIVILEGE_CODE.flinkReleaseShow,
        },
        {
          title: values['flink']['cluster-config-options'],
          link: '/scaleph/flink/cluster-config-options',
          pCode: PRIVILEGE_CODE.flinkReleaseShow,
        }
      ],
    },
    {
      title: values['job']['title'],
      link: '/scaleph/job',
      menuIcon: 'icon icon-op-task',
      pCode: PRIVILEGE_CODE.flinkShow,
      children: [
        {
          title: values['job']['artifact'],
          link: '/scaleph/job/artifact',
          pCode: PRIVILEGE_CODE.flinkReleaseShow,
        },
        {
          title: values['job']['job-config'],
          link: '/scaleph/job/job-config',
          pCode: PRIVILEGE_CODE.flinkReleaseShow,
        },
        {
          title: values['job']['job-instance'],
          link: '/scaleph/job/job-instance',
          pCode: PRIVILEGE_CODE.flinkReleaseShow,
        }
      ],
    },
    {
      title: values['datadev']['title'],
      link: '/scaleph/datadev',
      menuIcon: 'icon icon-classroom-post-answers-large',
      pCode: PRIVILEGE_CODE.datadevShow,
      children: [
        {
          title: values['datadev']['datasource'],
          link: '/scaleph/datadev/datasource',
          pCode: PRIVILEGE_CODE.datadevDatasourceShow,
        },
        {
          title: values['datadev']['project'],
          link: '/scaleph/datadev/project',
          pCode: PRIVILEGE_CODE.datadevProjectShow,
        },
        {
          title: values['datadev']['resource'],
          link: '/scaleph/datadev/resource',
          pCode: PRIVILEGE_CODE.datadevResourceShow,
        },
        {
          title: values['datadev']['cluster'],
          link: '/scaleph/datadev/cluster',
          pCode: PRIVILEGE_CODE.datadevClusterShow,
        },
      ],
    },
    {
      title: values['opscenter']['title'],
      link: '/scaleph/opscenter',
      menuIcon: 'icon icon-log',
      pCode: PRIVILEGE_CODE.opscenterShow,
      children: [
        {
          title: values['opscenter']['realtime'],
          link: '/scaleph/opscenter/realtime',
          pCode: PRIVILEGE_CODE.opscenterRealtimeShow,
        },
        {
          title: values['opscenter']['batch'],
          link: '/scaleph/opscenter/batch',
          pCode: PRIVILEGE_CODE.opscenterBatchShow,
        },
      ],
    },
    {
      title: values['resource']['title'],
      link: '/scaleph/resource',
      menuIcon: 'icon icon-release-set',
      pCode: PRIVILEGE_CODE.opscenterShow,
      children: [
        {
          title: values['resource']['flink'],
          link: '/scaleph/resource/flink',
          pCode: PRIVILEGE_CODE.opscenterRealtimeShow,
        },
        {
          title: values['resource']['seatunnel'],
          link: '/scaleph/resource/seatunnel',
          pCode: PRIVILEGE_CODE.opscenterRealtimeShow,
        },
        {
          title: values['resource']['cluster'],
          link: '/scaleph/resource/cluster',
          pCode: PRIVILEGE_CODE.opscenterBatchShow,
        },
      ],
    },
    {
      title: values['stdata']['title'],
      link: '/scaleph/stdata',
      menuIcon: 'icon icon-function-guide',
      pCode: PRIVILEGE_CODE.stdataShow,
      children: [
        { title: values['stdata']['dataElement'], link: '/scaleph/stdata/dataElement', pCode: PRIVILEGE_CODE.stdataDataElementShow },
        { title: values['stdata']['refdata'], link: '/scaleph/stdata/refdata', pCode: PRIVILEGE_CODE.stdataRefDataShow },
        {
          title: values['stdata']['refdataMap'],
          link: '/scaleph/stdata/refdataMap',
          pCode: PRIVILEGE_CODE.stdataRefDataMapShow,
        },
        {
          title: values['stdata']['system'],
          link: '/scaleph/stdata/system',
          pCode: PRIVILEGE_CODE.stdataSystemShow,
        },
      ],
    },
    {
      title: values['admin']['title'],
      link: '/scaleph/admin',
      menuIcon: 'icon icon-setting',
      pCode: PRIVILEGE_CODE.adminShow,
      children: [
        {
          title: values['admin']['user'],
          link: '/scaleph/admin/user',
          pCode: PRIVILEGE_CODE.userShow,
        },
        {
          title: values['admin']['privilege'],
          link: '/scaleph/admin/privilege',
          pCode: PRIVILEGE_CODE.privilegeShow,
        },
        {
          title: values['admin']['dict'],
          link: '/scaleph/admin/dict',
          pCode: PRIVILEGE_CODE.dictShow,
        },
        {
          title: values['admin']['setting'],
          link: '/scaleph/admin/setting',
          pCode: PRIVILEGE_CODE.settingShow,
        },
      ],
    },
  ];
  menus = menuList.filter((pm) => {
    return hasMenu(pm.pCode, pCodes);
  });
  for (let x = 0; x < menus.length; x++) {
    const ms = menus[x].children.filter((m) => {
      return hasMenu(m.pCode, pCodes);
    });
    menus[x].children = ms;
  }

  return menus;
}
