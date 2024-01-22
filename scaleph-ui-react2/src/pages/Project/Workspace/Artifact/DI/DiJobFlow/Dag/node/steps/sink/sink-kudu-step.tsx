import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {KuduParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkKuduStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
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
        <DataSourceItem dataSource={'Kudu'}/>
        <ProFormText
          name={KuduParams.kuduTable}
          label={intl.formatMessage({id: 'pages.project.di.step.kudu.table'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={KuduParams.saveMode}
          label={intl.formatMessage({id: 'pages.project.di.step.kudu.savemode'})}
          rules={[{required: true}]}
          allowClear={false}
          initialValue={'append'}
          valueEnum={{
            append: {text: 'append', disabled: false},
            overwrite: {text: 'overwrite', disabled: true},
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkKuduStepForm;
