import {useAccess, useIntl, useLocation, history} from "umi";
import {useRef, useState} from "react";
import {Button, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {MetaDataSet, MetaDataSetType} from "@/services/stdata/typings";
import {RefdataService} from "@/services/stdata/refdata.service";
import DataSetForm from "@/pages/Stdata/RefData/Value/DataSetForm";

const RefDataSetValue: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<MetaDataSet[]>([]);
  const [metaDataSetFormData, setMetaDataSetFormData] = useState<{
    visiable: boolean;
    data: MetaDataSet;
  }>({visiable: false, data: {}});

  const dataSetType = urlParams.state as MetaDataSetType;

  const tableColumns: ProColumns<MetaDataSet>[] = [
    {
      title: intl.formatMessage({id: 'pages.stdata.dataSet.dataSetType'}),
      dataIndex: 'dataSetType',
      width: 280,
      render: (text, record, index) => {
        return record.dataSetType?.dataSetTypeCode + '-' + record.dataSetType?.dataSetTypeName
      },
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataSet.dataSetCode'}),
      dataIndex: 'dataSetCode',
      width: 280
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataSet.dataSetValue'}),
      dataIndex: 'dataSetValue',
      width: 280
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataSet.system'}),
      dataIndex: 'system',
      width: 280,
      render: (text, record, index) => {
        return record.system?.systemName
      },
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.stdata.dataSet.isStandard'}),
      dataIndex: 'isStandard',
      width: 100,
      render: (text, record, index) => {
        return record.isStandard?.label
      },
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
                  onClick={() => setMetaDataSetFormData({visiable: true, data: record})}
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
                        RefdataService.deleteDataSet(record).then((d) => {
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
      <ProTable<MetaDataSet>
        headerTitle={
          <Button key="return" type="default" onClick={() => history.back()}>
            {intl.formatMessage({id: 'app.common.operate.return.label'})}
          </Button>
        }
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
          return RefdataService.listDataSet({...params, dataSetTypeCode: dataSetType.dataSetTypeCode});
        }}
        toolbar={{
          actions: [
            access.canAccess(PRIVILEGE_CODE.datadevResourceAdd) && (
              <Button
                key="new"
                type="primary"
                onClick={() => setMetaDataSetFormData({visiable: true, data: {}})}
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
                      RefdataService.deleteDataSetBatch(selectedRows).then((d) => {
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
      {metaDataSetFormData.visiable && (
        <DataSetForm
          visible={metaDataSetFormData.visiable}
          onCancel={() => setMetaDataSetFormData({visiable: false, data: {}})}
          onVisibleChange={(visiable) => {
            setMetaDataSetFormData({visiable: visiable, data: {}});
            actionRef.current?.reload();
          }}
          data={metaDataSetFormData.data}
        />
      )}
    </div>
  );
}

export default RefDataSetValue;
