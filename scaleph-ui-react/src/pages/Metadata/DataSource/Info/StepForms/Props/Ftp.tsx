import {useIntl} from "@umijs/max";
import {Form} from "antd";
import React, {useEffect} from "react";
import {ProCard, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {DsCategoryService} from "@/services/datasource/category.service";
import {DataSourceProps} from "@/services/datasource/typings";

const FtpForm: React.FC<DataSourceProps> = ({prefix, type}) => {
  const intl = useIntl();
  const form = Form.useFormInstance()

  useEffect(() => {
    form.setFieldValue("type", type?.type?.value)
    form.setFieldValue("dsTypeId", type?.id)
  }, [])

  return (
    <div>
      <ProCard
        headerBordered={true}
        style={{width: 1000}}>
        <ProFormText name="type" hidden/>
        <ProFormSelect
          name="dsTypeId"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.type'})}
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
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.version'})}
          colProps={{span: 21, offset: 1}}
        />
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.name'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({id: 'app.common.data.remark'})}
          colProps={{span: 21, offset: 1}}
          fieldProps={{
            rows: 5
          }}
        />
        <ProFormText
          name={[prefix, "host"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.host'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"localhost"}
        />
        <ProFormDigit
          name={[prefix, "port"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.port'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={21}
          fieldProps={{
            min: 0,
            max: 65535
          }}
        />
        <ProFormText
          name={[prefix, "username"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.username'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={[prefix, "password"]}
          label={intl.formatMessage({id: 'pages.metadata.dataSource.step.props.ftp.password'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
        />
      </ProCard>
    </div>
  );
}

export default FtpForm;

