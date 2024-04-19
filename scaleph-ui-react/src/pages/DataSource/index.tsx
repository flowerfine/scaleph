import {history, useAccess, useIntl} from "@umijs/max";
import {useRef, useState} from "react";
import {Button, Image, message, Modal, Space, Tooltip} from "antd";
import {DeleteOutlined} from "@ant-design/icons";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {PRIVILEGE_CODE} from "@/constants/privilegeCode";
import {DICT_TYPE} from "@/constants/dictType";
import {DsInfo} from "@/services/datasource/typings";
import {DsInfoService} from "@/services/datasource/info.service";
import {DictDataService} from "@/services/admin/dictData.service";

const DataSourceListWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [selectedRows, setSelectedRows] = useState<DsInfo[]>([]);

  const tableColumns: ProColumns<DsInfo>[] = [
    {
      title: intl.formatMessage({id: 'pages.dataSource.info.name'}),
      dataIndex: 'name'
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.type.logo'}),
      dataIndex: 'logo',
      width: 160,
      hideInSearch: true,
      render: (dom, entity, index, action, schema) => {
        return <Image
          alt={entity.dsType?.type.label}
          preview={false} src={entity.dsType?.logo}
          wrapperStyle={{
            width: '60%',
            height: '60%'
          }}
        ></Image>
      }
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.info.type'}),
      dataIndex: 'dsType',
      width: 160,
      render: (dom, entity, index, action, schema) => {
        return entity.dsType?.type.label
      },
      request: (params, props) => {
        return DictDataService.listDictDataByType2(DICT_TYPE.datasourceType)
      }
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
