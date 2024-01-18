import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormText,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {PaimonParams, STEP_ATTR_TYPE} from '../constant';

const SourcePaimonStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <ProFormText
            name={PaimonParams.warehouse}
            label={intl.formatMessage({id: 'pages.project.di.step.paimon.warehouse'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={PaimonParams.database}
            label={intl.formatMessage({id: 'pages.project.di.step.paimon.database'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={PaimonParams.table}
            label={intl.formatMessage({id: 'pages.project.di.step.paimon.table'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={PaimonParams.hdfsSitePath}
            label={intl.formatMessage({id: 'pages.project.di.step.paimon.hdfsSitePath'})}
            placeholder={intl.formatMessage({id: 'pages.project.di.step.paimon.hdfsSitePath.placeholder'})}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourcePaimonStepForm;