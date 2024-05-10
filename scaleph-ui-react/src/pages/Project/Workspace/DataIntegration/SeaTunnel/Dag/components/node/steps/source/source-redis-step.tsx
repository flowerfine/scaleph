import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {RedisParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourceRedisStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatSchema(values);
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
        <DataSourceItem dataSource={"Redis"}/>
        <ProFormDigit
          name={RedisParams.dbNum}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.dbNum'})}
          initialValue={0}
          fieldProps={{
            min: 0
          }}
        />
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
      </DrawerForm>
    </XFlow>
  );
};

export default SourceRedisStepForm;
