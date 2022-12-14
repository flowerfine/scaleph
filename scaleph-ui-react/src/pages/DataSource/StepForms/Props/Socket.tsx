import {useIntl, useModel} from "umi";
import {Form} from "antd";
import {useEffect} from "react";
import {ProCard, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {DsCategoryService} from "@/services/datasource/category.service";

const SocketForm: React.FC = () => {
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
          name={"host"}
          label={intl.formatMessage({id: 'pages.dataSource.step.props.socket.host'})}
          rules={[{required: true}]}
          colProps={{span: 21, offset: 1}}
          initialValue={"127.0.0.1"}
        />
        <ProFormDigit
          name={"port"}
          label={intl.formatMessage({id: 'pages.dataSource.step.props.socket.port'})}
          rules={[{required: true}]}
          colProps={{span: 21, offset: 1}}
          initialValue={9999}
          fieldProps={{
            min: 0,
            max: 65535
          }}
        />
      </ProCard>
    </div>
  );
}

export default SocketForm;

