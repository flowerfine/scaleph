import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {BaseFileParams, S3FileParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {
  ProForm,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {useEffect} from 'react';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";
import SchemaItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/schema";
import {InfoCircleOutlined} from "@ant-design/icons";

const SourceS3FileStepForm: React.FC<ModalFormProps<{
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
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
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
              StepSchemaService.formatSchema(values);
              StepSchemaService.formatHadoopS3Properties(values)
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              WsDiJobService.saveStepAttr(map).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.success'}));
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
        <DataSourceItem dataSource={"S3"}/>
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={S3FileParams.hadoopS3Properties}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.list'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={S3FileParams.key}
                label={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.key'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.key.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={S3FileParams.value}
                label={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.value'})}
                placeholder={intl.formatMessage({id: 'pages.project.di.step.s3.hadoop_s3_properties.value.placeholder'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
        <ProFormText
          name={BaseFileParams.path}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={BaseFileParams.readColumns}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.readColumns'})}
        />
        <ProFormText
          name={BaseFileParams.fileFilterPattern}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFilterPattern'})}
        />
        <SchemaItem/>
        <ProFormSwitch
          name={BaseFileParams.parsePartitionFromPath}
          label={intl.formatMessage({
            id: 'pages.project.di.step.baseFile.parsePartitionFromPath',
          })}
          initialValue={true}
        />
        <ProFormSelect
          name={BaseFileParams.dateFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.dateFormat'})}
          initialValue={'yyyy-MM-dd'}
          options={['yyyy-MM-dd', 'yyyy.MM.dd', 'yyyy/MM/dd']}
        />
        <ProFormSelect
          name={BaseFileParams.timeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.timeFormat'})}
          initialValue={'HH:mm:ss'}
          options={['HH:mm:ss', 'HH:mm:ss.SSS']}
        />
        <ProFormSelect
          name={BaseFileParams.datetimeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.datetimeFormat'})}
          initialValue={'yyyy-MM-dd HH:mm:ss'}
          options={[
            'yyyy-MM-dd HH:mm:ss',
            'yyyy.MM.dd HH:mm:ss',
            'yyyy/MM/dd HH:mm:ss',
            'yyyyMMddHHmmss',
          ]}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceS3FileStepForm;
