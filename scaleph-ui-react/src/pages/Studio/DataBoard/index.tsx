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
      title: 'id',
      dataIndex: 'id'
    },
    {
      title: 'project',
      dataIndex: 'project',
    },
    {
      title: 'jobId',
      dataIndex: 'jobId',
    },
    {
      title: 'jobCode',
      dataIndex: 'jobCode',
    },
    {
      title: 'clusterId',
      dataIndex: 'clusterId',
    },
    {
      title: 'cluster',
      dataIndex: 'cluster',
    },
    {
      title: 'jobInstanceId',
      dataIndex: 'jobInstanceId',
    },
    {
      title: 'jobLogUrl',
      dataIndex: 'jobLogUrl',
    },
    {
      title: 'startTime',
      dataIndex: 'startTime',
    },
    {
      title: 'endTime',
      dataIndex: 'endTime',
    },
    {
      title: 'duration',
      dataIndex: 'duration',
    },
    {
      title: 'jobInstanceState',
      dataIndex: 'jobInstanceState'
    },
  ];

  return (
    <>
      <Row gutter={4}>
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
        <Col span={12}>
          <Table columns={columns} dataSource={topBatch100} size="middle" scroll={{ x: 1500, y: 300 }} />
        </Col>
        <Col span={12}>
        </Col>
      </Row>
    </>
  );
};

export default DataBoard;
