import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {OpenMLDBParams, STEP_ATTR_TYPE} from '../constant';

const SourceOpenMLDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <Drawer
        open={visible}
        title={data.data.label}
        width={780}
        bodyStyle={{overflowY: 'scroll'}}
        destroyOnClose={true}
        onClose={onCancel}
        extra={
          <Button
            type="primary"
            onClick={() => {
              form.validateFields().then((values) => {
                if (onOK) {
                  onOK(values);
                }
              });
            }}
          >
            {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
          </Button>
        }
      >
        <ProForm form={form} initialValues={data.data.attrs} grid={true} submitter={false}>
          <ProFormText
            name={STEP_ATTR_TYPE.stepTitle}
            label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
            rules={[{required: true}, {max: 120}]}
          />
          <ProFormSwitch
            name={"cluster_mode"}
            label={intl.formatMessage({id: 'pages.project.di.step.openmldb.clusterMode'})}
            initialValue={false}
          />
          <ProFormDependency name={['cluster_mode']}>
            {({cluster_mode}) => {
              if (cluster_mode) {
                return (
                  <ProFormGroup>
                    <ProFormText
                      name={OpenMLDBParams.zkHost}
                      label={intl.formatMessage({id: 'pages.project.di.step.openmldb.zkHost'})}
                      colProps={{span: 12}}
                    />
                    <ProFormText
                      name={OpenMLDBParams.zkPath}
                      label={intl.formatMessage({id: 'pages.project.di.step.openmldb.zkPath'})}
                      colProps={{span: 12}}
                    />
                  </ProFormGroup>
                );
              }
              return <ProFormGroup>
                <ProFormText
                  name={OpenMLDBParams.host}
                  label={intl.formatMessage({id: 'pages.project.di.step.openmldb.host'})}
                  colProps={{span: 12}}
                />
                <ProFormDigit
                  name={OpenMLDBParams.port}
                  label={intl.formatMessage({id: 'pages.project.di.step.openmldb.port'})}
                  colProps={{span: 12}}
                  fieldProps={{
                    min: 0,
                    max: 65535
                  }}
                />
              </ProFormGroup>;
            }}
          </ProFormDependency>
          <ProFormText
            name={OpenMLDBParams.database}
            label={intl.formatMessage({id: 'pages.project.di.step.openmldb.database'})}
            rules={[{required: true}]}
          />
          <ProFormTextArea
            name={OpenMLDBParams.sql}
            label={intl.formatMessage({id: 'pages.project.di.step.openmldb.sql'})}
            rules={[{required: true}]}
          />
          <ProFormDigit
            name={OpenMLDBParams.sessionTimeout}
            label={intl.formatMessage({id: 'pages.project.di.step.openmldb.sessionTimeout'})}
            colProps={{span: 12}}
            initialValue={60000}
            fieldProps={{
              step: 1000,
              min: 1
            }}
          />
          <ProFormDigit
            name={OpenMLDBParams.requestTimeout}
            label={intl.formatMessage({id: 'pages.project.di.step.openmldb.requestTimeout'})}
            colProps={{span: 12}}
            initialValue={10000}
            fieldProps={{
              step: 1000,
              min: 1
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceOpenMLDBStepForm;
