import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {RedisParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkRedisStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatSchema(values);
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
          <DataSourceItem dataSource={'Redis'}/>
          <ProFormText
            name={RedisParams.key}
            label={intl.formatMessage({id: 'pages.project.di.step.redis.key'})}
            rules={[{required: true}]}
          />
          <ProFormDigit
            name={RedisParams.expire}
            label={intl.formatMessage({id: 'pages.project.di.step.redis.expire'})}
            initialValue={-1}
            fieldProps={{
              min: -1
            }}
          />
          <ProFormSelect
            name={RedisParams.dataType}
            label={intl.formatMessage({id: 'pages.project.di.step.redis.dataType'})}
            rules={[{required: true}]}
            valueEnum={{
              key: 'key',
              hash: 'hash',
              listDataSetType: 'list',
              set: 'set',
              zset: 'zset',
            }}
          />
          <ProFormSelect
            name={RedisParams.format}
            label={intl.formatMessage({id: 'pages.project.di.step.redis.format'})}
            allowClear={false}
            initialValue={'json'}
            valueEnum={{
              json: 'json',
              text: 'text',
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkRedisStepForm;
