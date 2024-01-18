import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {HttpParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkHttpStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatHeader(values);
                StepSchemaService.formatParam(values);
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
          <DataSourceItem dataSource={'Http'}/>
          <ProFormList
            name={HttpParams.headerArray}
            label={intl.formatMessage({id: 'pages.project.di.step.http.headers'})}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.http.headers'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={HttpParams.header}
                label={intl.formatMessage({id: 'pages.project.di.step.http.header'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={HttpParams.headerValue}
                label={intl.formatMessage({id: 'pages.project.di.step.http.value'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
          <ProFormList
            name={HttpParams.paramArray}
            label={intl.formatMessage({id: 'pages.project.di.step.http.params'})}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.http.params'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={HttpParams.param}
                label={intl.formatMessage({id: 'pages.project.di.step.http.param'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={HttpParams.paramValue}
                label={intl.formatMessage({id: 'pages.project.di.step.http.value'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
          <ProFormDigit
            name={HttpParams.retry}
            label={intl.formatMessage({id: 'pages.project.di.step.http.retry'})}
            colProps={{span: 6}}
            initialValue={3}
            fieldProps={{
              step: 1,
              min: 1
            }}
          />
          <ProFormDigit
            name={HttpParams.retryBackoffMultiplierMs}
            label={intl.formatMessage({id: 'pages.project.di.step.http.retryBackoffMultiplierMs'})}
            colProps={{span: 9}}
            initialValue={100}
            fieldProps={{
              step: 1000,
              min: 0
            }}
          />
          <ProFormDigit
            name={HttpParams.retryBackoffMaxMs}
            label={intl.formatMessage({id: 'pages.project.di.step.http.retryBackoffMaxMs'})}
            colProps={{span: 9}}
            initialValue={10000}
            fieldProps={{
              step: 1000,
              min: 0
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkHttpStepForm;