import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {HudiParams, STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
import {JobService} from "@/services/project/job.service";
import {Form, message, Modal} from "antd";
import {DiJob} from "@/services/project/typings";
import {getIntl, getLocale} from "umi";
import {
  ProForm,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from "@ant-design/pro-components";
import {useEffect} from "react";

const SourceHudiStepForm: React.FC<ModalFormProps<{
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
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
  }, []);

  return (<Modal
    open={visible}
    title={nodeInfo.data.displayName}
    width={780}
    bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
    destroyOnClose={true}
    onCancel={onCancel}
    onOk={() => {
      form.validateFields().then((values) => {
        let map: Map<string, string> = new Map();
        map.set(STEP_ATTR_TYPE.jobId, jobInfo.id + '');
        map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
        map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
        map.set(STEP_ATTR_TYPE.stepAttrs, form.getFieldsValue());
        JobService.saveStepAttr(map).then((resp) => {
          if (resp.success) {
            message.success(intl.formatMessage({id: 'app.common.operate.success'}));
            onCancel();
            onOK ? onOK() : null;
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
      <ProFormText
        name={HudiParams.tablePath}
        label={intl.formatMessage({id: 'pages.project.di.step.hudi.tablePath'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name={HudiParams.tableType}
        label={intl.formatMessage({id: 'pages.project.di.step.hudi.tableType'})}
        rules={[{required: true}]}
        valueEnum={{
          cow: {text: "Copy On Write", disabled: false},
          mor: {text: "Merge On Read", disabled: true}
        }}
      />
      <ProFormText
        name={HudiParams.confFiles}
        label={intl.formatMessage({id: 'pages.project.di.step.hudi.confFiles'})}
        rules={[{required: true}]}
      />
      <ProFormSwitch
        name={"useKerberos"}
        label={intl.formatMessage({id: 'pages.project.di.step.hudi.useKerberos'})}
      />
      <ProFormDependency name={["useKerberos"]}>
        {({useKerberos}) => {
          if (useKerberos) {
            return (
              <ProFormGroup>
                <ProFormText
                  name={HudiParams.kerberosPrincipal}
                  label={intl.formatMessage({id: 'pages.project.di.step.hudi.kerberosPrincipal'})}
                  rules={[{required: true}]}
                />
                <ProFormText
                  name={HudiParams.kerberosPrincipalFile}
                  label={intl.formatMessage({id: 'pages.project.di.step.hudi.kerberosPrincipalFile'})}
                  rules={[{required: true}]}
                />
              </ProFormGroup>
            );
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
    </ProForm>
  </Modal>);
}

export default SourceHudiStepForm;
