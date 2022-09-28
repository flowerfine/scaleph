import { JobService } from '@/services/project/job.service';
import {
  IArgsBase,
  ICmdHooks as IHooks,
  ICommandContextProvider,
  ICommandHandler,
  ManaSyringe,
} from '@antv/xflow';
import type { HookHub } from '@antv/xflow-hook';
import { message } from 'antd';
import { getIntl, getLocale } from 'umi';
import { CustomCommands } from '../constant';
const { inject, injectable, postConstruct } = ManaSyringe;

type ICommand = ICommandHandler<
  NsGraphPublish.IArgs,
  NsGraphPublish.IResult,
  NsGraphPublish.ICmdHooks
>;

export namespace NsGraphPublish {
  /** Command: 用于注册named factory */
  export const command = CustomCommands.GRAPH_PUBLISH;
  /** hook name */
  export const hookKey = 'graphPublish';
  /** hook 参数类型 */
  export interface IArgs extends IArgsBase {}

  /** hook handler 返回类型 */
  export interface IResult {
    err: any;
  }
  /** hooks 类型 */
  export interface ICmdHooks extends IHooks {
    graphPublish: HookHub<IArgs, IResult>;
  }
}

@injectable({
  token: { token: ICommandHandler, named: NsGraphPublish.command.id },
})
/** 创建节点命令 */
export class GraphPublishCommand implements ICommand {
  @inject(ICommandContextProvider) contextProvider: ICommand['contextProvider'];

  @postConstruct()
  init() {}

  /** 执行Cmd */
  execute = async () => {
    const intl = getIntl(getLocale(), true);
    const ctx = this.contextProvider();
    const hooks = ctx.getHooks();
    const { args, hooks: runtimeHook } = ctx.getArgs();
    const result = await hooks.graphPublish.call(
      args,
      async () => {
        const graphMeta = await ctx.getGraphMeta();
        JobService.publishJob(graphMeta.origin.id).then((resp) => {
          if (resp.success) {
            message.info(intl.formatMessage({ id: 'app.common.operate.success' }));
          }
        });
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
}
