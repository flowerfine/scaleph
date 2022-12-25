import { useIntl } from 'umi';
import { Card, Col, List, Row } from 'antd';
import { WsFlinkClusterConfig, WsFlinkClusterInstance } from '@/services/project/typings';
import { FlinkClusterConfigService } from '@/services/project/flinkClusterConfig.service';

const JobConfigurationWeb: React.FC<{
  clusterConfig: WsFlinkClusterConfig;
  clusterInstance: WsFlinkClusterInstance;
  flinkConfig?: { [key: string]: any };
}> = ({ clusterConfig, clusterInstance, flinkConfig }) => {
  const intl = useIntl();

  const flinkConfigOptions = FlinkClusterConfigService.setData(
    new Map(Object.entries(flinkConfig ? flinkConfig : {})),
  );

  const clusterConfigOptions = FlinkClusterConfigService.setData(
    new Map(Object.entries(clusterConfig.configOptions ? clusterConfig.configOptions : {})),
  );

  return (
    <>
      <Row gutter={[12, 12]}>
        <Col span={12}>
          <Card title={intl.formatMessage({ id: 'pages.project.job.detail.config.state' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="state.backend"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['state.backend']
                    ? flinkConfigOptions['state.backend']
                    : clusterConfigOptions['state.backend']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="state.savepoints.dir"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['state.savepoints.dir']
                    ? flinkConfigOptions['state.savepoints.dir']
                    : clusterConfigOptions['state.savepoints.dir']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="state.checkpoints.dir"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['state.checkpoints.dir']
                    ? flinkConfigOptions['state.checkpoints.dir']
                    : clusterConfigOptions['state.checkpoints.dir']}
                </div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={12}>
          <Card title={intl.formatMessage({ id: 'pages.project.job.detail.config.checkpoint' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="execution.checkpointing.mode"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['execution.checkpointing.mode']
                    ? flinkConfigOptions['execution.checkpointing.mode']
                    : clusterConfigOptions['execution.checkpointing.mode']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="execution.checkpointing.unaligned"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['execution.checkpointing.unaligned']
                    ? flinkConfigOptions['execution.checkpointing.unaligned']
                    : clusterConfigOptions['execution.checkpointing.unaligned']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="execution checkpointing interval"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['execution checkpointing interval']
                    ? flinkConfigOptions['execution checkpointing interval']
                    : clusterConfigOptions['execution checkpointing interval']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="execution.checkpointing.externalized-checkpoint-retention"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['execution.checkpointing.externalized-checkpoint-retention']
                    ? flinkConfigOptions[
                        'execution.checkpointing.externalized-checkpoint-retention'
                      ]
                    : clusterConfigOptions[
                        'execution.checkpointing.externalized-checkpoint-retention'
                      ]}
                </div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={8}>
          <Card title={intl.formatMessage({ id: 'pages.project.job.detail.config.resource' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="jobmanager.memory.process.size"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['jobmanager.memory.process.size']
                    ? flinkConfigOptions['jobmanager.memory.process.size']
                    : clusterConfigOptions['jobmanager.memory.process.size']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="jobmanager.memory.flink.size"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['jobmanager.memory.flink.size']
                    ? flinkConfigOptions['jobmanager.memory.flink.size']
                    : clusterConfigOptions['jobmanager.memory.flink.size']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="taskmanager.memory.process.size"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['taskmanager.memory.process.size']
                    ? flinkConfigOptions['taskmanager.memory.process.size']
                    : clusterConfigOptions['taskmanager.memory.process.size']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="taskmanager.memory.flink.size"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['taskmanager.memory.flink.size']
                    ? flinkConfigOptions['taskmanager.memory.flink.size']
                    : clusterConfigOptions['taskmanager.memory.flink.size']}
                </div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={8}>
          <Card title={intl.formatMessage({ id: 'pages.project.job.detail.config.ha' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="high-availability"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['high-availability']
                    ? flinkConfigOptions['high-availability']
                    : clusterConfigOptions['high-availability']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="high-availability.storageDir"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['high-availability.storageDir']
                    ? flinkConfigOptions['high-availability.storageDir']
                    : clusterConfigOptions['high-availability.storageDir']}
                </div>
              </List.Item>
              <List.Item>
                <List.Item.Meta title="high-availability.cluster-id"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['high-availability.cluster-id']
                    ? flinkConfigOptions['high-availability.cluster-id']
                    : clusterConfigOptions['high-availability.cluster-id']}
                </div>
              </List.Item>
            </List>
          </Card>
        </Col>
        <Col span={8}>
          <Card title={intl.formatMessage({ id: 'pages.project.job.detail.config.fault' })}>
            <List itemLayout="horizontal">
              <List.Item>
                <List.Item.Meta title="restart-strategy"></List.Item.Meta>
                <div>
                  {flinkConfigOptions['restart-strategy']
                    ? flinkConfigOptions['restart-strategy']
                    : clusterConfigOptions['restart-strategy']}
                </div>
              </List.Item>
            </List>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default JobConfigurationWeb;
