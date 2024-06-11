import {useIntl, useModel} from "@umijs/max";
import {Form, FormInstance} from "antd";
import {useEffect, useRef} from "react";
import {ProCard, ProFormColumnsType, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {DsCategoryService} from "@/services/datasource/category.service";
import {BetaSchemaForm} from "@ant-design/pro-form";

type DataItem = {
  name: string;
  state: string;
};

const HudiForm: React.FC = () => {
  const intl = useIntl();
  const formRef = useRef<FormInstance>();

  const {dsType} = useModel('dataSourceType', (model) => ({
    dsType: model.dsType
  }));

  useEffect(() => {
    formRef.current?.setFieldValue("dsTypeId", dsType?.id)
  }, [dsType])

  const columns: ProFormColumnsType<DataItem>[] =  [
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
        <BetaSchemaForm<DataItem>
          formRef={formRef}
          layoutType="Embed"
          grid={true}
          columns={columns}/>
      </ProCard>
    </div>
  );
}

export default HudiForm;

