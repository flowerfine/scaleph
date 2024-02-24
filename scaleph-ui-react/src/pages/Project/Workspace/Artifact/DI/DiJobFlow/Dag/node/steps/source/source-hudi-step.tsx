import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
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

const SourceHudiStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <DrawerForm
        title={data.data.label}
        form={form}
        initialValues={data.data.attrs}
        open={visible}
        onOpenChange={onVisibleChange}
        grid={true}
        width={780}
        drawerProps={{
          styles: {body: {overflowY: 'scroll'}},
          closeIcon: null,
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            values[HudiParams.useKerberos] = values.useKerberos;
            onOK(values)
            return Promise.resolve(true)
          }
          return Promise.resolve(false)
        }}
      >
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
      </DrawerForm>
    </XFlow>
  );
};

export default SourceHudiStepForm;
