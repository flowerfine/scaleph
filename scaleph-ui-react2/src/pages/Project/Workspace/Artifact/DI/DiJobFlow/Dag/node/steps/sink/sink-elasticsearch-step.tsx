import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {ElasticsearchParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkElasticsearchStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <DataSourceItem dataSource={'Elasticsearch'}/>
          <ProFormText
            name={ElasticsearchParams.index}
            label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.index'})}
            rules={[{required: true}]}
          />
          <ProFormGroup
            title={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.primaryKeys'})}
          >
            <ProFormList
              name={ElasticsearchParams.primaryKeyArray}
              copyIconProps={false}
              creatorButtonProps={{
                creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.elasticsearch.primaryKeys.list'}),
                type: 'text',
              }}
            >
              <ProFormText name={ElasticsearchParams.primaryKey} colProps={{span: 16, offset: 4}}/>
            </ProFormList>
          </ProFormGroup>

          <ProFormDigit
            name={ElasticsearchParams.maxRetryCount}
            label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.maxRetryCount'})}
            initialValue={3}
            fieldProps={{
              min: 1,
            }}
          />
          <ProFormDigit
            name={ElasticsearchParams.maxBatchSize}
            label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.maxBatchSize'})}
            initialValue={10}
            fieldProps={{
              step: 1000,
              min: 0,
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkElasticsearchStepForm;
