import { DataboardService } from '@/services/studio';
import { Card, Col, Row, Statistic } from 'antd';
import { useCallback, useEffect, useState } from 'react';
import { useIntl } from 'umi';

const DataBoard: React.FC = () => {
  const intl = useIntl();
  const [clusterCnt, setClusterCnt] = useState(0);
  const [batchJobCnt, setBatchJobCnt] = useState(0);
  const [realtimeJobCnt, setRealtimeJobCnt] = useState(0);
  const [projectCnt, setProjectCnt] = useState(0);

  useEffect(() => {
    fetchCluster();
    fetchJob({ jobType: 'b' }).then((d) => setBatchJobCnt(d));
    fetchJob({ jobType: 'r' }).then((d) => setRealtimeJobCnt(d));
    fetchProject();
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
      </Row>
    </>
  );
};

export default DataBoard;
