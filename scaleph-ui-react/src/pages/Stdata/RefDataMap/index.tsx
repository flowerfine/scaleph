import {useAccess, useIntl} from "@umijs/max";
import {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {MetaDataMap} from "@/services/stdata/typings";
import {RefdataService} from "@/services/stdata/refdata.service";
import RefDataMapForm from "@/pages/Stdata/RefDataMap/components/RefDataMapForm";

const RefDataMapWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<MetaDataMap[]>([]);
  const [metaDataMapFormData, setMetaDataMapFormData] = useState<{
    visiable: boolean;
    data: MetaDataMap;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<MetaDataMap>[] = [
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.srcDataSetTypeCode'}),
      dataIndex: 'srcDataSetTypeCode',
      width: 180
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.srcDataSetTypeName'}),
      dataIndex: 'srcDataSetTypeName',
      width: 180,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.srcDataSetCode'}),
      dataIndex: 'srcDataSetCode',
      width: 180,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.srcDataSetValue'}),
      dataIndex: 'srcDataSetValue',
      width: 180,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.tgtDataSetTypeCode'}),
      dataIndex: 'tgtDataSetTypeCode',
      width: 180
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.tgtDataSetTypeName'}),
      dataIndex: 'tgtDataSetTypeName',
      width: 180,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.tgtDataSetCode'}),
      dataIndex: 'tgtDataSetCode',
      width: 180,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataMap.tgtDataSetValue'}),
      dataIndex: 'tgtDataSetValue',
      width: 180,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.remark'}),
      dataIndex: 'remark',
      width: 180,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.updateTime'}),
      dataIndex: 'updateTime',
      hideInSearch: true,
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
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => setMetaDataMapFormData({visiable: true, data: record})}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.delete.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<DeleteOutlined/>}
                  onClick={() => {
                    Modal.confirm({
                      title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                      content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                      okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                      okButtonProps: {danger: true},
                      cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                      onOk() {
                        RefdataService.deleteDataMap(record).then((d) => {
                          if (d.success) {
                            message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
                            actionRef.current?.reload();
                          }
                        });
                      },
                    });
                  }}
                ></Button>
              </Tooltip>
            )}
          </Space>
        </>
      ),
    },
  ];

  return (
    <PageContainer title={false}>
      <ProTable<MetaDataMap>
        search={{
          labelWidth: 'auto',
          span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        scroll={{x: 1500}}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return RefdataService.listDataMap(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setMetaDataMapFormData({visiable: true, data: {}})}
              >
                {intl.formatMessage({id: 'app.common.operate.new.label'})}
              </Button>
            ),
            access.canAccess(PRIVILEGE_CODE.datadevResourceDelete) && (
              <Button
                key="del"
                type="default"
                disabled={selectedRows.length < 1}
                onClick={() => {
                  Modal.confirm({
                    title: intl.formatMessage({id: 'app.common.operate.delete.confirm.title'}),
                    content: intl.formatMessage({id: 'app.common.operate.delete.confirm.content'}),
                    okText: intl.formatMessage({id: 'app.common.operate.confirm.label'}),
                    okButtonProps: {danger: true},
                    cancelText: intl.formatMessage({id: 'app.common.operate.cancel.label'}),
                    onOk() {
                      RefdataService.deleteDataMapBatch(selectedRows).then((d) => {
                        if (d.success) {
                          message.success(intl.formatMessage({id: 'app.common.operate.delete.success'}));
                          actionRef.current?.reload();
                        }
                      });
                    },
                  });
                }}
              >
                {intl.formatMessage({id: 'app.common.operate.delete.label'})}
              </Button>
            ),
          ],
        }}
        pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
        rowSelection={{
          fixed: true,
          onChange: (selectedRowKeys, selectedRows, info) => setSelectedRows(selectedRows),
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
      />
      {metaDataMapFormData.visiable && (
        <RefDataMapForm
          visible={metaDataMapFormData.visiable}
          onCancel={() => setMetaDataMapFormData({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setMetaDataMapFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={metaDataMapFormData.data}
        />
      )}
    </PageContainer>
  );
}

export default RefDataMapWeb;
