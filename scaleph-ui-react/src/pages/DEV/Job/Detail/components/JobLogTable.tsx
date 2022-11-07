import { useAccess, useIntl } from 'umi';
import { useEffect, useState } from 'react';
import { FlinkJobLog, FlinkJobLogListParam } from '@/pages/DEV/Job/typings';
import { Button, Space, Table, Tooltip } from 'antd';
import { PRIVILEGE_CODE } from '@/constant';
import { ProfileOutlined } from '@ant-design/icons';
import { FlinkJobLogService } from '@/pages/DEV/Job/FlinkJobLogService';
import { ColumnsType } from 'antd/lib/table';

const JobLogTable: React.FC<{ flinkJobCode: number }> = ({ flinkJobCode }) => {
  const intl = useIntl();
  const access = useAccess();
  const [data, setData] = useState<FlinkJobLog[]>();
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0);
  const [queryParams, setQueryParams] = useState<FlinkJobLogListParam>({
    pageSize: 10,
    current: 1,
    flinkJobCode: flinkJobCode,
  });

  useEffect(() => {
    refreshLogList();
  }, []);

  const refreshLogList = () => {
    setLoading(true);
    FlinkJobLogService.list(queryParams).then((d) => {
      setData(d.data);
      setTotal(d.total);
      setLoading(false);
    });
  };
  const tableColumns: ColumnsType<FlinkJobLog> = [
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.jobId' }),
      dataIndex: 'jobId',
      key: 'jobId',
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.jobName' }),
      dataIndex: 'jobName',
      key: 'jobName',
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.version' }),
      dataIndex: 'flinkJobVersion',
      key: 'flinkJobVersion',
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.jobState' }),
      dataIndex: 'jobState',
      key: 'jobState',
      render: (text, record, index) => {
        return record.jobState?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.clusterStatus' }),
      dataIndex: 'clusterStatus',
      key: 'clusterStatus',
      render: (text, record, index) => {
        return record.clusterStatus?.label;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.startTime' }),
      dataIndex: 'startTime',
      key: 'startTime',
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.endTime' }),
      dataIndex: 'endTime',
      key: 'endTime',
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.dev.job.detail.duration' }),
      dataIndex: 'duration',
      key: 'duration',
    },
    {
      title: intl.formatMessage({ id: 'app.common.operate.label' }),
      dataIndex: 'actions',
      key: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({ id: 'app.common.operate.detail.label' })}>
                <Button
                  shape="default"
                  type="link"
                  icon={<ProfileOutlined />}
                  //  onClick={() => {
                  //    history.push("/workspace/dev/job/jar/detail", record)
                  //  }}
                />
              </Tooltip>
            )}
          </Space>
        </>
      ),
    },
  ];

  return (
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
          setQueryParams({ pageSize: pageSize, current: page, flinkJobCode: flinkJobCode });
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
  );
};

export default JobLogTable;
