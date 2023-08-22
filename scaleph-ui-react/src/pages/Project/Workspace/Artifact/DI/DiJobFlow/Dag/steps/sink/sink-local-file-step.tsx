import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import {useEffect} from 'react';
import {BaseFileParams, STEP_ATTR_TYPE} from '../../constant';

const SinkLocalFileStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({data, visible, onCancel, onOK}) => {
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
          colProps={{span: 24}}
        />
        <ProFormText
          name={BaseFileParams.path}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
          rules={[{required: true}]}
          colProps={{span: 24}}
        />
        <ProFormSelect
          name={BaseFileParams.fileFormatType}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFormat'})}
          colProps={{span: 24}}
          options={['json', 'parquet', 'orc', 'text', 'csv', 'excel']}
        />
        <ProFormDependency name={[BaseFileParams.fileFormatType]}>
          {({file_format_type}) => {
            if (file_format_type == 'text' || file_format_type == 'csv') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={BaseFileParams.fieldDelimiter}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.baseFile.fieldDelimiter',
                    })}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={BaseFileParams.rowDelimiter}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.baseFile.rowDelimiter',
                    })}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            if (file_format_type == 'excel') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={BaseFileParams.sheetName}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sheetName'})}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={BaseFileParams.maxRowsInMemory}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.maxRowsInMemory'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSwitch
          name={BaseFileParams.customFilename}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.customFilename'})}
          initialValue={false}
        />
        <ProFormText
          name={BaseFileParams.fileNameExpression}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileNameExpression'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={BaseFileParams.filenameTimeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.filenameTimeFormat'})}
          colProps={{span: 12}}
        />
        <ProFormSwitch
          name={BaseFileParams.havePartition}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.havePartition'})}
          initialValue={false}
        />
        <ProFormText
          name={BaseFileParams.partitionBy}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionBy'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={BaseFileParams.partitionDirExpression}
          label={intl.formatMessage({
            id: 'pages.project.di.step.baseFile.partitionDirExpression',
          })}
          colProps={{span: 12}}
        />
        <ProFormSwitch
          name={BaseFileParams.isPartitionFieldWriteInFile}
          label={intl.formatMessage({
            id: 'pages.project.di.step.baseFile.isPartitionFieldWriteInFile',
          })}
          colProps={{span: 24}}
        />
        <ProFormText
          name={BaseFileParams.sinkColumns}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sinkColumns'})}
          colProps={{span: 24}}
        />
        <ProFormSwitch
          name={BaseFileParams.isEnableTransaction}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isEnableTransaction'})}
          colProps={{span: 24}}
          initialValue={true}
          disabled
        />
        <ProFormDigit
          name={BaseFileParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.batchSize'})}
          colProps={{span: 24}}
          initialValue={1000000}
          fieldProps={{
            step: 10000,
            min: 0,
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SinkLocalFileStepForm;
