import enUS from 'antd/es/locale/en_US';
import zhCN from 'antd/es/locale/zh_CN';

import { ModalFormProps } from '@/app.d';
import { WsDiJobService } from '@/services/project/WsDiJobService';
import {
  IArgsBase,
  ICmdHooks as IHooks,
  ICommandContextProvider,
  ICommandHandler,
  ManaSyringe,
  NsGraph,
} from '@antv/xflow';
import type { HookHub } from '@antv/xflow-hook';
import { ConfigProvider, Descriptions, Form, Input, message, Modal } from 'antd';
import { render, unmount } from 'rc-util/lib/React/render';
import React, { useEffect } from 'react';
import { getIntl, getLocale } from 'umi';
import { CustomCommands } from '../constant';

const { inject, injectable, postConstruct } = ManaSyringe;

type ICommand = ICommandHandler<NsGraphHelp.IArgs, NsGraphHelp.IResult, NsGraphHelp.ICmdHooks>;

export namespace NsGraphHelp {
  /** Command: 用于注册named factory */
  export const command = CustomCommands.GRAPH_HELP;
  /** hook name */
  export const hookKey = 'graphHelp';

  /** hook 参数类型 */
  export interface IArgs extends IArgsBase {}

  /** hook handler 返回类型 */
  export interface IResult {
    err: any;
  }

  /** hooks 类型 */
  export interface ICmdHooks extends IHooks {
    graphHelp: HookHub<IArgs, IResult>;
  }
}

@injectable({
  token: { token: ICommandHandler, named: NsGraphHelp.command.id },
})
/** 创建节点命令 */
export class GraphHelpCommand implements ICommand {
  /** api */
  @inject(ICommandContextProvider) contextProvider: ICommand['contextProvider'];

  @postConstruct()
  init() {}

  /** 执行Cmd */
  execute = async () => {
    const ctx = this.contextProvider();
    const hooks = ctx.getHooks();
    const { args, hooks: runtimeHook } = ctx.getArgs();
    const result = await hooks.graphHelp.call(
      args,
      async () => {
        const graphMeta = await ctx.getGraphMeta();
        this.showModal(graphMeta);
        return { err: null };
      },
      runtimeHook,
    );
    ctx.setResult(result || { err: null });
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
    const data = { graphMeta: graphMeta, container: container };
    return render(
      <ConfigProvider locale={this.getCurrentLocale()}>
        <GraphHelpModal
          visible
          data={data}
          onCancel={() => this.onCancel(container)}
        ></GraphHelpModal>
      </ConfigProvider>,
      container,
    );
  };

  onCancel = (container: DocumentFragment) => {
    unmount(container);
  };
}

const GraphHelpModal: React.FC<
  ModalFormProps<{ graphMeta: NsGraph.IGraphMeta; container: DocumentFragment }>
> = ({ data, visible, onCancel }) => {
  const intl = getIntl(getLocale(), true);

  return (
    <Modal
      open={visible}
      title={intl.formatMessage({ id: 'pages.project.di.flow.dag.help' })}
      width={780}
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
      destroyOnClose={true}
      footer={false}
      onCancel={onCancel}
    >
      <Descriptions title={intl.formatMessage({ id: 'pages.project.di.flow.dag.help.key' })}>
        <Descriptions.Item
          label={intl.formatMessage({ id: 'pages.project.di.flow.dag.help.key.ctrlc' })}
        >
          Ctrl + C / Command + C
        </Descriptions.Item>
        <Descriptions.Item
          label={intl.formatMessage({ id: 'pages.project.di.flow.dag.help.key.ctrlv' })}
        >
          Ctrl + V / Command + V
        </Descriptions.Item>
        <Descriptions.Item
          label={intl.formatMessage({ id: 'pages.project.di.flow.dag.help.key.ctrlx' })}
        >
          Ctrl + X / Command + X
        </Descriptions.Item>
        <Descriptions.Item
          label={intl.formatMessage({ id: 'pages.project.di.flow.dag.help.key.del' })}
        >
          DELETE / BACKSPACE
        </Descriptions.Item>
        <Descriptions.Item
          label={intl.formatMessage({ id: 'pages.project.di.flow.dag.help.key.redo' })}
        >
          Ctrl + Y / Command + Y
        </Descriptions.Item>
        <Descriptions.Item
          label={intl.formatMessage({ id: 'pages.project.di.flow.dag.help.key.undo' })}
        >
          Ctrl + Z / Command + Z
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};
