import {useRef} from "react";
import {useAccess, useIntl} from '@umijs/max';
import {ActionType, ProColumns, ProFormInstance, ProTable} from "@ant-design/pro-components";
import {GenericResource} from "@/services/kubernetes/typings";
import {OamDefinitionService} from "@/services/oam/OamDefinitionService";

const OamComponentDefinitionWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const tableColumns: ProColumns<GenericResource>[] = [
    {
      title: intl.formatMessage({id: 'pages.oam.definition.name'}),
      dataIndex: 'name',
      render: (dom, entity, index, action, schema) => {
        return entity.metadata?.annotations?.name;
      }
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      render: (dom, entity, index, action, schema) => {
        return entity.metadata?.annotations?.description;
      }
    }
  ]

  return (
    <ProTable<GenericResource>
      search={false}
      rowKey="id"
      actionRef={actionRef}
      formRef={formRef}
      options={false}
      columns={tableColumns}
      request={(params, sorter, filter) => {
        return OamDefinitionService.listComponents(params);
      }}
      pagination={{showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10}}
      tableAlertRender={false}
      tableAlertOptionRender={false}
    />
  );
};

export default OamComponentDefinitionWeb;
