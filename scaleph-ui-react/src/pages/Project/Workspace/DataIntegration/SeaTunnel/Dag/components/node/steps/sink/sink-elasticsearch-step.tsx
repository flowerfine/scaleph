import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormGroup, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {ElasticsearchParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import SaveModeItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/saveMode";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";
import {InfoCircleOutlined} from "@ant-design/icons";

const SinkElasticsearchStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonList(values, ElasticsearchParams.primaryKeys, ElasticsearchParams.primaryKeys);
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
        <DataSourceItem dataSource={'Elasticsearch'}/>
        <ProFormText
          name={ElasticsearchParams.index}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.index'})}
          rules={[{required: true}]}
        />
        <SaveModeItem/>
        <ProFormDigit
          name={ElasticsearchParams.maxBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.maxBatchSize'})}
          colProps={{span: 12}}
          initialValue={10}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={ElasticsearchParams.maxRetryCount}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.maxRetryCount'})}
          colProps={{span: 12}}
          initialValue={3}
          fieldProps={{
            min: 1,
          }}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.primaryKeys'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.elasticsearch.primaryKeys.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={ElasticsearchParams.primaryKeys}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkElasticsearchStepForm;
