import {useIntl} from 'umi';
import {useRef} from 'react';
import {Col, Row} from 'antd';
import {ActionType, ProColumns, ProFormInstance, ProTable} from '@ant-design/pro-components';
import {DictDataService} from '@/services/admin/dictData.service';
import {DictTypeService} from '@/services/admin/dictType.service';
import {SysDictData, SysDictType} from '@/services/admin/typings';

const Dict: React.FC = () => {
  const intl = useIntl();
  const dictTypeActionRef = useRef<ActionType>();
  const dictDataActionRef = useRef<ActionType>();
  const dictTypeFormRef = useRef<ProFormInstance>();
  const dictDataFormRef = useRef<ProFormInstance>();

  const dictTypeTableColumns: ProColumns<SysDictType>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.dict.code'}),
      dataIndex: 'code',
      width: 180,
      fixed: 'left',
    },
    {
      title: intl.formatMessage({id: 'pages.admin.dict.name'}),
      dataIndex: 'name',
      width: 300,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      width: 150,
      hideInSearch: true,
    }
  ];

  const dictDataTableColumns: ProColumns<SysDictData>[] = [
    {
      title: intl.formatMessage({id: 'pages.admin.dict.dictType'}),
      dataIndex: 'dictType',
      width: 240,
      fixed: 'left',
      render: (_, record) => {
        return <span>{record.dictType?.code + '-' + record.dictType?.name}</span>;
      },
    },
    {
      title: intl.formatMessage({id: 'pages.admin.dict.value'}),
      dataIndex: 'value',
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'pages.admin.dict.label'}),
      dataIndex: 'label',
      width: 180,
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      width: 260,
      hideInSearch: true,
    }
  ];

  return (
    <div>
      <Row gutter={[12, 12]}>
        <Col span={8}>
          <ProTable<SysDictType>
            headerTitle={intl.formatMessage({id: 'pages.admin.dict.dictType'})}
            search={{filterType: 'light'}}
            rowKey="code"
            actionRef={dictTypeActionRef}
            formRef={dictTypeFormRef}
            options={false}
            columns={dictTypeTableColumns}
            request={(params, sorter, filter) => {
              return DictTypeService.listDictTypeByPage(params);
            }}
            pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
            onRow={(record) => {
              return {
                onClick: (event) => {
                  dictDataFormRef.current?.setFieldsValue({
                    dictType: record.code,
                  });
                  dictDataFormRef.current?.submit();
                },
              };
            }}
            tableAlertRender={false}
            tableAlertOptionRender={false}
          />
        </Col>
        <Col span={16}>
          <ProTable<SysDictData>
            headerTitle={intl.formatMessage({id: 'pages.admin.dict.dictData'})}
            search={{filterType: 'light'}}
            rowKey="key"
            actionRef={dictDataActionRef}
            formRef={dictDataFormRef}
            options={false}
            columns={dictDataTableColumns}
            request={(params, sorter, filter) => {
              return DictDataService.listDictDataByPage(params);
            }}
            pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
            tableAlertRender={false}
            tableAlertOptionRender={false}
          />
        </Col>
      </Row>
    </div>
  );
};

export default Dict;
