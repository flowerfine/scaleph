/**分页参数 */
export const DEFAULT_PAGE_PARAM = {
  pageSize: 10,
  pageIndex: 1,
  pageParams: [10, 20, 50, 100],
  sorter: [{ field: '', direction: '' }],
};

export const DICT_TYPE = {
  yesNo: 'yes_or_no',
  roleType: 'role_type',
  roleStatus: 'role_status',
  userStatus: 'user_status',
  deptStatus: 'dept_status',
  resourceType: 'resource_type',

  idCardType: 'id_card_type',
  gender: 'gender',
  nation: 'nation',

  datasourceType: 'datasource_type',
  redisMode: 'redis_mode',

  jobType: 'job_type',
  jobStatus: 'job_status',
  jobInstanceState: 'job_instance_state',
  runtimeState: 'runtime_state',
  clusterType: 'cluster_type',
  dataType: 'data_type',
  flinkRuntimeExecutionMode: 'flink_runtime_execution_mode',
  flinkDeployConfigType: 'flink_deploy_config_type',
  flinkResourceProvider: 'flink_resource_provider',
  flinkDeploymentMode: 'flink_deployment_mode',
  flinkVersion: 'flink_version',
  flinkStateBackend: 'flink_state_backend',
  flinkClusterStatus: 'flink_cluster_status',
  flinkSemantic: 'flink_semantic',
  flinkCheckpointRetain: 'flink_checkpoint_retain',
  flinkRestartStrategy: 'flink_restart_strategy',
  flinkHA: 'flink_high_availability',
  flinkJobType: 'flink_job_type',
  flinkJobStatus: 'flink_job_status',
  seatunnelVersion: 'seatunnel_version',
  seatunnelEngineType: 'seatunnel_engine_type',
  seatunnelPluginName: 'seatunnel_plugin_name',
  seatunnelRowKind: 'seatunnel_row_kind',
  seatunnelFakeMode: 'seatunnel_fake_mode',
  seatunnelCDCFormat: 'seatunnel_cdc_format',
  imagePullPolicy: 'image_pull_policy',

  deploymentKind: 'deployment_kind',


};

export const USER_AUTH = {
  token: 'u_token',
  userInfo: 'u_info',
  pCodes: 'u_pCode',
  roleSysAdmin: 'sys_super_admin',
  expireTime: 'u_expire_time',
};

export const RESOURCE_TYPE = {
  flinkRelease: 'flink_release',
  clusterCredential: 'cluster_credential',
  jar: 'jar',
};

export const WORKSPACE_CONF = {
  projectId: 'projectId',
}

export const PRIVILEGE_CODE = {
  studioShow: 'psdo0',
  studioDataBoardShow: 'psdb0',

  projectShow: 'pddp0',
  datadevProjectSelect: 'pddp4',
  datadevProjectAdd: 'pddp1',
  datadevProjectDelete: 'pddp3',
  datadevProjectEdit: 'pddp2',

  workspaceShow: 'pdev0',
  workspaceJobShow: 'pddj0',
  workspaceJobDetailShow: 'pddj0',
  workspaceJobArtifactShow: 'pddj0',
  workspaceJobArtifactJarShow: 'pddj0',
  workspaceJobSqlShow: 'pddj0',
  workspaceJobSeaTunnelShow: 'pddj0',
  workspaceClusterShow: 'pddj0',
  workspaceClusterConfigShow: 'pddj0',
  workspaceClusterConfigOptionsShow: 'pddj0',
  workspaceClusterInstanceShow: 'pddj0',

  resourceShow: 'pddj0',
  resourceJarShow: 'pddj0',
  resourceFlinkReleaseShow: 'pddj0',
  resourceSeaTunnelReleaseShow: 'pddj0',
  resourceKerberosShow: 'pddj0',
  resourceClusterCredentialShow: 'pddj0',

  dataSourceShow: 'pddj0',
  datadevDatasourceSelect: 'pdts4',
  datadevDatasourceAdd: 'pdts1',
  datadevDatasourceDelete: 'pdts3',
  datadevDatasourceEdit: 'pdts2',
  datadevDatasourceSecurity: 'pdts6',

  stdataShow: 'pstd0',
  stdataRefDataShow: 'pstr0',
  stdataRefDataMapShow: 'pstm0',
  stdataDataElementShow: 'pste0',
  stdataSystemShow: 'psts0',



  datadevResourceSelect: 'pdde4',
  datadevResourceAdd: 'pdde1',
  datadevResourceDelete: 'pdde3',
  datadevResourceEdit: 'pdde2',
  datadevResourceDownload: 'pdde7',
  datadevJobSelect: 'pddj4',
  datadevJobAdd: 'pddj1',
  datadevJobDelete: 'pddj3',
  datadevJobEdit: 'pddj2',
  datadevDirSelect: 'pddr4',
  datadevDirAdd: 'pddr1',
  datadevDirDelete: 'pddr3',
  datadevDirEdit: 'pddr2',
  datadevClusterSelect: 'pddc4',
  datadevClusterAdd: 'pddc1',
  datadevClusterDelete: 'pddc3',
  datadevClusterEdit: 'pddc2',
  opscenterBatchSelect: 'pobt4',
  opscenterRealtimeSelect: 'port4',
  stdataRefDataTypeSelect: 'pstt4',
  stdataRefDataTypeAdd: 'pstt1',
  stdataRefDataTypeDelete: 'pstt3',
  stdataRefDataTypeEdit: 'pstt2',
  stdataRefDataSelect: 'pstr4',
  stdataRefDataAdd: 'pstr1',
  stdataRefDataDelete: 'pstr3',
  stdataRefDataEdit: 'pstr2',
  stdataRefDataMapSelect: 'pstm4',
  stdataRefDataMapAdd: 'pstm1',
  stdataRefDataMapDelete: 'pstm3',
  stdataRefDataMapEdit: 'pstm2',
  stdataDataElementSelect: 'pste4',
  stdataDataElementAdd: 'pste1',
  stdataDataElementDelete: 'pste3',
  stdataDataElementEdit: 'pste2',
  stdataSystemSelect: 'psts4',
  stdataSystemAdd: 'psts1',
  stdataSystemDelete: 'psts3',
  stdataSystemEdit: 'psts2',


  adminShow: 'padm0',
  deptShow: 'padp0',
  deptAdd: 'padp1',
  deptEdit: 'padp2',
  deptDelete: 'padp3',
  deptSelect: 'padp4',
  deptGrant: 'padp5',

  roleShow: 'padr0',
  roleAdd: 'padr1',
  roleEdit: 'padr2',
  roleDelete: 'padr3',
  roleSelect: 'padr4',
  roleGrant: 'padr5',

  userShow: 'padu0',
  resourceWebShow: 'parw0',
  privilegeShow: 'pape0',
  workflowQuartzShow: 'pawq0',
  dictShow: 'padd0',
  settingShow: 'pads0',

  userSelect: 'pusr4',
  userAdd: 'pusr1',
  userDelete: 'pusr3',
  userEdit: 'pusr2',


  dictTypeSelect: 'pdct4',
  dictTypeAdd: 'pdct1',
  dictTypeDelete: 'pdct3',
  dictTypeEdit: 'pdct2',
  dictDataSelect: 'pdcd4',
  dictDataAdd: 'pdcd1',
  dictDataDelete: 'pdcd3',
  dictDataEdit: 'pdcd2',
};
