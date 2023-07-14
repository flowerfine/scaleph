import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {HudiParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {
  ProForm,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import {useEffect} from 'react';

const SourceHudiStepForm: React.FC<
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
              values[HudiParams.useKerberos] = values.useKerberos;
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
          name={HudiParams.tablePath}
          label={intl.formatMessage({id: 'pages.project.di.step.hudi.tablePath'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={HudiParams.tableType}
          label={intl.formatMessage({id: 'pages.project.di.step.hudi.tableType'})}
          rules={[{required: true}]}
          initialValue={'Copy On Write'}
          valueEnum={{
            cow: {text: 'Copy On Write', disabled: false},
            mor: {text: 'Merge On Read', disabled: true},
          }}
        />
        <ProFormText
          name={HudiParams.confFiles}
          label={intl.formatMessage({id: 'pages.project.di.step.hudi.confFiles'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={'useKerberos'}
          label={intl.formatMessage({id: 'pages.project.di.step.hudi.useKerberos'})}
        />
        <ProFormDependency name={['useKerberos']}>
          {({useKerberos}) => {
            if (useKerberos) {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={HudiParams.kerberosPrincipal}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.hudi.kerberosPrincipal',
                    })}
                    rules={[{required: true}]}
                  />
                  <ProFormText
                    name={HudiParams.kerberosPrincipalFile}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.hudi.kerberosPrincipalFile',
                    })}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </ProForm>
    </Drawer>
  );
};

export default SourceHudiStepForm;
