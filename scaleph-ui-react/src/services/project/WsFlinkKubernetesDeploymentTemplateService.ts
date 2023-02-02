import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {WsFlinkKubernetesDeploymentTemplate, WsFlinkKubernetesDeploymentTemplateParam} from './typings';

export const WsFlinkKubernetesDeploymentTemplateService = {
  url: '/api/flink/kubernetes/template',

  list: async (queryParam: WsFlinkKubernetesDeploymentTemplateParam) => {
    return request<PageResponse<WsFlinkKubernetesDeploymentTemplate>>(`${WsFlinkKubernetesDeploymentTemplateService.url}`, {
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
    return request<ResponseBody<WsFlinkKubernetesDeploymentTemplate>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/` + id, {
      method: 'GET',
    });
  },

  asTemplate: async (row: any) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/asTemplate`, {
      method: 'POST',
      data: row,
    });
  },

  asTemplateWithDefault: async (row: any) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/asTemplateWithDefault`, {
      method: 'POST',
      data: row,
    });
  },

  mergeDefault: async (row: any) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/default`, {
      method: 'PATCH',
      data: row,
    });
  },

  add: async (row: WsFlinkKubernetesDeploymentTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsFlinkKubernetesDeploymentTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  delete: async (row: WsFlinkKubernetesDeploymentTemplate) => {
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsFlinkKubernetesDeploymentTemplate[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsFlinkKubernetesDeploymentTemplateService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  formatData: (data: WsFlinkKubernetesDeploymentTemplate, value: Record<string, any>) => {
    const spec: Record<string, any> = {}
    spec["flinkVersion"] = value["spec.flinkVersion"]
    spec["serviceAccount"] = value["spec.serviceAccount"]
    spec["image"] = value["spec.image"]
    spec["imagePullPolicy"] = value["spec.imagePullPolicy"]

    const jobManager: Record<string, any> = spec.jobManager ? spec.jobManager : {}
    spec["jobManager"] = jobManager
    jobManager["replicas"] = value["spec.jobManager.replicas"]
    const jobManagerResource: Record<string, any> = jobManager.resource ? jobManager.resource : {}
    jobManager["resource"] = jobManagerResource
    jobManagerResource["cpu"] = value["spec.jobManager.resource.cpu"]
    jobManagerResource["memory"] = value["spec.jobManager.resource.memory"]

    const taskManager: Record<string, any> = spec.taskManager ? spec.taskManager : {}
    spec["taskManager"] = taskManager
    taskManager["replicas"] = value["spec.taskManager.replicas"]
    const taskManagerResource: Record<string, any> = taskManager.resource ? taskManager.resource : {}
    taskManager["resource"] = taskManagerResource
    taskManagerResource["cpu"] = value["spec.taskManager.resource.cpu"]
    taskManagerResource["memory"] = value["spec.taskManager.resource.memory"]

    const flinkConfiguration: Record<string, any> = spec.flinkConfiguration ? spec.flinkConfiguration : {}
    spec["flinkConfiguration"] = flinkConfiguration
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
    data.spec = spec
    return data;
  },

  parseData: (data: WsFlinkKubernetesDeploymentTemplate) => {
    const spec: Record<string, any> = data.spec ? {...data.spec} : {}
    const flinkConfiguration: Record<string, any> = new Map<string, any>()
    Object.entries<[string, any][]>(spec.flinkConfiguration ? {...spec.flinkConfiguration} : {}).forEach(([key, value]) => {
      flinkConfiguration.set(key, value);
    });
    const value = {
      'spec.flinkVersion': spec['flinkVersion'],
      'spec.serviceAccount': spec['serviceAccount'],
      'spec.image': spec['image'],
      'spec.imagePullPolicy': spec['imagePullPolicy'],

      'spec.jobManager.replicas': spec.jobManager?.replicas,
      'spec.jobManager.resource.cpu': spec.jobManager?.resource?.cpu,
      'spec.jobManager.resource.memory': spec.jobManager?.resource?.memory,

      'spec.taskManager.replicas': spec.taskManager?.replicas,
      'spec.taskManager.resource.cpu': spec.taskManager?.resource?.cpu,
      'spec.taskManager.resource.memory': spec.taskManager?.resource?.memory,

      'execution.checkpointing.mode': flinkConfiguration['execution.checkpointing.mode'],
      'execution.checkpointing.interval': flinkConfiguration['execution.checkpointing.interval'],
      'execution.checkpointing.min-pause': flinkConfiguration['execution.checkpointing.min-pause'],
      'execution.checkpointing.timeout': flinkConfiguration['execution.checkpointing.timeout'],
      'execution.checkpointing.max-concurrent-checkpoints': flinkConfiguration['execution.checkpointing.max-concurrent-checkpoints'],
      'execution.checkpointing.unaligned': flinkConfiguration['execution.checkpointing.unaligned'],
      'execution.checkpointing.alignment-timeout': flinkConfiguration['execution.checkpointing.alignment-timeout'],
      'execution.checkpointing.externalized-checkpoint-retention': flinkConfiguration['execution.checkpointing.externalized-checkpoint-retention'],
      'state.checkpoints.num-retained': flinkConfiguration['state.checkpoints.num-retained'],

      strategy: flinkConfiguration['restart-strategy'],
      'restart-strategy.fixed-delay.attempts': flinkConfiguration['restart-strategy.fixed-delay.attempts'],
      'restart-strategy.fixed-delay.delay': flinkConfiguration['restart-strategy.fixed-delay.delay'],
      'restart-strategy.failure-rate.delay': flinkConfiguration['restart-strategy.failure-rate.delay'],
      'restart-strategy.failure-rate.failure-rate-interval': flinkConfiguration['restart-strategy.failure-rate.failure-rate-interval'],
      'restart-strategy.failure-rate.max-failures-per-interval': flinkConfiguration['restart-strategy.failure-rate.max-failures-per-interval'],
      'restart-strategy.exponential-delay.initial-backoff': flinkConfiguration['restart-strategy.exponential-delay.initial-backoff'],
      'restart-strategy.exponential-delay.backoff-multiplier': flinkConfiguration['restart-strategy.exponential-delay.backoff-multiplier'],
      'restart-strategy.exponential-delay.max-backoff': flinkConfiguration['restart-strategy.exponential-delay.max-backoff'],
      'restart-strategy.exponential-delay.reset-backoff-threshold': flinkConfiguration['restart-strategy.exponential-delay.reset-backoff-threshold'],
      'restart-strategy.exponential-delay.jitter-factor': flinkConfiguration['restart-strategy.exponential-delay.jitter-factor'],

      ha: flinkConfiguration['high-availability'],
      'high-availability.storageDir': flinkConfiguration['high-availability.storageDir'],
      'high-availability.cluster-id': flinkConfiguration['high-availability.cluster-id'],
      'high-availability.zookeeper.path.root': flinkConfiguration['high-availability.zookeeper.path.root'],
      'high-availability.zookeeper.quorum': flinkConfiguration['high-availability.zookeeper.quorum'],

      'kubernetes.operator.periodic.savepoint.interval': flinkConfiguration['kubernetes.operator.periodic.savepoint.interval'],
      'kubernetes.operator.savepoint.trigger.grace-period': flinkConfiguration['kubernetes.operator.savepoint.trigger.grace-period'],
      'kubernetes.operator.savepoint.format.type': flinkConfiguration['kubernetes.operator.savepoint.format.type'],
      'kubernetes.operator.savepoint.history.max.age': flinkConfiguration['kubernetes.operator.savepoint.history.max.age'],
      'kubernetes.operator.savepoint.history.max.count': flinkConfiguration['kubernetes.operator.savepoint.history.max.count'],

      'kubernetes.operator.cluster.health-check.enabled': flinkConfiguration['kubernetes.operator.cluster.health-check.enabled'],
      'kubernetes.operator.cluster.health-check.restarts.window': flinkConfiguration['kubernetes.operator.cluster.health-check.restarts.window'],
      'kubernetes.operator.cluster.health-check.restarts.threshold': flinkConfiguration['kubernetes.operator.cluster.health-check.restarts.threshold'],
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
      options.push({ key: key, value: value });
    });
    value['options'] = options;

    return value;
  },
};
