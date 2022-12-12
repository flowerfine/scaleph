import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {MondoDBParams, SchemaParams, STEP_ATTR_TYPE} from '../../constant';
import {JobService} from '@/services/project/job.service';
import {Form, message, Modal} from 'antd';
import {DiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {InfoCircleOutlined} from '@ant-design/icons';
import {useEffect} from 'react';
import {ProForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";

const SourceMongoDBStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as DiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Modal
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let map: Map<string, any> = new Map();
          map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
          map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
          map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
          StepSchemaService.formatSchema(values);
          map.set(STEP_ATTR_TYPE.stepAttrs, values);
          JobService.saveStepAttr(map).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.success'}));
              onCancel();
              onOK ? onOK(values) : null;
            }
          });
        });
      }}
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <DataSourceItem dataSource={"MongoDB"}/>
        <ProFormText
          name={MondoDBParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={MondoDBParams.collection}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.collection'})}
          rules={[{required: true}]}
        />
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
    </Modal>
  );
};

export default SourceMongoDBStepForm;
