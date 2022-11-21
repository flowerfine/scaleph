import { useIntl } from 'umi';
import { Button, Card, Col, Form, List, Row } from 'antd';
import { FlinkClusterConfig, FlinkClusterInstance } from '@/services/project/typings';
import { FlinkClusterConfigService } from '@/services/project/flinkClusterConfig.service';

const JobConfigurationWeb: React.FC<{
  clusterConfig: FlinkClusterConfig;
  clusterInstance: FlinkClusterInstance;
  flinkConfig?: { [key: string]: any };
}> = ({ clusterConfig, clusterInstance, flinkConfig }) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  const configOptions = FlinkClusterConfigService.setData(
    new Map(Object.entries(flinkConfig ? flinkConfig : {})),
  );

  return (
    <>
      <Row gutter={[12, 12]}>
        <Col span={12}>
          <Card title={intl.formatMessage({ id: 'pages.dev.job.detail.config.state' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="state.backend"></List.Item.Meta>
                <div>{clusterConfig['state.backend']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="state.savepoints.dir"></List.Item.Meta>
                <div>{clusterConfig['state.savepoints.dir']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="state.checkpoints.dir"></List.Item.Meta>
                <div>{clusterConfig['state.checkpoints.dir']}</div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={12}>
          <Card title={intl.formatMessage({ id: 'pages.dev.job.detail.config.checkpoint' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="execution.checkpointing.mode"></List.Item.Meta>
                <div>{clusterConfig['execution.checkpointing.mode']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="execution.checkpointing.unaligned"></List.Item.Meta>
                <div>{clusterConfig['execution.checkpointing.unaligned']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="execution checkpointing interval"></List.Item.Meta>
                <div>{clusterConfig['execution checkpointing interval']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="execution.checkpointing.externalized-checkpoint-retention"></List.Item.Meta>
                <div>
                  {clusterConfig['execution.checkpointing.externalized-checkpoint-retention']}
                </div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={8}>
          <Card title={intl.formatMessage({ id: 'pages.dev.job.detail.config.resource' })}>
          <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="jobmanager.memory.process.size"></List.Item.Meta>
                <div>{clusterConfig['jobmanager.memory.process.size']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="jobmanager.memory.flink.size"></List.Item.Meta>
                <div>{clusterConfig['jobmanager.memory.flink.size']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="taskmanager.memory.process.size"></List.Item.Meta>
                <div>{clusterConfig['taskmanager.memory.process.size']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="taskmanager.memory.flink.size"></List.Item.Meta>
                <div>{clusterConfig['taskmanager.memory.flink.size']}</div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={8}>
          <Card title={intl.formatMessage({ id: 'pages.dev.job.detail.config.ha' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="high-availability"></List.Item.Meta>
                <div>{clusterConfig['high-availability']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="high-availability.storageDir"></List.Item.Meta>
                <div>{clusterConfig['high-availability.storageDir']}</div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="high-availability.cluster-id"></List.Item.Meta>
                <div>{clusterConfig['high-availability.cluster-id']}</div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={8}>
          <Card title={intl.formatMessage({ id: 'pages.dev.job.detail.config.fault' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="restart-strategy"></List.Item.Meta>
                <div>{clusterConfig['restart-strategy']}</div>
              </List.Item>
            </List>
          </Card>
        </Col>
      </Row>
    </>
  );
  // return (<ProForm
  //   form={form}
  //   layout={'horizontal'}
  //   initialValues={configOptions}
  //   grid={true}
  //   rowProps={{gutter: [16, 8]}}
  //   submitter={false}>
  //   <State/>
  //   <FaultTolerance/>
  //   <HighAvailability/>
  //   <Resource/>
  //   <Additional/>
  // </ProForm>);

  return (
    <>
      <Button
        onClick={() => {
          console.log(configOptions);
        }}
      >
        button
      </Button>
    </>
  );
};

export default JobConfigurationWeb;
