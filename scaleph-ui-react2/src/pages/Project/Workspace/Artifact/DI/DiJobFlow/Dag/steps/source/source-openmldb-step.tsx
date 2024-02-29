import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {OpenMLDBParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {useEffect} from 'react';

const SourceOpenMLDBStepForm: React.FC<ModalFormProps<{
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
        <ProFormSwitch
          name={"cluster_mode"}
          label={intl.formatMessage({id: 'pages.project.di.step.openmldb.clusterMode'})}
          initialValue={false}
        />
        <ProFormDependency name={['cluster_mode']}>
          {({cluster_mode}) => {
            if (cluster_mode) {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={OpenMLDBParams.zkHost}
                    label={intl.formatMessage({id: 'pages.project.di.step.openmldb.zkHost'})}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={OpenMLDBParams.zkPath}
                    label={intl.formatMessage({id: 'pages.project.di.step.openmldb.zkPath'})}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup>
              <ProFormText
                name={OpenMLDBParams.host}
                label={intl.formatMessage({id: 'pages.project.di.step.openmldb.host'})}
                colProps={{span: 12}}
              />
              <ProFormDigit
                name={OpenMLDBParams.port}
                label={intl.formatMessage({id: 'pages.project.di.step.openmldb.port'})}
                colProps={{span: 12}}
                fieldProps={{
                  min: 0,
                  max: 65535
                }}
              />
            </ProFormGroup>;
          }}
        </ProFormDependency>
        <ProFormText
          name={OpenMLDBParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.openmldb.database'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={OpenMLDBParams.sql}
          label={intl.formatMessage({id: 'pages.project.di.step.openmldb.sql'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={OpenMLDBParams.sessionTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.openmldb.sessionTimeout'})}
          colProps={{span: 12}}
          initialValue={60000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={OpenMLDBParams.requestTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.openmldb.requestTimeout'})}
          colProps={{span: 12}}
          initialValue={10000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceOpenMLDBStepForm;
