import enUS from 'antd/es/locale/en_US';
import zhCN from 'antd/es/locale/zh_CN';

import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {
  IArgsBase,
  ICmdHooks as IHooks,
  ICommandContextProvider,
  ICommandHandler,
  ManaSyringe,
  NsGraph,
} from '@antv/xflow';
import type {HookHub} from '@antv/xflow-hook';
import {ConfigProvider, Form, Input, message, Modal} from 'antd';
import {render, unmount} from 'rc-util/lib/React/render';
import React, {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {CustomCommands} from '../constant';

const {inject, injectable, postConstruct} = ManaSyringe;

type ICommand = ICommandHandler<NsGraphParamsSetting.IArgs,
  NsGraphParamsSetting.IResult,
  NsGraphParamsSetting.ICmdHooks>;

export namespace NsGraphParamsSetting {
  /** Command: 用于注册named factory */
  export const command = CustomCommands.GRAPH_PARAMS_SETTING;
  /** hook name */
  export const hookKey = 'graphParamsSetting';

  /** hook 参数类型 */
  export interface IArgs extends IArgsBase {
  }

  /** hook handler 返回类型 */
  export interface IResult {
    err: any;
  }

  /** hooks 类型 */
  export interface ICmdHooks extends IHooks {
    graphParamsSetting: HookHub<IArgs, IResult>;
  }
}

@injectable({
  token: {token: ICommandHandler, named: NsGraphParamsSetting.command.id},
})
/** 创建节点命令 */
export class GraphParamsSettingCommand implements ICommand {
  /** api */
  @inject(ICommandContextProvider) contextProvider: ICommand['contextProvider'];

  @postConstruct()
  init() {
  }

  /** 执行Cmd */
  execute = async () => {
    const ctx = this.contextProvider();
    const hooks = ctx.getHooks();
    const {args, hooks: runtimeHook} = ctx.getArgs();
    const result = await hooks.graphParamsSetting.call(
      args,
      async () => {
        const graphMeta = await ctx.getGraphMeta();
        this.showModal(graphMeta);
        return {err: null};
      },
      runtimeHook,
    );
    ctx.setResult(result || {err: null});
    return this;
  };

  /** undo cmd */
  undo = async () => {
    if (this.isUndoable()) {
      const ctx = this.contextProvider();
      ctx.undo();
    }
    return this;
  };

  /** redo cmd */
  redo = async () => {
    if (!this.isUndoable()) {
      await this.execute();
    }
    return this;
  };

  isUndoable(): boolean {
    const ctx = this.contextProvider();
    return ctx.isUndoable();
  }

  getCurrentLocale = () => {
    const local: string = getLocale() as string;
    switch (local) {
      case 'zh-CN':
        return zhCN;
      case 'en-US':
        return enUS;
      default:
        return zhCN;
    }
  };

  showModal = (graphMeta: NsGraph.IGraphMeta) => {
    const container = document.createDocumentFragment();
    const data = {graphMeta: graphMeta, container: container};
    return render(
      <ConfigProvider locale={this.getCurrentLocale()}>
        <GraphParamsSettingForm
          visible
          data={data}
          onCancel={() => this.onCancel(container)}
        ></GraphParamsSettingForm>
      </ConfigProvider>,
      container,
    );
  };

  onCancel = (container: DocumentFragment) => {
    unmount(container);
  };
}

const GraphParamsSettingForm: React.FC<ModalFormProps<{ graphMeta: NsGraph.IGraphMeta; container: DocumentFragment }>> = ({
                                                                                                                            data,
                                                                                                                            visible,
                                                                                                                            onCancel
                                                                                                                          }) => {
  const jobId = data.graphMeta.origin.id;
  const [form] = Form.useForm();
  const intl = getIntl(getLocale(), true);
  useEffect(() => {
    JobService.listJobAttr(jobId).then((d) => {
      form.setFieldsValue(d);
    });
  }, []);
  return (
    <Modal
      open={visible}
      title={intl.formatMessage({id: 'pages.project.di.flow.dag.prop'})}
      width={780}
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          JobService.saveJobAttr(values).then((resp) => {
            message.success(intl.formatMessage({id: 'app.common.operate.success'}));
            onCancel();
          });
        });
      }}
    >
      <Form form={form} layout="horizontal">
        <Form.Item name="jobId" hidden={true}>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="jobAttr"
          label={intl.formatMessage({id: 'pages.project.di.flow.dag.prop.jobAttr'})}
        >
          <Input.TextArea
            rows={5}
            placeholder={intl.formatMessage({id: 'pages.project.di.flow.dag.prop.placeholder'})}
          ></Input.TextArea>
        </Form.Item>
        <Form.Item
          name="jobProp"
          label={intl.formatMessage({id: 'pages.project.di.flow.dag.prop.jobProp'})}
        >
          <Input.TextArea
            rows={5}
            placeholder={intl.formatMessage({id: 'pages.project.di.flow.dag.prop.placeholder'})}
          ></Input.TextArea>
        </Form.Item>
        <Form.Item
          name="engineProp"
          label={intl.formatMessage({id: 'pages.project.di.flow.dag.prop.engineProp'})}
        >
          <Input.TextArea
            rows={5}
            placeholder={intl.formatMessage({id: 'pages.project.di.flow.dag.prop.placeholder'})}
          ></Input.TextArea>
        </Form.Item>
      </Form>
    </Modal>
  );
};
