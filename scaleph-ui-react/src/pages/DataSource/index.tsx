import {history, useAccess, useIntl} from "umi";
import {useEffect, useRef, useState} from "react";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {DsInfo, DsType} from "@/services/datasource/typings";
import {DsCategoryService} from "@/services/datasource/category.service";
import {DsInfoService} from "@/services/datasource/info.service";
import {Button, message, Modal, Select, Space, Tooltip} from "antd";
import {PRIVILEGE_CODE} from "@/constant";
import {DeleteOutlined} from "@ant-design/icons";

const DataSourceListWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<DsInfo[]>([]);
  const [dsTypes, setDsTypes] = useState<DsType[]>([]);

  useEffect(() => {
    DsCategoryService.listTypes({}).then((response) => {
      if (response.data) {
        setDsTypes(response.data)
      }
    })
  }, []);

  const tableColumns: ProColumns<DsInfo>[] = [
    {
      title: intl.formatMessage({id: 'pages.dataSource.info.name'}),
      dataIndex: 'name'
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.info.type'}),
      dataIndex: 'dsType',
      width: 160,
      render: (dom, entity, index, action, schema) => {
        return entity.dsType.type.label
      },
      renderFormItem: (item, {defaultRender, ...rest}, form) => {
        return (
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {dsTypes.map((item) => {
              return (
                <Select.Option key={item.type.value} value={item.type.value}>
                  {item.type.label}
                </Select.Option>
              );
            })}
          </Select>
        );
      },
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.info.version'}),
      dataIndex: 'version',
      width: 160,
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.remark'}),
      dataIndex: 'remark',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.createTime'}),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.updateTime'}),
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
                        DsInfoService.deleteOne(record).then((d) => {
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
  ]

  return (
    <ProTable<DsInfo>
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
        return DsInfoService.list(params);
      }}
      toolbar={{
        actions: [
          access.canAccess(PRIVILEGE_CODE.datadevClusterAdd) && (
            <Button
              key="new"
              type="primary"
              onClick={() => history.push("/dataSource/stepForms")}
            >
              {intl.formatMessage({id: 'app.common.operate.new.label'})}
            </Button>
          ),
          access.canAccess(PRIVILEGE_CODE.datadevClusterDelete) && (
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
                    DsInfoService.deleteBatch(selectedRows).then((d) => {
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
        onChange(selectedRowKeys, selectedRows, info) {
          setSelectedRows(selectedRows);
        },
      }}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  );
}

export default DataSourceListWeb;
