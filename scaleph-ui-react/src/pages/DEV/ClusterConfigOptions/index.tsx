import {
  FooterToolbar,
  ProCard,
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from "@ant-design/pro-components";
import {Col, Form, message, Row} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE, RESOURCE_TYPE} from "@/constant";
import {ResourceListParam} from "@/services/resource/typings";
import {list} from "@/services/resource/resource.service";
import {FlinkClusterConfig} from "@/services/dev/typings";
import {add, update} from "@/services/dev/flinkClusterConfig.service";
import {history, useIntl, useLocation} from 'umi';
import {Dict} from '@/app.d';

const DevBatchJob: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const [form] = Form.useForm();

  const params = urlParams.state as FlinkClusterConfig;

  const configOptions = new Map(Object.entries(params?.configOptions ? params?.configOptions : {}))

  const data = {
    name: params?.name,
    deployMode: params?.deployMode?.value,
    flinkVersion: params?.flinkVersion?.value,
    flinkRelease: params?.flinkRelease?.id,
    resourceProvider: params?.resourceProvider?.value,
    clusterCredential: params?.clusterCredential?.id,
    remark: params?.remark,
    'state.backend': configOptions.get('state.backend'),
    'state.savepoints.dir': configOptions.get('state.savepoints.dir'),
    'state.checkpoints.dir': configOptions.get('state.checkpoints.dir'),
    'execution.checkpointing.mode': configOptions.get('execution.checkpointing.mode'),
    'execution.checkpointing.unaligned': configOptions.get('execution.checkpointing.unaligned'),
    'execution checkpointing interval': configOptions.get('execution checkpointing interval'),
    'execution.checkpointing.externalized-checkpoint-retention': configOptions.get('execution.checkpointing.externalized-checkpoint-retention'),
    'strategy': configOptions.get('restart-strategy'),
    'restart-strategy.fixed-delay.attempts': configOptions.get('restart-strategy.fixed-delay.attempts'),
    'restart-strategy.fixed-delay.delay': configOptions.get('restart-strategy.fixed-delay.delay'),
    'restart-strategy.failure-rate.delay': configOptions.get('restart-strategy.failure-rate.delay'),
    'restart-strategy.failure-rate.failure-rate-interval': configOptions.get('restart-strategy.failure-rate.failure-rate-interval'),
    'restart-strategy.failure-rate.max-failures-per-interval': configOptions.get('restart-strategy.failure-rate.max-failures-per-interval'),
    'restart-strategy.exponential-delay.initial-backoff': configOptions.get('restart-strategy.exponential-delay.initial-backoff'),
    'restart-strategy.exponential-delay.backoff-multiplier': configOptions.get('restart-strategy.exponential-delay.backoff-multiplier'),
    'restart-strategy.exponential-delay.max-backoff': configOptions.get('restart-strategy.exponential-delay.max-backoff'),
    'restart-strategy.exponential-delay.reset-backoff-threshold': configOptions.get('restart-strategy.exponential-delay.reset-backoff-threshold'),
    'restart-strategy.exponential-delay.jitter-factor': configOptions.get('restart-strategy.exponential-delay.jitter-factor'),
    'ha': configOptions.get('high-availability'),
    'high-availability.storageDir': configOptions.get('high-availability.storageDir'),
    'high-availability.cluster-id': configOptions.get('high-availability.cluster-id'),
    'high-availability.zookeeper.path.root': configOptions.get('high-availability.zookeeper.path.root'),
    'high-availability.zookeeper.quorum': configOptions.get('high-availability.zookeeper.quorum'),
    'jobmanager.memory.process.size': configOptions.get('jobmanager.memory.process.size'),
    'jobmanager.memory.flink.size': configOptions.get('jobmanager.memory.flink.size'),
    'taskmanager.memory.process.size': configOptions.get('taskmanager.memory.process.size'),
    'taskmanager.memory.flink.size': configOptions.get('taskmanager.memory.flink.size'),
  }

  configOptions.delete('state.backend')
  configOptions.delete('state.savepoints.dir')
  configOptions.delete('state.checkpoints.dir')
  configOptions.delete('execution.checkpointing.mode')
  configOptions.delete('execution.checkpointing.unaligned')
  configOptions.delete('execution checkpointing interval')
  configOptions.delete('execution.checkpointing.externalized-checkpoint-retention')
  configOptions.delete('restart-strategy')
  configOptions.delete('restart-strategy.fixed-delay.attempts')
  configOptions.delete('restart-strategy.fixed-delay.delay')
  configOptions.delete('restart-strategy.failure-rate.delay')
  configOptions.delete('restart-strategy.failure-rate.failure-rate-interval')
  configOptions.delete('restart-strategy.failure-rate.max-failures-per-interval')
  configOptions.delete('restart-strategy.exponential-delay.initial-backoff')
  configOptions.delete('restart-strategy.exponential-delay.backoff-multiplier')
  configOptions.delete('restart-strategy.exponential-delay.max-backoff')
  configOptions.delete('restart-strategy.exponential-delay.reset-backoff-threshold')
  configOptions.delete('restart-strategy.exponential-delay.jitter-factor')
  configOptions.delete('high-availability')
  configOptions.delete('high-availability.storageDir')
  configOptions.delete('high-availability.cluster-id')
  configOptions.delete('high-availability.zookeeper.path.root')
  configOptions.delete('high-availability.zookeeper.quorum')
  configOptions.delete('jobmanager.memory.process.size')
  configOptions.delete('jobmanager.memory.flink.size')
  configOptions.delete('taskmanager.memory.process.size')
  configOptions.delete('taskmanager.memory.flink.size')

  const options: Array<any> = []
  configOptions.forEach((value, key) => {
    options.push({key: key, value: value})
  })
  data['options'] = options

  return (<div>
    <ProForm
      form={form}
      layout={"horizontal"}
      initialValues={data}
      grid={true}
      rowProps={{gutter: [16, 8]}}
      submitter={{
        render: (props, doms) => {
          return (
            <Row>
              <Col>
                <FooterToolbar>{doms}</FooterToolbar>
              </Col>
            </Row>
          )
        },
        searchConfig: {
          resetText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
          submitText: intl.formatMessage({id: 'app.common.operate.submit.label'})
        },
        onReset: () => {
          history.back();
        },
      }}
      onFinish={(value) => {
        const options = new Map<string, any>();
        options['state.backend'] = value['state.backend']
        options['state.savepoints.dir'] = value['state.savepoints.dir']
        options['state.checkpoints.dir'] = value['state.checkpoints.dir']
        options['execution.checkpointing.mode'] = value['execution.checkpointing.mode']
        options['execution.checkpointing.unaligned'] = value['execution.checkpointing.unaligned']
        options['execution checkpointing interval'] = value['execution checkpointing interval']
        options['execution.checkpointing.externalized-checkpoint-retention'] = value['execution.checkpointing.externalized-checkpoint-retention']
        options['restart-strategy'] = value['strategy']
        options['restart-strategy.fixed-delay.attempts'] = value['restart-strategy.fixed-delay.attempts']
        options['restart-strategy.fixed-delay.delay'] = value['restart-strategy.fixed-delay.delay']
        options['restart-strategy.failure-rate.delay'] = value['restart-strategy.failure-rate.delay']
        options['restart-strategy.failure-rate.failure-rate-interval'] = value['restart-strategy.failure-rate.failure-rate-interval']
        options['restart-strategy.failure-rate.max-failures-per-interval'] = value['restart-strategy.failure-rate.max-failures-per-interval']
        options['restart-strategy.exponential-delay.initial-backoff'] = value['restart-strategy.exponential-delay.initial-backoff']
        options['restart-strategy.exponential-delay.backoff-multiplier'] = value['restart-strategy.exponential-delay.backoff-multiplier']
        options['restart-strategy.exponential-delay.max-backoff'] = value['restart-strategy.exponential-delay.max-backoff']
        options['restart-strategy.exponential-delay.reset-backoff-threshold'] = value['restart-strategy.exponential-delay.reset-backoff-threshold']
        options['restart-strategy.exponential-delay.jitter-factor'] = value['restart-strategy.exponential-delay.jitter-factor']
        options['high-availability'] = value['ha']
        options['high-availability.storageDir'] = value['high-availability.storageDir']
        options['high-availability.cluster-id'] = value['high-availability.cluster-id']
        options['high-availability.zookeeper.path.root'] = value['high-availability.zookeeper.path.root']
        options['high-availability.zookeeper.quorum'] = value['high-availability.zookeeper.quorum']
        options['jobmanager.memory.process.size'] = value['jobmanager.memory.process.size']
        options['jobmanager.memory.flink.size'] = value['jobmanager.memory.flink.size']
        options['taskmanager.memory.process.size'] = value['taskmanager.memory.process.size']
        options['taskmanager.memory.flink.size'] = value['taskmanager.memory.flink.size']

        value.options?.forEach(function (item: Record<string, any>) {
          options[item.key] = item.value
        })

        const param: FlinkClusterConfig = {
          id: params?.id,
          name: value['name'],
          flinkVersion: {value: value['flinkVersion']},
          resourceProvider: {value: value['resourceProvider']},
          deployMode: {value: value['deployMode']},
          flinkRelease: {id: value['flinkRelease']},
          clusterCredential: {id: value['clusterCredential']},
          remark: value['remark'],
          configOptions: options
        }
        return params?.id ?
          update(param).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
            } else {
              message.error(d.errorMessage);
            }
          })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.new.failure'}));
            })
            .finally(() => {
              history.back();
            })
          : add(param).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
            } else {
              message.error(d.errorMessage);
            }
          })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.new.failure'}));
            })
            .finally(() => {
              history.back();
            });
      }}
    >
      <ProCard
        title={'Basic'}
        headerBordered
        collapsible={true}
      >
        <ProForm.Group>
          <ProFormText
            name="name"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.name'})}
            colProps={{span: 10, offset: 1}}
            rules={[
              {required: true},
              {max: 30},
              {
                pattern: /^[\w\s_]+$/,
                message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
              },
            ]}
          />
          <ProFormSelect
            name="deployMode"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            dependencies={['resourceProvider']}
            request={(params) => {
              if (params.resourceProvider == '0') {
                const dict: Dict = {label: 'Session', value: '2'}
                return Promise.resolve([dict])
              }
              return listDictDataByType(DICT_TYPE.flinkDeploymentMode)
            }}
          />
          <ProFormSelect
            name="flinkVersion"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkVersion'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkVersion)}
          />
          <ProFormSelect
            name="flinkRelease"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            dependencies={['flinkVersion']}
            request={(params) => {
              const resourceParam: ResourceListParam = {
                resourceType: RESOURCE_TYPE.flinkRelease,
                label: params.flinkVersion
              }
              return list(resourceParam).then((response) => {
                return response.records.map((item) => {
                  return {label: item.fileName, value: item.id}
                })
              })
            }}
          />
          <ProFormSelect
            name="resourceProvider"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkResourceProvider)}
          />
          <ProFormSelect
            name="clusterCredential"
            label={intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'})}
            colProps={{span: 10, offset: 1}}
            rules={[{required: true}]}
            showSearch={true}
            dependencies={['resourceProvider']}
            request={(params) => {
              const resourceParam: ResourceListParam = {
                resourceType: RESOURCE_TYPE.clusterCredential,
                label: params.resourceProvider
              }
              return list(resourceParam).then((response) => {
                return response.records.map((item) => {
                  return {label: item.name, value: item.id}
                })
              })
            }}
          />
          <ProFormText
            name="remark"
            label={intl.formatMessage({id: 'pages.dev.remark'})}
            colProps={{span: 10, offset: 1}}
            rules={[{max: 200}]}
          />
        </ProForm.Group>
      </ProCard>
      <ProCard
        title={'State & Checkpoints & Savepoints'}
        headerBordered
        collapsible={true}
      >
        <ProForm.Group>
          <ProFormSelect
            name="state.backend"
            label={"state.backend"}
            colProps={{span: 10, offset: 1}}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkStateBackend)}
          />
          <ProFormText
            name="state.savepoints.dir"
            label={'state.savepoints.dir'}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name="state.checkpoints.dir"
            label={"state.checkpoints.dir"}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormSelect
            name="execution.checkpointing.mode"
            label={"execution.checkpointing.mode"}
            colProps={{span: 10, offset: 1}}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkSemantic)}
          />
          <ProFormSwitch
            name="execution.checkpointing.unaligned"
            label={"execution.checkpointing.unaligned"}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name="execution checkpointing interval"
            label={'execution checkpointing interval'}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormSelect
            name="execution.checkpointing.externalized-checkpoint-retention"
            label={"execution.checkpointing.externalized-checkpoint-retention"}
            colProps={{span: 10, offset: 1}}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkCheckpointRetain)}
          />
        </ProForm.Group>
      </ProCard>
      <ProCard
        title={"Fault Tolerance"}
        headerBordered
        collapsible={true}
      >
        <ProForm.Group>
          <ProFormSelect
            name="strategy"
            label={"restart-strategy"}
            colProps={{span: 10, offset: 1}}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkRestartStrategy)}
          />

          <ProFormDependency name={['strategy']}>
            {({strategy}) => {
              if (strategy == 'fixeddelay') {
                return (
                  <ProForm.Group>
                    <ProFormDigit
                      name="restart-strategy.fixed-delay.attempts"
                      label="restart-strategy.fixed-delay.attempts"
                      min={1}
                      fieldProps={{precision: 0}}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="restart-strategy.fixed-delay.delay"
                      label={'restart-strategy.fixed-delay.delay'}
                      colProps={{span: 10, offset: 1}}
                    />
                  </ProForm.Group>
                )
              }
              if (strategy == 'failurerate') {
                return (
                  <ProForm.Group>
                    <ProFormText
                      name="restart-strategy.failure-rate.delay"
                      label={'restart-strategy.failure-rate.delay'}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="restart-strategy.failure-rate.failure-rate-interval"
                      label={'restart-strategy.failure-rate.failure-rate-interval'}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="restart-strategy.failure-rate.max-failures-per-interval"
                      label={'restart-strategy.failure-rate.max-failures-per-interval'}
                      colProps={{span: 10, offset: 1}}
                    />

                  </ProForm.Group>
                )
              }
              if (strategy == 'exponentialdelay') {
                return (
                  <ProForm.Group>
                    <ProFormText
                      name="restart-strategy.exponential-delay.initial-backoff"
                      label={'restart-strategy.exponential-delay.initial-backoff'}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.backoff-multiplier"
                      label={'restart-strategy.exponential-delay.backoff-multiplier'}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.max-backoff"
                      label={'restart-strategy.exponential-delay.max-backoff'}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.reset-backoff-threshold"
                      label={'restart-strategy.exponential-delay.reset-backoff-threshold'}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.jitter-factor"
                      label={'restart-strategy.exponential-delay.jitter-factor'}
                      colProps={{span: 10, offset: 1}}
                    />
                  </ProForm.Group>
                )
              }
              return <ProForm.Group/>;
            }}
          </ProFormDependency>
        </ProForm.Group>
      </ProCard>
      <ProCard
        title={"High Availability"}
        headerBordered
        collapsible={true}
      >
        <ProForm.Group>
          <ProFormSelect
            name="ha"
            label={"high-availability"}
            colProps={{span: 10, offset: 1}}
            showSearch={true}
            request={() => listDictDataByType(DICT_TYPE.flinkHA)}
          />
          <ProFormText
            name="high-availability.storageDir"
            label={'high-availability.storageDir'}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name="high-availability.cluster-id"
            label={'high-availability.cluster-id'}
            colProps={{span: 10, offset: 1}}
          />

          <ProFormDependency name={['ha']}>
            {({ha}) => {
              if (ha == 'zookeeper') {
                return (
                  <ProForm.Group>
                    <ProFormText
                      name="high-availability.zookeeper.path.root"
                      label={'high-availability.zookeeper.path.root'}
                      colProps={{span: 10, offset: 1}}
                    />
                    <ProFormText
                      name="high-availability.zookeeper.quorum"
                      label={'high-availability.zookeeper.quorum'}
                      colProps={{span: 10, offset: 1}}
                    />
                  </ProForm.Group>
                )
              }
              return <ProForm.Group/>;
            }}
          </ProFormDependency>
        </ProForm.Group>
      </ProCard>
      <ProCard
        title={"Memory Configuration"}
        headerBordered
        collapsible={true}
      >
        <ProForm.Group>
          <ProFormText
            name="jobmanager.memory.process.size"
            label={'jobmanager.memory.process.size'}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name="jobmanager.memory.flink.size"
            label={'jobmanager.memory.flink.size'}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name="taskmanager.memory.process.size"
            label={'taskmanager.memory.process.size'}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name="taskmanager.memory.flink.size"
            label={'taskmanager.memory.flink.size'}
            colProps={{span: 10, offset: 1}}
          />
        </ProForm.Group>
      </ProCard>
      <ProCard
        title={"Additional Config Options"}
        headerBordered
        collapsible={true}
      >
        <ProFormList
          name="options"
        >
          <ProFormGroup>
            <ProFormText
              name="key"
              label={'Options'}
              colProps={{span: 10, offset: 1}}
            />
            <ProFormText
              name="value"
              label={'Value'}
              colProps={{span: 10, offset: 1}}
            />
          </ProFormGroup>
        </ProFormList>
      </ProCard>
    </ProForm>
  </div>);
}

export default DevBatchJob;
