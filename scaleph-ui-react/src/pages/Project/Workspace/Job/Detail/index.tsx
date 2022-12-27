import { Button, Col, Descriptions, message, Modal, Row, Space, Tabs, Typography } from 'antd';
import {
  CameraOutlined,
  CaretRightOutlined,
  CloseOutlined,
  ContainerOutlined,
  DashboardOutlined,
  DeleteOutlined,
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
import { FlinkJobInstanceService } from '@/services/project/FlinkJobInstanceService';
import JobEditForm from '../components/JobEditForm';
import { FlinkJobService } from '@/services/project/FlinkJobService';

const JobDetailWeb: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const params = urlParams.state as WsFlinkJob;
  const [flinkJobInstance, setFlinkJobInstance] = useState<WsFlinkJobInstance>();
  const [flinkClusterConfig, setFlinkClusterConfig] = useState<WsFlinkClusterConfig>();
  const [flinkClusterInstance, setFlinkClusterInstance] = useState<WsFlinkClusterInstance>();
  const [jobEditFormData, setJobEditFormData] = useState<{ visible: boolean; data: any }>({
    visible: false,
    data: {},
  });

  useEffect(() => {
    setFlinkJobInstance(params.wsFlinkJobInstance);
    setFlinkClusterConfig(params.wsFlinkClusterConfig);
    setFlinkClusterInstance(params.wsFlinkClusterInstance);
  }, []);

  const refresh = () => {
    FlinkJobService.selectOne(params.id as number).then((d) => {
      setFlinkJobInstance(d.data?.wsFlinkJobInstance);
      setFlinkClusterConfig(d.data?.wsFlinkClusterConfig);
      setFlinkClusterInstance(d.data?.wsFlinkClusterInstance);
    });
  };

  const isJobEditable = () => {
    if (flinkJobInstance?.jobId == undefined) {
      return true;
    }
    if (
      flinkJobInstance?.jobId &&
      flinkJobInstance.jobState.value != 'CANCELED' &&
      flinkJobInstance.jobState.value != 'FAILED' &&
      flinkJobInstance.jobState.value != 'FINISHED' &&
      flinkJobInstance.jobState.value != 'SUBMIT_FAILED'
    ) {
      return false;
    } else {
      return true;
    }
  };

  const isJobStartable = () => {
    if (isJobEditable() && flinkJobInstance?.jobState.value == 'SUSPENDED') {
      return true;
    } else {
      return false;
    }
  };

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
              <Button
                type="default"
                icon={<CaretRightOutlined />}
                disabled={!isJobStartable()}
                onClick={() => {
                  Modal.confirm({
                    type: 'info',
                    title: intl.formatMessage({ id: 'pages.project.job.detail.start.title' }),
                    content: intl.formatMessage({
                      id: 'pages.project.job.detail.start.content',
                    }),
                    okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                    cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                    onOk() {
                      FlinkJobInstanceService.submit(params).then((d) => {
                        if (d.success) {
                          message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                          refresh();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({ id: 'pages.project.job.detail.start' })}
              </Button>
              <Button
                type="default"
                disabled={flinkJobInstance?.jobState.value != 'RUNNING'}
                icon={<PauseOutlined />}
                onClick={() => {
                  Modal.confirm({
                    type: 'info',
                    title: intl.formatMessage({ id: 'pages.project.job.detail.start.title' }),
                    content: intl.formatMessage({
                      id: 'pages.project.job.detail.start.content',
                    }),
                    okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                    cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                    onOk() {
                      FlinkJobInstanceService.stop(flinkJobInstance?.id as number).then((d) => {
                        if (d.success) {
                          message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                          refresh();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({ id: 'pages.project.job.detail.suspend' })}
              </Button>
              <Button
                type="default"
                disabled={flinkJobInstance?.jobState.value != 'RUNNING'}
                icon={<CloseOutlined />}
                onClick={() => {
                  Modal.confirm({
                    type: 'info',
                    title: intl.formatMessage({ id: 'pages.project.job.detail.start.title' }),
                    content: intl.formatMessage({
                      id: 'pages.project.job.detail.start.content',
                    }),
                    okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                    cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                    onOk() {
                      FlinkJobInstanceService.cancel(flinkJobInstance?.id as number).then((d) => {
                        if (d.success) {
                          message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                          refresh();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({ id: 'pages.project.job.detail.cancel' })}
              </Button>
            </div>
            <div>
              <Button
                type="default"
                disabled={flinkJobInstance?.jobState.value != 'RUNNING'}
                icon={<CameraOutlined />}
                onClick={() => {
                  Modal.confirm({
                    type: 'info',
                    title: intl.formatMessage({ id: 'pages.project.job.detail.start.title' }),
                    content: intl.formatMessage({
                      id: 'pages.project.job.detail.start.content',
                    }),
                    okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                    cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                    onOk() {
                      FlinkJobInstanceService.savepoint(flinkJobInstance?.id as number).then(
                        (d) => {
                          if (d.success) {
                            message.success(
                              intl.formatMessage({ id: 'app.common.operate.success' }),
                            );
                            refresh();
                          }
                        },
                      );
                    },
                  });
                }}
              >
                {intl.formatMessage({ id: 'pages.project.job.detail.savepoint' })}
              </Button>
            </div>

            <div>
              <a href={flinkClusterInstance?.webInterfaceUrl} target="_blank">
                <Button type="default" icon={<DashboardOutlined />}>
                  {intl.formatMessage({ id: 'pages.project.job.detail.flinkui' })}
                </Button>
              </a>
              {/* <Button type="default" icon={<AreaChartOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.metrics' })}
              </Button>
              <Button type="default" icon={<OrderedListOutlined />}>
                {intl.formatMessage({ id: 'pages.project.job.detail.logs' })}
              </Button> */}
            </div>
            <div>
              {/* 
              //todo update job instance flink config in dynamic
              <Button
                type="default"
                icon={<EditOutlined />}
                onClick={() => {
                  setJobEditFormData({ visible: true, data: { params } });
                }}
              >
                {intl.formatMessage({ id: 'pages.project.job.detail.config' })}
              </Button> */}
              <Button
                type="primary"
                danger
                icon={<DeleteOutlined />}
                disabled={!isJobEditable()}
                onClick={() => {
                  Modal.confirm({
                    type: 'info',
                    title: intl.formatMessage({ id: 'app.common.operate.delete.confirm.title' }),
                    content: intl.formatMessage({
                      id: 'app.common.operate.delete.confirm.content',
                    }),
                    okText: intl.formatMessage({ id: 'app.common.operate.confirm.label' }),
                    cancelText: intl.formatMessage({ id: 'app.common.operate.cancel.label' }),
                    onOk() {
                      FlinkJobService.delete(params).then((d) => {
                        if (d.success) {
                          message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                          refresh();
                        }
                      });
                    },
                  });
                }}
              >
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
                    {intl.formatMessage({ id: 'pages.project.job.detail.checkpoint' })}
                  </>
                ),
                key: 'checkpoint',
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
      {jobEditFormData.visible && (
        <JobEditForm
          data={jobEditFormData.data}
          onCancel={() => {
            setJobEditFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setJobEditFormData({ visible: visible, data: {} });
            refresh();
          }}
          visible={jobEditFormData.visible}
        ></JobEditForm>
      )}
    </div>
  );
};

export default JobDetailWeb;
