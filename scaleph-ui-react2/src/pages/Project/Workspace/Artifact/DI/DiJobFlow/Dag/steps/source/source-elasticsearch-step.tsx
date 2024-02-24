import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {ElasticsearchParams, SchemaParams, STEP_ATTR_TYPE} from '../../constant';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/helper";
import {InfoCircleOutlined} from "@ant-design/icons";

const SourceElasticsearchStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as WsDiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Drawer
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll'}}
      destroyOnClose={true}
      onClose={onCancel}
      extra={
        <Button
          type="primary"
          onClick={() => {
            form.validateFields().then((values) => {
              let map: Map<string, any> = new Map();
              map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
              map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
              map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
              StepSchemaService.formatEsSource(values)
              StepSchemaService.formatSchema(values);
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              WsDiJobService.saveStepAttr(map).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.success'}));
                  onCancel();
                  onOK ? onOK(values) : null;
                }
              });
            });
          }}
        >
          {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
        </Button>
      }
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
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
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.source'})}
        >
          <ProFormList
            name={ElasticsearchParams.sourceArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.elasticsearch.source.field'}),
              type: 'text',
            }}
          >
            <ProFormText
              name={ElasticsearchParams.sourceField}
              colProps={{span: 16, offset: 4}}
            />
          </ProFormList>
        </ProFormGroup>
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.schema'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.schema.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={SchemaParams.fields}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.schema.fields'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={SchemaParams.field}
                label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.field'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={SchemaParams.type}
                label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.type'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
      </ProForm>
    </Drawer>
  );
};

export default SourceElasticsearchStepForm;
