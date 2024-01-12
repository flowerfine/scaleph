import React, {useEffect} from 'react';
import {Button, Drawer, Form, message} from 'antd';
import {ProForm, ProFormText} from '@ant-design/pro-components';
import {useIntl} from "@umijs/max";
import {Node} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const SinkConsoleStepForm: React.FC<
  ModalFormProps<{
    node: Node;
    job: WsDiJob;
  }>
> = ({data, visible, onCancel, onOK}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const {node, job} = data

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, node.data.label);
  }, []);

  return (
    <Drawer
      open={visible}
      title={node.data.label}
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
              map.set(STEP_ATTR_TYPE.jobId, job.id);
              map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify('待定'));
              map.set(STEP_ATTR_TYPE.stepCode, node.id);
              StepSchemaService.formatSchema(values);
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
      <ProForm form={form} initialValues={node.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
          colProps={{span: 24}}
        />
      </ProForm>
    </Drawer>
  );
};

export default SinkConsoleStepForm;
