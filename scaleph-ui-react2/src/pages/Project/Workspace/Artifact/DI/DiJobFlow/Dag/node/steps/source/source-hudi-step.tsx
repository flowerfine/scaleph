import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {
  ProForm,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {HudiParams, STEP_ATTR_TYPE} from '../constant';

const SourceHudiStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                values[HudiParams.useKerberos] = values.useKerberos;
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
          <ProFormText
            name={HudiParams.tablePath}
            label={intl.formatMessage({id: 'pages.project.di.step.hudi.tablePath'})}
            rules={[{required: true}]}
          />
          <ProFormSelect
            name={HudiParams.tableType}
            label={intl.formatMessage({id: 'pages.project.di.step.hudi.tableType'})}
            rules={[{required: true}]}
            initialValue={'Copy On Write'}
            valueEnum={{
              cow: {text: 'Copy On Write', disabled: false},
              mor: {text: 'Merge On Read', disabled: true},
            }}
          />
          <ProFormText
            name={HudiParams.confFiles}
            label={intl.formatMessage({id: 'pages.project.di.step.hudi.confFiles'})}
            rules={[{required: true}]}
          />
          <ProFormSwitch
            name={'useKerberos'}
            label={intl.formatMessage({id: 'pages.project.di.step.hudi.useKerberos'})}
          />
          <ProFormDependency name={['useKerberos']}>
            {({useKerberos}) => {
              if (useKerberos) {
                return (
                  <ProFormGroup>
                    <ProFormText
                      name={HudiParams.kerberosPrincipal}
                      label={intl.formatMessage({
                        id: 'pages.project.di.step.hudi.kerberosPrincipal',
                      })}
                      rules={[{required: true}]}
                    />
                    <ProFormText
                      name={HudiParams.kerberosPrincipalFile}
                      label={intl.formatMessage({
                        id: 'pages.project.di.step.hudi.kerberosPrincipalFile',
                      })}
                      rules={[{required: true}]}
                    />
                  </ProFormGroup>
                );
              }
              return <ProFormGroup/>;
            }}
          </ProFormDependency>
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceHudiStepForm;
