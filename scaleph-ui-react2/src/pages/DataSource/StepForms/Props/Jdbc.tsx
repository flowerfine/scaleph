import {
  ProCard,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {useIntl, useModel} from "umi";
import {DsCategoryService} from "@/services/datasource/category.service";
import {useEffect} from "react";
import {Form} from "antd";

const JdbcForm: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance()

  const {dsType} = useModel('dataSourceType', (model) => ({
    dsType: model.dsType
  }));

  useEffect(() => {
    form.setFieldValue("dsTypeId", dsType?.id)
  }, [dsType])

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <ProFormSelect
          name="dsTypeId"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.type'})}
          colProps={{span: 21, offset: 1}}
          disabled
          showSearch={false}
          request={() => {
            return DsCategoryService.listTypes({}).then((response) => {
              if (response.data) {
                return response.data.map((item) => {
                  return {label: item.type.label, value: item.id, item: item};
                });
              }
              return []
            })
          }}
        />
        <ProFormText
          name="version"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.version'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.name'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({id: 'pages.dataSource.remark'})}
          colProps={{span: 21, offset: 1}}
          fieldProps={{
            rows: 5
          }}
        />
        <ProFormText
          name="driverClassName"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.driverClassName'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name="url"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.url'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name="user"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.user'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name="password"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.password'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormList
          name="additionalProps"
          label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.additionalProps'})}
          copyIconProps={false}
          colProps={{span: 21, offset: 1}}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.additionalProps.name'}),
            type: "text",
          }}
        >
          <ProFormGroup>
            <ProFormText name="property"
                         label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.additionalProps.property'})}
                         colProps={{span: 10, offset: 1}}/>
            <ProFormText name="value"
                         label={intl.formatMessage({id: 'pages.dataSource.step.props.jdbc.additionalProps.value'})}
                         colProps={{span: 10, offset: 1}}/>
          </ProFormGroup>
        </ProFormList>
      </ProCard>
    </div>
  );
}

export default JdbcForm;
