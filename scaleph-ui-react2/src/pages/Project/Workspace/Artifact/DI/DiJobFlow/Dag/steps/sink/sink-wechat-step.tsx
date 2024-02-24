import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {STEP_ATTR_TYPE, WeChatParams} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {ProForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {useEffect} from 'react';
import {InfoCircleOutlined} from '@ant-design/icons';
import {StepSchemaService} from '../helper';

const SinkWeChatStepForm: React.FC<
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
              StepSchemaService.formatUserIds(values);
              StepSchemaService.formatMobiles(values);
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
          name={WeChatParams.url}
          label={intl.formatMessage({id: 'pages.project.di.step.wechat.url'})}
          rules={[{required: true}]}
        />
        <ProFormList
          name={WeChatParams.mentionedArray}
          label={intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedList'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedList.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.wechat.userId'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText name={WeChatParams.userId} colProps={{span: 20, offset: 1}}/>
          </ProFormGroup>
        </ProFormList>

        <ProFormList
          name={WeChatParams.mentionedMobileArray}
          label={intl.formatMessage({id: 'pages.project.di.step.wechat.mentionedMobileList'})}
          tooltip={{
            title: intl.formatMessage({
              id: 'pages.project.di.step.wechat.mentionedMobileList.tooltip',
            }),
            icon: <InfoCircleOutlined/>,
          }}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.wechat.mobile'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText name={WeChatParams.mobile} colProps={{span: 20, offset: 1}}/>
          </ProFormGroup>
        </ProFormList>
      </ProForm>
    </Drawer>
  );
};

export default SinkWeChatStepForm;
