import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {DiJob} from '@/services/project/typings';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {HiveParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProForm, ProFormSelect, ProFormSwitch, ProFormText,} from "@ant-design/pro-components";

const SinkHiveStepForm: React.FC<ModalFormProps<{
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
          colProps={{span: 24}}
        />
        <ProFormText
          name={HiveParams.tableName}
          label={intl.formatMessage({id: 'pages.project.di.step.hive.tableName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={HiveParams.metastoreUri}
          label={intl.formatMessage({id: 'pages.project.di.step.hive.metastoreUri'})}
          rules={[{required: true}]}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.hive.metastoreUri.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />

        <ProFormText
          name={HiveParams.partitionBy}
          label={intl.formatMessage({id: 'pages.project.di.step.hive.partitionBy'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.hive.partitionBy.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />

        <ProFormText
          name={HiveParams.sinkColumns}
          label={intl.formatMessage({id: 'pages.project.di.step.hive.sinkColumns'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.hive.sinkColumns.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormSwitch
          name={HiveParams.isEnableTransaction}
          label={intl.formatMessage({id: 'pages.project.di.step.hive.isEnableTransaction'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.hive.isEnableTransaction.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={true}
          disabled
        />
        <ProFormSelect
          name={HiveParams.saveMode}
          label={intl.formatMessage({id: 'pages.project.di.step.hive.saveMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.hive.saveMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          allowClear={false}
          initialValue={"append"}
          valueEnum={{
            append: {text: "append", disabled: false},
            overwrite: {text: "overwrite", disabled: true}
          }}
        />
      </ProForm>
    </Modal>
  );
};

export default SinkHiveStepForm;
