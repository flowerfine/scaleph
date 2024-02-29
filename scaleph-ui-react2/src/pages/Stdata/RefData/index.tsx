import {history, useAccess, useIntl} from "umi";
import {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined, FolderOpenOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {MetaDataSetType} from "@/services/stdata/typings";
import {RefdataService} from "@/services/stdata/refdata.service";
import DataSetTypeForm from "@/pages/Stdata/RefData/components/DataSetTypeForm";

const RefData: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<MetaDataSetType[]>([]);
  const [metaDataSetTypeFormData, setMetaDataSetTypeFormData] = useState<{
    visiable: boolean;
    data: MetaDataSetType;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<MetaDataSetType>[] = [
    {
      title: intl.formatMessage({id: 'pages.stdata.dataSetType.dataSetTypeCode'}),
      dataIndex: 'dataSetTypeCode',
      width: 280
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataSetType.dataSetTypeName'}),
      dataIndex: 'dataSetTypeName',
      width: 280
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
              <Tooltip title={intl.formatMessage({id: 'pages.stdata.dataSet.open'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<FolderOpenOutlined/>}
                  onClick={() => history.push('/stdata/refdata/value', record)}
                ></Button>
              </Tooltip>
            )}
            {access.canAccess(PRIVILEGE_CODE.datadevProjectEdit) && (
              <Tooltip title={intl.formatMessage({id: 'app.common.operate.edit.label'})}>
                <Button
                  shape="default"
                  type="link"
                  icon={<EditOutlined/>}
                  onClick={() => setMetaDataSetTypeFormData({visiable: true, data: record})}
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
                        RefdataService.deleteDataSetType(record).then((d) => {
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
    <div>
      <ProTable<MetaDataSetType>
        search={{
          labelWidth: 'auto',
          span: {xs: 24, sm: 12, md: 8, lg: 6, xl: 6, xxl: 4},
        }}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        options={false}
        columns={tableColumns}
        request={(params, sorter, filter) => {
          return RefdataService.listDataSetType(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setMetaDataSetTypeFormData({visiable: true, data: {}})}
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
                      RefdataService.deleteDataSetTypeBatch(selectedRows).then((d) => {
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
      {metaDataSetTypeFormData.visiable && (
        <DataSetTypeForm
          visible={metaDataSetTypeFormData.visiable}
          onCancel={() => setMetaDataSetTypeFormData({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setMetaDataSetTypeFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={metaDataSetTypeFormData.data}
        />
      )}
    </div>
  );
}

export default RefData;
