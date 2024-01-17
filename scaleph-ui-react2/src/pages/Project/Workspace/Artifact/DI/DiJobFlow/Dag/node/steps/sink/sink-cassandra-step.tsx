import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {CassandraParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkCassandraStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <DataSourceItem dataSource={'Cassandra'}/>
          <ProFormSelect
            name={CassandraParams.consistencyLevel}
            label={intl.formatMessage({id: 'pages.project.di.step.cassandra.consistencyLevel'})}
            allowClear={false}
            initialValue={'LOCAL_ONE'}
            options={[
              'ANY',
              'ONE',
              'TWO',
              'THREE',
              'QUORUM',
              'ALL',
              'LOCAL_ONE',
              'LOCAL_QUORUM',
              'EACH_QUORUM',
              'SERIAL',
              'LOCAL_SERIAL',
            ]}
          />
          <ProFormText
            name={CassandraParams.table}
            label={intl.formatMessage({id: 'pages.project.di.step.cassandra.table'})}
            rules={[{required: true}]}
          />
          <ProFormSelect
            name={CassandraParams.batchType}
            label={intl.formatMessage({id: 'pages.project.di.step.cassandra.batchType'})}
            allowClear={false}
            initialValue={'UNLOGGED'}
            options={['LOGGED', 'UNLOGGED', 'COUNTER']}
          />
          <ProFormDigit
            name={CassandraParams.batchSize}
            label={intl.formatMessage({id: 'pages.project.di.step.cassandra.batchSize'})}
            initialValue={5000}
            fieldProps={{
              step: 1000,
              min: 0,
            }}
          />
          <ProFormSwitch
            name={CassandraParams.asyncWrite}
            label={intl.formatMessage({id: 'pages.project.di.step.cassandra.asyncWrite'})}
            initialValue={true}
          />

          <ProFormGroup label={intl.formatMessage({id: 'pages.project.di.step.cassandra.fields'})}>
            <ProFormList
              name={CassandraParams.fieldArray}
              copyIconProps={false}
              creatorButtonProps={{
                creatorButtonText: intl.formatMessage({
                  id: 'pages.project.di.step.cassandra.fields.field',
                }),
                type: 'text',
              }}
            >
              <ProFormText name={CassandraParams.field}/>
            </ProFormList>
          </ProFormGroup>
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkCassandraStepForm;
