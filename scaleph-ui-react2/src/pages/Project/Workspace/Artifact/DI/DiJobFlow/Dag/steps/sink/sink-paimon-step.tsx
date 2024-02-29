import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {ProForm, ProFormText} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {PaimonParams, STEP_ATTR_TYPE} from '../../constant';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource";

const SinkPaimonStepForm: React.FC<ModalFormProps<{
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
        <ProFormText
          name={PaimonParams.warehouse}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.warehouse'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PaimonParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PaimonParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.table'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PaimonParams.hdfsSitePath}
          label={intl.formatMessage({id: 'pages.project.di.step.paimon.hdfsSitePath'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.paimon.hdfsSitePath.placeholder'})}
        />
      </ProForm>
    </Drawer>
  );
};

export default SinkPaimonStepForm;
