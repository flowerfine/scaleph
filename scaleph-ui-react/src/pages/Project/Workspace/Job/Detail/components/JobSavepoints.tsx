import { FlinkJobInstanceService } from '@/services/project/FlinkJobInstanceService';
import { WsFlinkCheckPoint, WsFlinkCheckPointParam } from '@/services/project/typings';
import { Table } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import { useEffect, useState } from 'react';
import { useAccess, useIntl } from 'umi';

const JobSavepointsWeb: React.FC<{ flinkJobInstanceId: number }> = ({ flinkJobInstanceId }) => {
  const intl = useIntl();
  const access = useAccess();
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0);
  const [data, setData] = useState<WsFlinkCheckPoint[]>();
  const [queryParams, setQueryParams] = useState<WsFlinkCheckPointParam>({
    pageSize: 10,
    current: 1,
    flinkJobInstanceId: flinkJobInstanceId,
  });

  useEffect(() => {
    refreshLogList();
  }, []);

  const refreshLogList = () => {
    setLoading(true);
    FlinkJobInstanceService.listCheckPoints(queryParams).then((d) => {
      setData(d.data);
      setTotal(d.total);
      setLoading(false);
    });
  };

  const tableColumns: ColumnsType<WsFlinkCheckPoint> = [
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.flinkCheckpointId' }),
      dataIndex: 'flinkCheckpointId',
      key: 'flinkCheckpointId',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.checkpointType' }),
      dataIndex: 'checkpointType',
      key: 'checkpointType',
      render: (text, record, index) => {
        return record.checkpointType?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.FlinkCheckpointStatus' }),
      dataIndex: 'FlinkCheckpointStatus',
      key: 'FlinkCheckpointStatus',
      render: (text, record, index) => {
        return record.status?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.savepoint' }),
      dataIndex: 'savepoint',
      key: 'savepoint',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.triggerTimestamp' }),
      dataIndex: 'triggerTimestamp',
      key: 'triggerTimestamp',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.duration' }),
      dataIndex: 'duration',
      key: 'duration',
    },
    // {
    //   title: intl.formatMessage({ id: 'pages.project.job.detail.discarded' }),
    //   dataIndex: 'discarded',
    //   key: 'discarded',
    // },
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.externalPath' }),
      dataIndex: 'externalPath',
      key: 'externalPath',
    },
    {
      title: intl.formatMessage({ id: 'pages.project.job.detail.stateSize' }),
      dataIndex: 'stateSize',
      key: 'stateSize',
    },
    // {
    //   title: intl.formatMessage({ id: 'pages.project.job.detail.processedData' }),
    //   dataIndex: 'processedData',
    //   key: 'processedData',
    // },
    // {
    //   title: intl.formatMessage({ id: 'pages.project.job.detail.persistedData' }),
    //   dataIndex: 'persistedData',
    //   key: 'persistedData',
    // },
    // {
    //   title: intl.formatMessage({ id: 'pages.project.job.detail.alignmentBuffered' }),
    //   dataIndex: 'alignmentBuffered',
    //   key: 'alignmentBuffered',
    // },
    // {
    //   title: intl.formatMessage({ id: 'pages.project.job.detail.numSubtasks' }),
    //   dataIndex: 'numSubtasks',
    //   key: 'numSubtasks',
    // },
    // {
    //   title: intl.formatMessage({ id: 'pages.project.job.detail.numAcknowledgedSubtasks' }),
    //   dataIndex: 'numAcknowledgedSubtasks',
    //   key: 'numAcknowledgedSubtasks',
    // },
    // {
    //   title: intl.formatMessage({ id: 'pages.project.job.detail.latestAckTimestamp' }),
    //   dataIndex: 'latestAckTimestamp',
    //   key: 'latestAckTimestamp',
    // },
  ];
  return (
    <>
      <Table
        columns={tableColumns}
        rowKey={(record) => record.id}
        dataSource={data}
        loading={loading}
        pagination={{
          position: ['bottomRight'],
          size: 'small',
          total: total,
          showSizeChanger: true,
          onChange: (page, pageSize) => {
            setQueryParams({
              pageSize: pageSize,
              current: page,
              flinkJobInstanceId: flinkJobInstanceId,
            });
            refreshLogList();
          },
          showTotal: (total, range) => (
            <>
              {intl.formatMessage({ id: 'app.common.pagination.from' }) +
                ' ' +
                range[0] +
                '-' +
                range[1] +
                ' ' +
                intl.formatMessage({ id: 'app.common.pagination.to' }) +
                ' ' +
                total +
                ' ' +
                intl.formatMessage({ id: 'app.common.pagination.total' })}
            </>
          ),
        }}
      ></Table>
    </>
  );
};

export default JobSavepointsWeb;
