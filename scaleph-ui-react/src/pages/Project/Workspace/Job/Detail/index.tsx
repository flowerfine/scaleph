import { Button, Col, Descriptions, Row, Space, Tabs, Typography } from 'antd';
import {
  AreaChartOutlined,
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  ContainerOutlined,
  DashboardOutlined,
  DeleteOutlined,
  EditOutlined,
  OrderedListOutlined,
  PauseOutlined,
  ProfileOutlined,
  RollbackOutlined,
  SaveOutlined,
  ToolOutlined,
} from '@ant-design/icons';
import { useIntl, useLocation } from 'umi';
import styles from './index.less';
import JobSavepointsWeb from './components/JobSavepoints';
import JobLogTable from './components/JobLogTable';
import JobConfigurationWeb from './components/JobConfiguration';
import { useEffect, useState } from 'react';
import {
  WsFlinkClusterConfig,
  WsFlinkClusterInstance,
  WsFlinkJob,
  WsFlinkJobInstance,
} from '@/services/project/typings';
import JobOverviewWeb from './components/JobOverview';

const JobDetailWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const params = urlParams.state as WsFlinkJob;
  const [flinkJobInstance, setFlinkJobInstance] = useState<WsFlinkJobInstance>();
  const [flinkClusterConfig, setFlinkClusterConfig] = useState<WsFlinkClusterConfig>();
  const [flinkClusterInstance, setFlinkClusterInstance] = useState<WsFlinkClusterInstance>();

  useEffect(() => {
    setFlinkJobInstance(params.wsFlinkJobInstance);
    setFlinkClusterConfig(params.wsFlinkClusterConfig);
    setFlinkClusterInstance(params.wsFlinkClusterInstance);
  }, []);

  return (
    <div className={styles.mainContainer}>
      <Descriptions
        contentStyle={{ fontWeight: 'bold' }}
        title={
          <Row gutter={[0, 12]} justify="start" align="middle">
            <Col>
              <Typography.Title level={5} copyable={false}>
                {params.name}
              </Typography.Title>
            </Col>
            <Col>
              <Button
                shape="default"
                type="link"
                icon={<RollbackOutlined />}
                size="small"
                onClick={() => {
                  window.history.back();
                }}
              >
                {intl.formatMessage({ id: 'pages.project.job.detail.backToList' })}
              </Button>
            </Col>
          </Row>
        }
        size="small"
        extra={
          <Space>
            <div>
              <Button type="default" icon={<CaretRightOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.start' })}
              </Button>
              <Button type="default" disabled={true} icon={<PauseOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.suspend' })}
              </Button>
              <Button type="default" disabled={true} icon={<CloseOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.cancel' })}
              </Button>
            </div>
            <div>
              <Button type="default" icon={<CameraOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.savepoint' })}
              </Button>
            </div>

            <div>
              <Button type="default" icon={<DashboardOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.flinkui' })}
              </Button>
              <Button type="default" icon={<AreaChartOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.metrics' })}
              </Button>
              <Button type="default" icon={<OrderedListOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.logs' })}
              </Button>
            </div>
            <div>
              <Button type="default" icon={<EditOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.config' })}
              </Button>
              <Button type="primary" danger icon={<DeleteOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.delete' })}
              </Button>
            </div>
          </Space>
        }
        column={3}
      >
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.job.detail.jobId' })}>
          <Typography.Paragraph
            copyable={true}
            ellipsis={{ rows: 1, expandable: true, symbol: 'more' }}
          >
            {flinkJobInstance?.jobId ? flinkJobInstance?.jobId : '-'}
          </Typography.Paragraph>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.job.detail.jobName' })}>
          <Typography.Text copyable={true}>
            {flinkJobInstance?.jobName ? flinkJobInstance?.jobName : '-'}
          </Typography.Text>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.job.detail.jobState' })}>
          {flinkJobInstance?.jobState?.label ? flinkJobInstance?.jobState?.label : '-'}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.job.detail.startTime' })}>
          {flinkJobInstance?.startTime ? flinkJobInstance?.startTime : '-'}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.job.detail.duration' })}>
          {flinkJobInstance?.duration ? flinkJobInstance?.duration : '-'}
        </Descriptions.Item>
      </Descriptions>
      <Row>
        <Col span={24}>
          <Tabs
            defaultActiveKey="overview"
            type="line"
            onChange={() => {}}
            items={[
              {
                label: (
                  <>
                    <ProfileOutlined />
                    {intl.formatMessage({ id: 'pages.project.job.detail.overview' })}
                  </>
                ),
                key: 'overview',
                children: (
                  <>
                    <JobOverviewWeb data={params} />
                  </>
                ),
              },
              {
                label: (
                  <>
                    <ToolOutlined />
                    {intl.formatMessage({ id: 'pages.project.job.detail.config' })}
                  </>
                ),
                key: 'config',
                children: (
                  <>
                    <JobConfigurationWeb
                      clusterConfig={flinkClusterConfig ? flinkClusterConfig : {}}
                      clusterInstance={flinkClusterInstance ? flinkClusterInstance : {}}
                      flinkConfig={params.flinkConfig}
                    />
                  </>
                ),
              },
              {
                label: (
                  <>
                    <SaveOutlined />
                    {intl.formatMessage({ id: 'pages.project.job.detail.savepoint' })}
                  </>
                ),
                key: 'Savepoint',
                children: (
                  <>
                    <JobSavepointsWeb flinkJobInstanceId={flinkJobInstance?.id as number} />
                  </>
                ),
              },
              {
                label: (
                  <>
                    <ContainerOutlined />
                    {intl.formatMessage({ id: 'pages.project.job.detail.jobHistory' })}
                  </>
                ),
                key: 'jobHistory',
                children: (
                  <>
                    <JobLogTable flinkJobCode={params.code ? params.code : 0} />
                  </>
                ),
              },
            ]}
          ></Tabs>
        </Col>
      </Row>
    </div>
  );
};

export default JobDetailWeb;
