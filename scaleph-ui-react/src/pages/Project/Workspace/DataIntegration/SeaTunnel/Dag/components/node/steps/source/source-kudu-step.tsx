import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormDigit, ProFormGroup, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {KuduParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";
import FieldItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";
import {StepSchemaService} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/helper";

const SourceKuduStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatSchema(values)
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
          name={KuduParams.tableName}
          label={intl.formatMessage({id: 'pages.project.di.step.kudu.tableName'})}
          rules={[{required: true}]}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.kudu.client'})}
          collapsible={true}
          defaultCollapsed={true}
        >
          <ProFormDigit
            name={KuduParams.clientWorkerCount}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.clientWorkerCount'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.kudu.clientWorkerCount.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            colProps={{span: 8}}
            fieldProps={{
              min: 1
            }}
          />
          <ProFormDigit
            name={KuduParams.clientDefaultOperationTimeoutMs}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.clientDefaultOperationTimeoutMs'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
          <ProFormDigit
            name={KuduParams.clientDefaultAdminOperationTimeoutMs}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.clientDefaultAdminOperationTimeoutMs'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
        </ProFormGroup>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.kudu.scan'})}
          collapsible={true}
          defaultCollapsed={true}
        >
          <ProFormDigit
            name={KuduParams.scanTokenQueryTimeout}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.scanTokenQueryTimeout'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
          <ProFormDigit
            name={KuduParams.scanTokenBatchSizeBytes}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.scanTokenBatchSizeBytes'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1024 * 1024
            }}
          />
          <ProFormDigit
            name={KuduParams.filter}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.filter'})}
            colProps={{span: 8}}
          />
        </ProFormGroup>
        <FieldItem/>
        <ProFormTextArea
          name={KuduParams.tableList}
          label={intl.formatMessage({id: 'pages.project.di.step.kudu.tableList'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kudu.tableList.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceKuduStepForm;
