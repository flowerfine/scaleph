import {PRIVILEGE_CODE} from '@/constant';
import {CredentialFile, SeaTunnelConnectorFile} from '@/services/resource/typings';
import {history} from '@@/core/history';
import {DownloadOutlined} from '@ant-design/icons';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {Button, message, Space, Tooltip} from 'antd';
import {useEffect, useRef, useState} from 'react';
import {useAccess, useIntl, useLocation} from 'umi';
import {SeatunnelReleaseService} from "@/services/resource/seatunnelRelease.service";
import ConnectorFileForm from "@/pages/Resource/SeaTunnelConnector/components/ConnectorFileForm";

const SeaTunnelConnectorResource: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [seatunnelReleaseId, setSeatunnelReleaseId] = useState<number>(0);
  const [connectorFormData, setConnectorFormData] = useState<{
    visiable: boolean;
    data: CredentialFile;
  }>({visiable: false, data: {}});

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
      title: intl.formatMessage({id: 'pages.resource.credentialFile.blockSize'}),
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
    <div>
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
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="refresh"
                type="primary"
                onClick={() => actionRef.current?.reload()}
              >
                {intl.formatMessage({id: 'app.common.operate.refresh.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="fetch"
                type="default"
                onClick={() => {
                  SeatunnelReleaseService.fetch(seatunnelReleaseId).then((response) => {
                    if (response.success) {
                      message.success(intl.formatMessage({id: 'app.common.operate.success'}));
                      actionRef.current?.reload();
                    }
                  })
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.fetch.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="upload"
                type="default"
                onClick={() => {
                  setConnectorFormData({visiable: true, data: {}});
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.upload.label'})}
              </Button>
            ),
          ],
        }}
        pagination={false}
        rowSelection={false}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
      {connectorFormData.visiable && (
        <ConnectorFileForm
          visible={connectorFormData.visiable}
          onCancel={() => setConnectorFormData({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setConnectorFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={{id: seatunnelReleaseId}}
        />
      )}
    </div>
  );
};

export default SeaTunnelConnectorResource;
