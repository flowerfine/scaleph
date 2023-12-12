import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from '@/constants/dictType';
import {DictDataService} from '@/services/admin/dictData.service';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {InfoCircleOutlined} from '@ant-design/icons';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {JdbcParams, STEP_ATTR_TYPE} from '../../constant';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {DsInfoService} from "@/services/datasource/info.service";
import {DsInfoParam} from "@/services/datasource/typings";

const SourceJdbcStepForm: React.FC<ModalFormProps<{
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
        <ProFormSelect
          name={"dataSourceType"}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSourceType'})}
          colProps={{span: 6}}
          initialValue={"MySQL"}
          allowClear={false}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.datasourceType)}
        />
        <ProFormSelect
          name={STEP_ATTR_TYPE.dataSource}
          label={intl.formatMessage({id: 'pages.project.di.step.dataSource'})}
          rules={[{required: true}]}
          colProps={{span: 18}}
          dependencies={["dataSourceType"]}
          request={((params, props) => {
            const param: DsInfoParam = {
              name: params.keyWords,
              dsType: params.dataSourceType
            };
            return DsInfoService.list(param).then((response) => {
              return response.data.map((item) => {
                return {label: item.name, value: item.id, item: item};
              });
            });
          })}
        />
        <ProFormDigit
          name={JdbcParams.connectionCheckTimeoutSec}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.connectionCheckTimeoutSec'})}
          fieldProps={{
            min: 0
          }}
        />
        <ProFormText
          name={JdbcParams.compatibleMode}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormText
          name={JdbcParams.partitionColumn}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionColumn'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionColumn.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />
        <ProFormDigit
          name={JdbcParams.partitionLowerBound}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionLowerBound'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionLowerBound.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />
        <ProFormDigit
          name={JdbcParams.partitionUpperBound}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionUpperBound'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionUpperBound.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />
        <ProFormDigit
          name={JdbcParams.partitionNum}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionNum'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionNum.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 6}}
        />
        <ProFormDigit
          name={JdbcParams.fetchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.fetchSize'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.jdbc.fetchSize.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={0}
        />

        <ProFormTextArea
          name={JdbcParams.query}
          label={intl.formatMessage({id: 'pages.project.di.step.jdbc.query'})}
          rules={[{required: true}]}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceJdbcStepForm;
