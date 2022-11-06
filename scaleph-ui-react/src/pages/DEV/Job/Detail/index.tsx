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
import JobConfigurationWeb from '@/pages/DEV/Job/Detail/components/JobConfiguration';
import JobOverviewWeb from '@/pages/DEV/Job/Detail/components/JobOverview';
import { useIntl, useLocation } from '@@/exports';
import { FlinkJobForJar } from '@/pages/DEV/Job/typings';
import styles from './index.less';
import JobSavepointsWeb from './components/JobSavepoints';
import JobLogTable from './components/JobLogTable';

const JobDetailWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const params = urlParams.state as FlinkJobForJar;

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
                  console.log(params);
                }}
              >
                {intl.formatMessage({ id: 'pages.dev.job.detail.backToList' })}
              </Button>
            </Col>
          </Row>
        }
        size="small"
        extra={
          <Space>
            <div>
              <Button type="default" icon={<CaretRightOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.start' })}
              </Button>
              <Button type="default" disabled={true} icon={<PauseOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.suspend' })}
              </Button>
              <Button type="default" disabled={true} icon={<CloseOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.cancel' })}
              </Button>
            </div>
            <div>
              <Button type="default" icon={<CameraOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.savepoint' })}
              </Button>
            </div>

            <div>
              <Button type="default" icon={<DashboardOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.flinkui' })}
              </Button>
              <Button type="default" icon={<AreaChartOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.metrics' })}
              </Button>
              <Button type="default" icon={<OrderedListOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.logs' })}
              </Button>
            </div>
            <div>
              <Button type="default" icon={<EditOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.config' })}
              </Button>
              <Button type="primary" danger icon={<DeleteOutlined />}>
                {intl.formatMessage({ id: 'pages.dev.job.detail.delete' })}
              </Button>
            </div>
          </Space>
        }
        column={3}
      >
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.dev.job.detail.jobId' })}>
          <Typography.Paragraph
            copyable={true}
            ellipsis={{ rows: 1, expandable: true, symbol: 'more' }}
          >
            {params.flinkJobInstance?.jobId}
          </Typography.Paragraph>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.dev.job.detail.jobName' })}>
          <Typography.Text copyable={true}>{params.flinkJobInstance?.jobName}</Typography.Text>
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.dev.job.detail.jobState' })}>
          {params.flinkJobInstance?.jobState.label}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.dev.job.detail.startTime' })}>
          {params.flinkJobInstance?.startTime}
        </Descriptions.Item>
        <Descriptions.Item label={intl.formatMessage({ id: 'pages.dev.job.detail.duration' })}>
          {params.flinkJobInstance?.duration}
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
                    {intl.formatMessage({ id: 'pages.dev.job.detail.overview' })}
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
                    {intl.formatMessage({ id: 'pages.dev.job.detail.config' })}
                  </>
                ),
                key: 'config',
                children: (
                  <>
                    <JobConfigurationWeb
                      clusterConfig={params.flinkClusterConfig ? params.flinkClusterConfig : {}}
                      clusterInstance={
                        params.flinkClusterInstance ? params.flinkClusterInstance : {}
                      }
                      flinkConfig={params.flinkConfig}
                    />
                  </>
                ),
              },
              {
                label: (
                  <>
                    <SaveOutlined />
                    {intl.formatMessage({ id: 'pages.dev.job.detail.savepoint' })}
                  </>
                ),
                key: 'Savepoint',
                children: (
                  <>
                    <JobSavepointsWeb />
                  </>
                ),
              },
              {
                label: (
                  <>
                    <ContainerOutlined />
                    {intl.formatMessage({ id: 'pages.dev.job.detail.jobHistory' })}
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
