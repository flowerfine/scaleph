import React from "react";
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormText} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {ModalFormProps} from '@/typings';
import {DsInfo} from "@/services/datasource/typings";
import {DsInfoService} from "@/services/datasource/info.service";
import DataSourceForm from "@/pages/Metadata/DataSource/Info/StepForms/Props/DataSourceForm";

const DataSourceUpdateForm: React.FC<ModalFormProps<DsInfo>> = ({data, visible, onOK, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        intl.formatMessage({id: 'app.common.operate.edit.label'}) +
        intl.formatMessage({id: 'pages.metadata.dataSource.info'})
      }
      width={1100}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          DsInfoService.update(data.id, {...values}).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
              if (onOK) {
                onOK(values);
              }
            }
          })
        });
      }}
    >
      <ProForm
        form={form}
        layout="horizontal"
        submitter={false}
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
        rowProps={{gutter: [16, 8]}}
        grid={true}
        initialValues={data}
      >
        <ProFormText name="id" hidden/>
        <DataSourceForm prefix={"props"} type={data.dsType}/>
      </ProForm>
    </Modal>
  );
};

export default DataSourceUpdateForm;
