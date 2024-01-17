import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDependency, ProFormGroup, ProFormSelect, ProFormText,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {RedisParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/fields";

const SourceRedisStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <DataSourceItem dataSource={"Redis"}/>
          <ProFormText
            name={RedisParams.keys}
            label={intl.formatMessage({id: 'pages.project.di.step.redis.keys'})}
            rules={[{required: true}]}
          />
          <ProFormSelect
            name={"data_type"}
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
          <ProFormDependency name={['data_type']}>
            {({data_type}) => {
              if (data_type == 'hash') {
                return (
                  <ProFormSelect
                    name={RedisParams.hashKeyParseMode}
                    label={intl.formatMessage({id: 'pages.project.di.step.redis.hashKeyParseMode'})}
                    initialValue={"all"}
                    options={["all", "kv"]}
                    allowClear={false}
                  />
                );
              }
              return <></>;
            }}
          </ProFormDependency>
          <ProFormSelect
            name={'format'}
            label={intl.formatMessage({id: 'pages.project.di.step.redis.format'})}
            allowClear={false}
            initialValue={'json'}
            valueEnum={{
              json: 'json',
              text: 'text',
            }}
          />
          <ProFormDependency name={['format']}>
            {({format}) => {
              if (format == 'json') {
                return <FieldItem/>;
              }
              return <ProFormGroup/>;
            }}
          </ProFormDependency>
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceRedisStepForm;
