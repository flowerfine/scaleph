import {PageResponse, ResponseBody, SelectOption} from '@/typings';
import {request} from '@umijs/max';
import {
  KubernetesOptionsVO,
  WsFlinkKubernetesTemplate,
  WsFlinkKubernetesTemplateAddParam,
  WsFlinkKubernetesTemplateParam,
  WsFlinkKubernetesTemplateUpdateParam
} from './typings';

export const WsFlinkKubernetesTemplateService = {
  url: '/api/flink/kubernetes/template',

  list: async (queryParam: WsFlinkKubernetesTemplateParam) => {
    return request<PageResponse<WsFlinkKubernetesTemplate>>(`${WsFlinkKubernetesTemplateService.url}`, {
      method: 'GET',
      params: queryParam,
    }).then((res) => {
      const result = {
        data: res.records,
        total: res.total,
        pageSize: res.size,
        current: res.current,
      };
      return result;
    });
  },

  selectOne: async (id: number) => {
    return request<ResponseBody<WsFlinkKubernetesTemplate>>(`${WsFlinkKubernetesTemplateService.url}/` + id, {
      method: 'GET',
    });
  },

  getFlinkVersionOptions: async () => {
    return request<ResponseBody<Array<SelectOption>>>(`${WsFlinkKubernetesTemplateService.url}/flinkVersionMappings`, {
      method: 'GET',
    });
  },

  getFlinkImageOptions: async (version: string) => {
    return request<ResponseBody<Array<SelectOption>>>(`${WsFlinkKubernetesTemplateService.url}/flinkImageMappings`, {
      method: 'GET',
      params: {flinkVersion: version},
    });
  },

  asYaml: async (row: WsFlinkKubernetesTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}/asYaml`, {
      method: 'POST',
      data: {...row, deploymentKind: row.deploymentKind?.value},
    });
  },

  asYamlWithDefault: async (row: WsFlinkKubernetesTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}/asYamlWithDefault`, {
      method: 'POST',
      data: {...row, deploymentKind: row.deploymentKind?.value},
    });
  },

  mergeDefault: async (row: WsFlinkKubernetesTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}/default`, {
      method: 'PATCH',
      data: row,
    });
  },

  add: async (param: WsFlinkKubernetesTemplateAddParam) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (param: WsFlinkKubernetesTemplateUpdateParam) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  updateTemplate: async (param: WsFlinkKubernetesTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}/template`, {
      method: 'POST',
      data: {...param, deploymentKind: param.deploymentKind?.value},
    });
  },

  delete: async (row: WsFlinkKubernetesTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsFlinkKubernetesTemplate[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsFlinkKubernetesTemplateService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  formatData: (data: WsFlinkKubernetesTemplate, value: Record<string, any>) => {
    const kubernetesOptionsVO: KubernetesOptionsVO = {
      flinkVersion: value.flinkVersion,
      serviceAccount: value.serviceAccount,
      image: value.image,
      imagePullPolicy: value.imagePullPolicy,
    }
    data.kubernetesOptions = kubernetesOptionsVO;

    const jobManager: Record<string, any> = {}
    jobManager["replicas"] = value["jobManager.replicas"]
    const jobManagerResource: Record<string, any> = {}
    jobManager["resource"] = jobManagerResource
    jobManagerResource["cpu"] = value["jobManager.resource.cpu"]
    jobManagerResource["memory"] = value["jobManager.resource.memory"]
    data.jobManager = jobManager

    const taskManager: Record<string, any> = {}
    taskManager["replicas"] = value["taskManager.replicas"]
    const taskManagerResource: Record<string, any> = {}
    taskManager["resource"] = taskManagerResource
    taskManagerResource["cpu"] = value["taskManager.resource.cpu"]
    taskManagerResource["memory"] = value["taskManager.resource.memory"]
    data.taskManager = taskManager

    data.additionalDependencies = value.additionalDependencies

    const flinkConfiguration: Record<string, any> = {}
    flinkConfiguration["execution.checkpointing.mode"] = value["execution.checkpointing.mode"]
    flinkConfiguration["execution.checkpointing.interval"] = value["execution.checkpointing.interval"]
    flinkConfiguration["execution.checkpointing.min-pause"] = value["execution.checkpointing.min-pause"]
    flinkConfiguration["execution.checkpointing.timeout"] = value["execution.checkpointing.timeout"]
    flinkConfiguration["execution.checkpointing.max-concurrent-checkpoints"] = value["execution.checkpointing.max-concurrent-checkpoints"]
    flinkConfiguration["execution.checkpointing.unaligned"] = value["execution.checkpointing.unaligned"]
    flinkConfiguration["execution.checkpointing.alignment-timeout"] = value["execution.checkpointing.alignment-timeout"]
    flinkConfiguration["execution.checkpointing.externalized-checkpoint-retention"] = value["execution.checkpointing.externalized-checkpoint-retention"]
    flinkConfiguration["state.checkpoints.num-retained"] = value["state.checkpoints.num-retained"]

    flinkConfiguration['restart-strategy'] = value['strategy'];
    flinkConfiguration['restart-strategy.fixed-delay.attempts'] =
      value['restart-strategy.fixed-delay.attempts'];
    flinkConfiguration['restart-strategy.fixed-delay.delay'] = value['restart-strategy.fixed-delay.delay'];
    flinkConfiguration['restart-strategy.failure-rate.delay'] = value['restart-strategy.failure-rate.delay'];
    flinkConfiguration['restart-strategy.failure-rate.failure-rate-interval'] =
      value['restart-strategy.failure-rate.failure-rate-interval'];
    flinkConfiguration['restart-strategy.failure-rate.max-failures-per-interval'] =
      value['restart-strategy.failure-rate.max-failures-per-interval'];
    flinkConfiguration['restart-strategy.exponential-delay.initial-backoff'] =
      value['restart-strategy.exponential-delay.initial-backoff'];
    flinkConfiguration['restart-strategy.exponential-delay.backoff-multiplier'] =
      value['restart-strategy.exponential-delay.backoff-multiplier'];
    flinkConfiguration['restart-strategy.exponential-delay.max-backoff'] =
      value['restart-strategy.exponential-delay.max-backoff'];
    flinkConfiguration['restart-strategy.exponential-delay.reset-backoff-threshold'] =
      value['restart-strategy.exponential-delay.reset-backoff-threshold'];
    flinkConfiguration['restart-strategy.exponential-delay.jitter-factor'] =
      value['restart-strategy.exponential-delay.jitter-factor'];

    flinkConfiguration['high-availability'] = value['ha'];
    flinkConfiguration['high-availability.storageDir'] = value['high-availability.storageDir'];
    flinkConfiguration['high-availability.cluster-id'] = value['high-availability.cluster-id'];
    flinkConfiguration['high-availability.zookeeper.path.root'] =
      value['high-availability.zookeeper.path.root'];
    flinkConfiguration['high-availability.zookeeper.quorum'] = value['high-availability.zookeeper.quorum'];

    flinkConfiguration['kubernetes.operator.periodic.savepoint.interval'] = value['kubernetes.operator.periodic.savepoint.interval'];
    flinkConfiguration['kubernetes.operator.savepoint.trigger.grace-period'] = value['kubernetes.operator.savepoint.trigger.grace-period'];
    flinkConfiguration['kubernetes.operator.savepoint.format.type'] = value['kubernetes.operator.savepoint.format.type'];
    flinkConfiguration['kubernetes.operator.savepoint.history.max.age'] = value['kubernetes.operator.savepoint.history.max.age'];
    flinkConfiguration['kubernetes.operator.savepoint.history.max.count'] = value['kubernetes.operator.savepoint.history.max.count'];

    flinkConfiguration['kubernetes.operator.cluster.health-check.enabled'] = value['kubernetes.operator.cluster.health-check.enabled'];
    flinkConfiguration['kubernetes.operator.cluster.health-check.restarts.window'] = value['kubernetes.operator.cluster.health-check.restarts.window'];
    flinkConfiguration['kubernetes.operator.cluster.health-check.restarts.threshold'] = value['kubernetes.operator.cluster.health-check.restarts.threshold'];

    value.options?.forEach(function (item: Record<string, any>) {
      flinkConfiguration[item.key] = item.value;
    });
    data.flinkConfiguration = flinkConfiguration
    return data;
  },

  parseData: (data: WsFlinkKubernetesTemplate) => {
    const flinkConfiguration: Record<string, any> = new Map<string, any>()
    Object.entries<[string, any][]>(data.flinkConfiguration ? {...data.flinkConfiguration} : {}).forEach(([key, value]) => {
      flinkConfiguration.set(key, value);
    });

    const value = {
      'flinkVersion': data.kubernetesOptions?.flinkVersion,
      'serviceAccount': data.kubernetesOptions?.serviceAccount,
      'image': data.kubernetesOptions?.image,
      'imagePullPolicy': data.kubernetesOptions?.imagePullPolicy,

      'jobManager.replicas': data.jobManager?.replicas,
      'jobManager.resource.cpu': data.jobManager?.resource?.cpu,
      'jobManager.resource.memory': data.jobManager?.resource?.memory,
      'taskManager.replicas': data.taskManager?.replicas,
      'taskManager.resource.cpu': data.taskManager?.resource?.cpu,
      'taskManager.resource.memory': data.taskManager?.resource?.memory,

      'additionalDependencies': data.additionalDependencies,

      'execution.checkpointing.mode': flinkConfiguration.get('execution.checkpointing.mode'),
      'execution.checkpointing.interval': flinkConfiguration.get('execution.checkpointing.interval'),
      'execution.checkpointing.min-pause': flinkConfiguration.get('execution.checkpointing.min-pause'),
      'execution.checkpointing.timeout': flinkConfiguration.get('execution.checkpointing.timeout'),
      'execution.checkpointing.max-concurrent-checkpoints': flinkConfiguration.get('execution.checkpointing.max-concurrent-checkpoints'),
      'execution.checkpointing.unaligned': flinkConfiguration.get('execution.checkpointing.unaligned'),
      'execution.checkpointing.alignment-timeout': flinkConfiguration.get('execution.checkpointing.alignment-timeout'),
      'execution.checkpointing.externalized-checkpoint-retention': flinkConfiguration.get('execution.checkpointing.externalized-checkpoint-retention'),
      'state.checkpoints.num-retained': flinkConfiguration.get('state.checkpoints.num-retained'),

      strategy: flinkConfiguration.get('restart-strategy'),
      'restart-strategy.fixed-delay.attempts': flinkConfiguration.get('restart-strategy.fixed-delay.attempts'),
      'restart-strategy.fixed-delay.delay': flinkConfiguration.get('restart-strategy.fixed-delay.delay'),
      'restart-strategy.failure-rate.delay': flinkConfiguration.get('restart-strategy.failure-rate.delay'),
      'restart-strategy.failure-rate.failure-rate-interval': flinkConfiguration.get('restart-strategy.failure-rate.failure-rate-interval'),
      'restart-strategy.failure-rate.max-failures-per-interval': flinkConfiguration.get('restart-strategy.failure-rate.max-failures-per-interval'),
      'restart-strategy.exponential-delay.initial-backoff': flinkConfiguration.get('restart-strategy.exponential-delay.initial-backoff'),
      'restart-strategy.exponential-delay.backoff-multiplier': flinkConfiguration.get('restart-strategy.exponential-delay.backoff-multiplier'),
      'restart-strategy.exponential-delay.max-backoff': flinkConfiguration.get('restart-strategy.exponential-delay.max-backoff'),
      'restart-strategy.exponential-delay.reset-backoff-threshold': flinkConfiguration.get('restart-strategy.exponential-delay.reset-backoff-threshold'),
      'restart-strategy.exponential-delay.jitter-factor': flinkConfiguration.get('restart-strategy.exponential-delay.jitter-factor'),

      ha: flinkConfiguration.get('high-availability'),
      'high-availability.storageDir': flinkConfiguration.get('high-availability.storageDir'),
      'high-availability.cluster-id': flinkConfiguration.get('high-availability.cluster-id'),
      'high-availability.zookeeper.path.root': flinkConfiguration.get('high-availability.zookeeper.path.root'),
      'high-availability.zookeeper.quorum': flinkConfiguration.get('high-availability.zookeeper.quorum'),

      'kubernetes.operator.periodic.savepoint.interval': flinkConfiguration.get('kubernetes.operator.periodic.savepoint.interval'),
      'kubernetes.operator.savepoint.trigger.grace-period': flinkConfiguration.get('kubernetes.operator.savepoint.trigger.grace-period'),
      'kubernetes.operator.savepoint.format.type': flinkConfiguration.get('kubernetes.operator.savepoint.format.type'),
      'kubernetes.operator.savepoint.history.max.age': flinkConfiguration.get('kubernetes.operator.savepoint.history.max.age'),
      'kubernetes.operator.savepoint.history.max.count': flinkConfiguration.get('kubernetes.operator.savepoint.history.max.count'),

      'kubernetes.operator.cluster.health-check.enabled': flinkConfiguration.get('kubernetes.operator.cluster.health-check.enabled'),
      'kubernetes.operator.cluster.health-check.restarts.window': flinkConfiguration.get('kubernetes.operator.cluster.health-check.restarts.window'),
      'kubernetes.operator.cluster.health-check.restarts.threshold': flinkConfiguration.get('kubernetes.operator.cluster.health-check.restarts.threshold'),
    };

    flinkConfiguration.delete('execution.checkpointing.mode');
    flinkConfiguration.delete('execution.checkpointing.interval');
    flinkConfiguration.delete('execution.checkpointing.min-pause');
    flinkConfiguration.delete('execution.checkpointing.timeout');
    flinkConfiguration.delete('execution.checkpointing.max-concurrent-checkpoints');
    flinkConfiguration.delete('execution.checkpointing.unaligned');
    flinkConfiguration.delete('execution.checkpointing.alignment-timeout');
    flinkConfiguration.delete('execution.checkpointing.externalized-checkpoint-retention');
    flinkConfiguration.delete('state.checkpoints.num-retained');

    flinkConfiguration.delete('restart-strategy');
    flinkConfiguration.delete('restart-strategy.fixed-delay.attempts');
    flinkConfiguration.delete('restart-strategy.fixed-delay.delay');
    flinkConfiguration.delete('restart-strategy.failure-rate.delay');
    flinkConfiguration.delete('restart-strategy.failure-rate.failure-rate-interval');
    flinkConfiguration.delete('restart-strategy.failure-rate.max-failures-per-interval');
    flinkConfiguration.delete('restart-strategy.exponential-delay.initial-backoff');
    flinkConfiguration.delete('restart-strategy.exponential-delay.backoff-multiplier');
    flinkConfiguration.delete('restart-strategy.exponential-delay.max-backoff');
    flinkConfiguration.delete('restart-strategy.exponential-delay.reset-backoff-threshold');
    flinkConfiguration.delete('restart-strategy.exponential-delay.jitter-factor');

    flinkConfiguration.delete('high-availability');
    flinkConfiguration.delete('high-availability.storageDir');
    flinkConfiguration.delete('high-availability.cluster-id');
    flinkConfiguration.delete('high-availability.zookeeper.path.root');
    flinkConfiguration.delete('high-availability.zookeeper.quorum');

    flinkConfiguration.delete('kubernetes.operator.periodic.savepoint.interval');
    flinkConfiguration.delete('kubernetes.operator.savepoint.trigger.grace-period');
    flinkConfiguration.delete('kubernetes.operator.savepoint.format.type');
    flinkConfiguration.delete('kubernetes.operator.savepoint.history.max.age');
    flinkConfiguration.delete('kubernetes.operator.savepoint.history.max.count');

    flinkConfiguration.delete('kubernetes.operator.cluster.health-check.enabled');
    flinkConfiguration.delete('kubernetes.operator.cluster.health-check.restarts.window');
    flinkConfiguration.delete('kubernetes.operator.cluster.health-check.restarts.threshold');

    const options: Array<any> = [];
    flinkConfiguration.forEach((value: any, key: string) => {
      options.push({key: key, value: value});
    });
    value['options'] = options;

    return value;
  },
};
