import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {DorisParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkDorisStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatDorisConfig(values);
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
          colProps={{span: 24}}
        />
        <DataSourceItem dataSource={'Doris'}/>
        <ProFormText
          name={DorisParams.tableIdentifier}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.tableIdentifier'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={DorisParams.sinkLabelPrefix}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkLabelPrefix'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={DorisParams.sinkEnable2PC}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkEnable2PC'})}
          initialValue={true}
        />
        <ProFormSwitch
          name={DorisParams.sinkEnableDelete}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkEnableDelete'})}
          initialValue={false}
        />
        <ProFormDigit
          name={DorisParams.sinkCheckInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkCheckInterval'})}
          colProps={{span: 12}}
          initialValue={10000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkMaxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkMaxRetries'})}
          colProps={{span: 12}}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkBufferSize'})}
          colProps={{span: 12}}
          initialValue={256 * 1024}
          fieldProps={{
            step: 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkBufferCount}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkBufferCount'})}
          colProps={{span: 12}}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig'})}
        >
          <ProFormList
            name={DorisParams.dorisConfigArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig.list'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={DorisParams.dorisConfigProperty}
                label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig.key'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig.key.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={DorisParams.dorisConfigValue}
                label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig.value'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig.value.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkDorisStepForm;
