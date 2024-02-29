import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {FilterRowKindParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Form, message, Modal} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {ProForm, ProFormSelect, ProFormText,} from '@ant-design/pro-components';
import {useEffect} from 'react';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";

const TransformFilterRowKindStepForm: React.FC<ModalFormProps<{
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
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <ProFormSelect
          name={FilterRowKindParams.includeKinds}
          label={intl.formatMessage({id: 'pages.project.di.step.filterRowKind.includeKinds'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.filterRowKind.includeKinds.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          mode={'multiple'}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelRowKind)
          }}
        />
        <ProFormSelect
          name={FilterRowKindParams.excludeKinds}
          label={intl.formatMessage({id: 'pages.project.di.step.filterRowKind.excludeKinds'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.filterRowKind.excludeKinds.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          mode={'multiple'}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelRowKind)
          }}
        />
      </ProForm>
    </Modal>
  );
};

export default TransformFilterRowKindStepForm;
