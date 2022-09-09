import {
  FooterToolbar,
  ProCard,
  ProForm,
  ProFormDependency,
  ProFormFieldSet,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from "@ant-design/pro-components";
import {Col, message, Row} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE, RESOURCE_TYPE} from "@/constant";
import {ResourceListParam} from "@/services/resource/typings";
import {list} from "@/services/resource/resource.service";
import {useIntl} from "@@/exports";
import {FlinkClusterConfig} from "@/services/dev/typings";
import {add} from "@/services/dev/flinkClusterConfig.service";

const DevBatchJob: React.FC = () => {
  const intl = useIntl();

  return (<div>
    <ProForm
      layout={"horizontal"}
      grid={true}
      rowProps={{gutter: [16, 8]}}
      // colProps={{span: 10, offset: 1}}
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
      }}
      onFinish={(value) => {
        console.log(value)
        const options: { [key: string]: any } = {}
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

        value.options.forEach(function (item: Record<string, any>) {
          options[item.key] = item.value
        })

        const param: FlinkClusterConfig = {
          name: value['name'],
          flinkVersion: {value: value['flinkVersion']},
          resourceProvider: {value: value['resourceProvider']},
          deployMode: {value: value['deployMode']},
          flinkRelease: {id: value['flinkRelease']},
          clusterCredential: {id: value['clusterCredential']},
          remark: value['remark'],
          configOptions: options
        }
        return add(param).then((d) => {
          if (d.success) {
            message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
          }
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
            request={() => listDictDataByType(DICT_TYPE.flinkDeploymentMode)}
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
                  return {label: item.name, value: item.id}
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
          <ProFormFieldSet
            name="state.savepoints.dir"
            label={"state.savepoints.dir"}
            type="group"
            wrapperCol={{span: 10}}
            transform={(value) => {
              return {"state.savepoints.dir": value[0] + value[1]}
            }}
          >
            <ProFormSelect
              name="protocol"
              initialValue={'file://'}
              valueEnum={{'file://': 'file://'}}
              colProps={{span: 4}}
            />
            <ProFormText
              name="dir"
              label={'dir'}
              colProps={{span: 10}}
            />
          </ProFormFieldSet>
          <ProFormFieldSet
            name="state.checkpoints.dir"
            label={"state.checkpoints.dir"}
            type="group"
            wrapperCol={{span: 10}}
            transform={(value) => {
              return {"state.checkpoints.dir": value[0] + value[1]}
            }}
          >
            <ProFormSelect
              name="protocol"
              initialValue={'file://'}
              valueEnum={{'file://': 'file://'}}
              colProps={{span: 4}}
            />
            <ProFormText
              name="dir"
              label={'dir'}
              colProps={{span: 10}}
            />
          </ProFormFieldSet>

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
                    <ProFormText
                      name="restart-strategy.fixed-delay.attempts"
                      label={'restart-strategy.fixed-delay.attempts'}
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
