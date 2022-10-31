import {IArgsBase, ICmdHooks as IHooks, ICommandContextProvider, ICommandHandler, ManaSyringe,} from '@antv/xflow';
import type {HookHub} from '@antv/xflow-hook';
import {CustomCommands} from '../constant';
import {history} from 'umi';

const {inject, injectable, postConstruct} = ManaSyringe;

type ICommand = ICommandHandler<NsGraphSubmit.IArgs,
  NsGraphSubmit.IResult,
  NsGraphSubmit.ICmdHooks>;

export namespace NsGraphSubmit {
  /** Command: 用于注册named factory */
  export const command = CustomCommands.GRAPH_SUBMIT;
  /** hook name */
  export const hookKey = 'graphSubmit';

  /** hook 参数类型 */
  export interface IArgs extends IArgsBase {
  }

  /** hook handler 返回类型 */
  export interface IResult {
    err: any;
  }

  /** hooks 类型 */
  export interface ICmdHooks extends IHooks {
    graphSubmit: HookHub<IArgs, IResult>;
  }
}

@injectable({
  token: {token: ICommandHandler, named: NsGraphSubmit.command.id}
})
/** 创建节点命令 */
export class GraphSubmitCommand implements ICommand {
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
    const result = await hooks.graphSubmit.call(
      args,
      async () => {
        const graphMeta = await ctx.getGraphMeta();
        history.push('/workspace/dev/job/seatunnel/options', graphMeta.origin);
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

}
