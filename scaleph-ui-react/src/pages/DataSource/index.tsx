import {useAccess, useIntl} from "umi";
import {useEffect, useRef, useState} from "react";
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {DsInfo, DsType} from "@/services/datasource/typings";
import {DsCategoryService} from "@/services/datasource/category.service";
import {DsInfoService} from "@/services/datasource/info.service";
import {Select} from "antd";

const DataSourceCategoryAndTypeWeb: React.FC = () => {
  const intl = useIntl();
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
      dataIndex: 'name',
      width: 160,
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.info.type'}),
      dataIndex: 'dsTypeId',
      renderFormItem: (item, { defaultRender, ...rest }, form) => {
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
                <Select.Option key={item.id} value={item.id}>
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
      hideInSearch: true
    },
    {
      title: intl.formatMessage({id: 'pages.dataSource.remark'}),
      dataIndex: 'remark',
      hideInSearch: true
    },
    {
      title: intl.formatMessage({ id: 'pages.dataSource.createTime' }),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.dataSource.updateTime' }),
      dataIndex: 'updateTime',
      hideInSearch: true,
      width: 180,
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
      pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
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

export default DataSourceCategoryAndTypeWeb;
