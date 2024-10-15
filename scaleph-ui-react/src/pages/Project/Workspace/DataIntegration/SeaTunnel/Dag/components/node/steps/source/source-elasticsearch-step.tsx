import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormDigit, ProFormGroup, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {ElasticsearchParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";
import FieldItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourceElasticsearchStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonList(values, ElasticsearchParams.source, ElasticsearchParams.source)
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
        <DataSourceItem dataSource={"Elasticsearch"}/>
        <ProFormText
          name={ElasticsearchParams.index}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.index'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={ElasticsearchParams.query}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.query'})}
          initialValue={"{\"match_all\": {}}"}
        />
        <ProFormText
          name={ElasticsearchParams.scrollTime}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.scrollTime'})}
          colProps={{span: 12}}
          initialValue={"1m"}
        />
        <ProFormDigit
          name={ElasticsearchParams.scrollSize}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.scrollSize'})}
          colProps={{span: 12}}
          initialValue={100}
          fieldProps={{
            min: 1,
            step: 100
          }}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.source'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.elasticsearch.source.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={ElasticsearchParams.source}/>
        </ProFormGroup>
        <ProFormText
          name={ElasticsearchParams.arrayColumn}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.arrayColumn'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.arrayColumn.placeholder'})}
        />
        {/*<FieldItem/>*/}
      </DrawerForm>
    </XFlow>
  );
};

export default SourceElasticsearchStepForm;
