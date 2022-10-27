import {PRIVILEGE_CODE} from '@/constant';
import {SeaTunnelConnectorFile} from '@/services/resource/typings';
import {history} from '@@/core/history';
import {DownloadOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, Space, Tooltip} from 'antd';
import {useEffect, useRef, useState} from 'react';
import {useAccess, useIntl, useLocation} from 'umi';
import {SeatunnelReleaseService} from "@/services/resource/seatunnelRelease.service";

const SeaTunnelConnectorResource: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [seatunnelReleaseId, setSeatunnelReleaseId] = useState<number>(0);

  useEffect(() => {
    const params = urlParams.state as { id: number };
    if (params) {
      setSeatunnelReleaseId(params?.id);
    }
  }, []);

  const tableColumns: ProColumns<SeaTunnelConnectorFile>[] = [
    {
      title: intl.formatMessage({id: 'pages.resource.credentialFile.name'}),
      dataIndex: 'name',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.credentialFile.len'}),
      dataIndex: 'len',
      width: 280,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.resource.credentialFile.blockSize' }),
      dataIndex: 'blockSize',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.credentialFile.accessTime'}),
      dataIndex: 'accessTime',
      hideInSearch: true,
      valueType: 'dateTime',
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.resource.credentialFile.modificationTime'}),
      dataIndex: 'modificationTime',
      hideInSearch: true,
      valueType: 'dateTime',
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'app.common.operate.label'}),
      dataIndex: 'actions',
      align: 'center',
      width: 120,
      fixed: 'right',
      valueType: 'option',
      render: (_, record) => (
        <>
          <Space>
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDownload) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.download.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DownloadOutlined/>}
                  onClick={() => SeatunnelReleaseService.downloadConnector(seatunnelReleaseId, record.name)}
                />
              </Tooltip>
            )}
          </Space>
        </>
      ),
    },
  ];

  return (
    <ProTable<SeaTunnelConnectorFile>
      headerTitle={
        <Button key="return" type="default" onClick={() => history.back()}>
          {intl.formatMessage({id: 'app.common.operate.return.label'})}
        </Button>
      }
      search={false}
      rowKey="name"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) => {
        return SeatunnelReleaseService.listConnectors(seatunnelReleaseId);
      }}
      pagination={false}
      rowSelection={false}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  );
};

export default SeaTunnelConnectorResource;
