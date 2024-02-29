import { DataboardService } from '@/services/studio';
import { topBatch100 } from '@/services/studio/typings'
import { Card, Col, Row, Statistic, Table } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useCallback, useEffect, useState } from 'react';
import { useIntl } from 'umi';

const DataBoard: React.FC = () => {
  const intl = useIntl();
  const [clusterCnt, setClusterCnt] = useState(0);
  const [batchJobCnt, setBatchJobCnt] = useState(0);
  const [realtimeJobCnt, setRealtimeJobCnt] = useState(0);
  const [projectCnt, setProjectCnt] = useState(0);
  const [topBatch100, setTopBatch100] = useState<topBatch100[]>([]);

  useEffect(() => {
    fetchCluster();
    fetchJob({ jobType: 'b' }).then((d) => setBatchJobCnt(d));
    fetchJob({ jobType: 'r' }).then((d) => setRealtimeJobCnt(d));
    fetchProject();
    fetchTopBatch100();
    console.log(topBatch100)
  }, []);
  // 集群数量
  const fetchCluster = useCallback(async () => {
    const clusterCnt = await DataboardService.cluster();
    setClusterCnt(clusterCnt);
  }, []);
  // 任务数量
  const fetchJob = useCallback(async (params: { jobType: string }) => {
    const jobCnt = await DataboardService.job(params);
    return jobCnt;
  }, []);
  // 项目数量
  const fetchProject = useCallback(async () => {
    const projectCnt = await DataboardService.project();
    setProjectCnt(projectCnt);
  }, []);
  // 7日任务运行TOP100
  const fetchTopBatch100 = useCallback(async () => {
    const topBatch100 = await DataboardService.topBatch100();
    setTopBatch100(topBatch100);
  }, []);

  const columns: ColumnsType<topBatch100> = [
    {
      title: 'ID',
      dataIndex: 'id'
    },
    {
      title: '项目',
      dataIndex: 'project',
    },
    {
      title: '作业 ID',
      dataIndex: 'jobId',
    },
    {
      title: '作业 CODE',
      dataIndex: 'jobCode',
    },
    {
      title: '用户 ID',
      dataIndex: 'clusterId',
    },
    {
      title: '用户',
      dataIndex: 'cluster',
    },
    {
      title: '作业实例 ID',
      dataIndex: 'jobInstanceId',
    },
    {
      title: '作业日志 URL',
      dataIndex: 'jobLogUrl',
    },
    {
      title: '开始时间',
      dataIndex: 'startTime',
    },
    {
      title: '结束时间',
      dataIndex: 'endTime',
    },
    {
      title: '运行时长',
      dataIndex: 'duration',
    },
    {
      title: '作业实例状态',
      dataIndex: 'jobInstanceState'
    },
  ];

  return (
    <>
      <Row gutter={[4, 16]}>
      <Col span="6">
          <Card>
            <Statistic
              title={intl.formatMessage({ id: 'pages.studio.databoard.projectCnt' })}
              value={projectCnt}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col span="6">
          <Card>
            <Statistic
              title={intl.formatMessage({ id: 'pages.studio.databoard.clusterCnt' })}
              value={clusterCnt}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col span="6">
          <Card>
            <Statistic
              title={intl.formatMessage({ id: 'pages.studio.databoard.batchJobCnt' })}
              value={batchJobCnt}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col span="6">
          <Card>
            <Statistic
              title={intl.formatMessage({ id: 'pages.studio.databoard.realtimeJobCnt' })}
              value={realtimeJobCnt}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col span={24}>
            <Card title="作业TOP100">
              <Table columns={columns} dataSource={topBatch100} size="middle" scroll={{ x: 1500, y: 300 }} />
            </Card>
          </Col>
      </Row>
    </>
  );
};

export default DataBoard;
