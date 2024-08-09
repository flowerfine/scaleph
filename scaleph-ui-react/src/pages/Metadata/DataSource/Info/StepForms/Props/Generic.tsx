import {useEffect, useRef} from "react";
import {FormInstance} from "antd";
import {BetaSchemaForm} from "@ant-design/pro-form";
import {ProCard, ProFormColumnsType} from "@ant-design/pro-components";
import {useIntl, useModel} from "@umijs/max";
import {DsCategoryService} from "@/services/datasource/category.service";
import {DataSourceProps} from "@/services/datasource/typings";

const GenericForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();
  const formRef = useRef<FormInstance>();

  const {dsType} = useModel('dataSourceType', (model) => ({
    dsType: model.dsType
  }));

  useEffect(() => {
    formRef.current?.setFieldValue("type", type?.type?.value)
    formRef.current?.setFieldValue("dsTypeId", type?.id)
  }, [dsType])

  const columns: ProFormColumnsType[] = [
    {
      dataIndex: [prefix, 'type'],
      valueType: 'text',
      hideInForm: true
    },
    {
      title: intl.formatMessage({id: 'pages.metadata.dataSource.step.props.type'}),
      dataIndex: 'dsTypeId',
      colProps: {span: 21, offset: 1},
      valueType: 'select',
      fieldProps: {
        disabled: true
      },
      request: () => {
        return DsCategoryService.listTypes({}).then((response) => {
          if (response.data) {
            return response.data.map((item) => {
              return {label: item.type.label, value: item.id, item: item};
            });
          }
          return []
        })
      }
    },
    {
      title: intl.formatMessage({id: 'pages.metadata.dataSource.step.props.version'}),
      dataIndex: 'version',
      colProps: {span: 21, offset: 1},
      valueType: 'text',
    },
    {
      title: intl.formatMessage({id: 'pages.metadata.dataSource.step.props.name'}),
      dataIndex: 'name',
      colProps: {span: 21, offset: 1},
      valueType: 'text',
      formItemProps: {
        rules: [{required: true}]
      },
    },
    {
      title: intl.formatMessage({id: 'app.common.data.remark'}),
      dataIndex: 'remark',
      colProps: {span: 21, offset: 1},
      valueType: 'textarea',
      fieldProps: {
        rows: 5
      },
    },
  ]

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <BetaSchemaForm
          formRef={formRef}
          layoutType="Embed"
          grid={true}
          columns={columns}/>
      </ProCard>
    </div>
  );
}

export default GenericForm;

