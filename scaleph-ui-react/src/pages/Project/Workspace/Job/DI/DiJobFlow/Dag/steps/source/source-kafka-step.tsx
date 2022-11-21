import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {DiJob, MetaDataSourceParam} from '@/services/project/typings';
import {ProForm, ProFormSelect, ProFormSwitch, ProFormText, ProFormTextArea} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {KafkaParams, STEP_ATTR_TYPE} from '../../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {DataSourceService} from "@/services/project/dataSource.service";

const SourceKafkaStepForm: React.FC<ModalFormProps<{
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
        />
        <ProFormSelect
          name={"dataSourceType"}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSourceType'})}
          colProps={{span: 6}}
          initialValue={"Kafka"}
          fieldProps={{
            disabled: true
          }}
          request={(() => DictDataService.listDictDataByType(DICT_TYPE.datasourceType))}
        />

        <ProFormSelect
          name={STEP_ATTR_TYPE.dataSource}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSource'})}
          rules={[{required: true}]}
          colProps={{span: 18}}
          dependencies={["dataSourceType"]}
          request={((params, props) => {
            const param: MetaDataSourceParam = {
              datasourceName: params.keyWords,
              datasourceType: params.dataSourceType
            };
            return DataSourceService.listDataSourceByPage(param).then((response) => {
              return response.data.map((item) => {
                return {label: item.datasourceName, value: item.id, item: item};
              });
            });
          })}
        />
        <ProFormText
          name={KafkaParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.topic'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={KafkaParams.pattern}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.pattern'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.pattern.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={false}
        />
        <ProFormText
          name={KafkaParams.consumerGroup}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.consumerGroup'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.consumerGroup.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormSwitch
          name={KafkaParams.commit_on_checkpoint}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.commit_on_checkpoint'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.commit_on_checkpoint.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={true}
        />
        <ProFormTextArea
          name={KafkaParams.kafkaConf}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.conf'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.conf.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
      </ProForm>
    </Modal>
  );
};

export default SourceKafkaStepForm;
