import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {StarRocksParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";
import {InfoCircleOutlined} from "@ant-design/icons";

const SinkStarRocksStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatStarRocksConfig(values);
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
            colProps={{span: 24}}
          />
          <DataSourceItem dataSource={'StarRocks'}/>
          <ProFormText
            name={StarRocksParams.baseUrl}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.base-url'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={StarRocksParams.database}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.database'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={StarRocksParams.table}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.table'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={StarRocksParams.labelPrefix}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.labelPrefix'})}
          />
          <ProFormDigit
            name={StarRocksParams.batchMaxRows}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchMaxRows'})}
            colProps={{span: 8}}
            initialValue={1024}
            fieldProps={{
              step: 1000,
              min: 1,
            }}
          />
          <ProFormDigit
            name={StarRocksParams.batchMaxBytes}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchMaxBytes'})}
            colProps={{span: 8}}
            initialValue={5 * 1024 * 1024}
            fieldProps={{
              step: 1024 * 1024,
              min: 1,
            }}
          />
          <ProFormDigit
            name={StarRocksParams.batchIntervalMs}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchIntervalMs'})}
            colProps={{span: 8}}
            initialValue={1000}
            fieldProps={{
              step: 1000,
              min: 1,
            }}
          />
          <ProFormDigit
            name={StarRocksParams.maxRetries}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetries'})}
            colProps={{span: 8}}
            initialValue={1}
            fieldProps={{
              step: 1,
              min: 1,
            }}
          />
          <ProFormDigit
            name={StarRocksParams.retryBackoffMultiplierMs}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.retryBackoffMultiplierMs'})}
            colProps={{span: 8}}
            fieldProps={{
              step: 1000,
              min: 1,
            }}
          />
          <ProFormDigit
            name={StarRocksParams.maxRetryBackoffMs}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetryBackoffMs'})}
            colProps={{span: 8}}
            fieldProps={{
              step: 1000,
              min: 1,
            }}
          />
          <ProFormSwitch
            name={StarRocksParams.enableUpsertDelete}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.enableUpsertDelete'})}
          />
          <ProFormTextArea
            name={StarRocksParams.saveModeCreateTemplate}
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.saveModeCreateTemplate'})}
          />

          <ProFormGroup
            label={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfig'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfig.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
          >
            <ProFormList
              name={StarRocksParams.starrocksConfigMap}
              copyIconProps={false}
              creatorButtonProps={{
                creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfig.list'}),
                type: 'text',
              }}
            >
              <ProFormGroup>
                <ProFormText
                  name={StarRocksParams.starrocksConfigKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigKey'})}
                  placeholder={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigKey.placeholder'})}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name={StarRocksParams.starrocksConfigValue}
                  label={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigValue'})}
                  placeholder={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfigValue.placeholder'})}
                  colProps={{span: 10, offset: 1}}
                />
              </ProFormGroup>
            </ProFormList>
          </ProFormGroup>
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkStarRocksStepForm;
