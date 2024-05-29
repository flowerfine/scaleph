import {useAccess, useIntl} from "@umijs/max";
import {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import {ActionType, PageContainer, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {MetaDataElement} from "@/services/stdata/typings";
import {MetaDataElementService} from "@/services/stdata/data-element.service";
import MetaDataElementForm from "@/pages/Stdata/DataElement/components/DataElementForm";

const DataElementWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<MetaDataElement[]>([]);
  const [metaDataElementFormData, setMetaDataElementFormData] = useState<{
    visiable: boolean;
    data: MetaDataElement;
  }>({visiable: false, data: {}});

  const tableColumns: ProColumns<MetaDataElement>[] = [
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.elementCode'}),
      dataIndex: 'elementCode',
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.elementName'}),
      dataIndex: 'elementName',
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.dataType'}),
      dataIndex: 'dataType',
      hideInSearch: true,
      width: 100,
      render: (dom, entity, index, actions, schema) => {
        return entity.dataType.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.dataLength'}),
      dataIndex: 'dataLength',
      hideInSearch: true,
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.dataPrecision'}),
      dataIndex: 'dataPrecision',
      hideInSearch: true,
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.dataScale'}),
      dataIndex: 'dataScale',
      hideInSearch: true,
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.nullable'}),
      dataIndex: 'nullable',
      hideInSearch: true,
      width: 100,
      render: (dom, entity, index, actions, schema) => {
        return entity.nullable?.label
      }
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.dataDefault'}),
      dataIndex: 'dataDefault',
      hideInSearch: true,
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.lowValue'}),
      dataIndex: 'lowValue',
      hideInSearch: true,
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.highValue'}),
      dataIndex: 'highValue',
      hideInSearch: true,
      width: 100
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataElement.dataSetType'}),
      dataIndex: 'dataSetType',
      hideInSearch: true,
      width: 180,
      render: (dom, entity, index, actions, schema) => {
        if (entity.dataSetType) {
          return entity.dataSetType?.dataSetTypeCode + '-' + entity.dataSetType?.dataSetTypeName
        }
        return null
      }
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
                  onClick={() => setMetaDataElementFormData({visiable: true, data: record})}
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
                        MetaDataElementService.deleteOne(record).then((d) => {
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
      <ProTable<MetaDataElement>
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
          return MetaDataElementService.list(params);
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setMetaDataElementFormData({visiable: true, data: {}})}
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
                      MetaDataElementService.deleteBatch(selectedRows).then((d) => {
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
      {metaDataElementFormData.visiable && (
        <MetaDataElementForm
          visible={metaDataElementFormData.visiable}
          onCancel={() => setMetaDataElementFormData({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setMetaDataElementFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={metaDataElementFormData.data}
        />
      )}
    </PageContainer>);
}

export default DataElementWeb;
