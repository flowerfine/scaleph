import type { Cell } from '@antv/x6';
import { HookHub, IArgsBase, ICommandHandler, IHooks, NsGraph } from '@antv/xflow';
import { ICommandContextProvider } from '@antv/xflow-core/es/command/interface';
import { Disposable } from '@antv/xflow-core/es/common/disposable';
import { cellsToJson } from '@antv/xflow-core/es/common/graph-utils';
import { LOCAL_STORAGE_KEY } from '@antv/xflow-core/es/constants';
import { inject, injectable } from 'mana-syringe';
import { CustomCommands } from '../constant';

type ICommand = ICommandHandler<
  NsGraphCutSelection.IArgs,
  NsGraphCutSelection.IResult,
  NsGraphCutSelection.ICmdHooks
>;

export namespace NsGraphCutSelection {
  /** command : register name facotry */
  export const command = CustomCommands.GRAPH_CUT;
  /** hookName*/
  export const hookKey = 'graphCutSelection';
  /** hook parameter type */
  export type IArgs = IArgsBase;
  /** hook handler return data type */
  export interface IResult {
    err: null | string;
  }
  /**hook type */
  export interface ICmdHooks extends IHooks {
    graphCopySelection: HookHub<IArgs, IResult>;
  }
}

@injectable({
  token: { token: ICommandHandler, named: NsGraphCutSelection.command.id },
})
export class GraphCutSelectionCommand implements ICommand {
  @inject(ICommandContextProvider) contextProvider: ICommand['contextProvider'];

  parseCells = (cells: Cell[]) => {
    // if groupNode add its group children
    cells.forEach((cell) => {
      const data = cell.getData();
      if (cell.isNode() && data.isGroup) {
        const children = cell.getChildren();
        children?.forEach((child) => {
          cells.push(child);
        });
      }
    });
    // filter edges target not in selections
    const nodeIds = cells.filter((cell) => cell.isNode()).map((cell) => cell.id);
    const map = cells.reduce((acc, cell) => {
      if (cell.isEdge()) {
        const source = cell.getSourceCellId();
        const target = cell.getTargetCellId();
        if (source && target) {
          if (nodeIds.includes(source) && nodeIds.includes(target)) {
            acc.set(cell.id, cell);
          }
        }
      } else {
        acc.set(cell.id, cell);
      }
      return acc;
    }, new Map<string, Cell>());
    const uniqeList = Array.from(map.values());
    return cellsToJson(uniqeList);
  };

  execute = async () => {
    const ctx = this.contextProvider();
    const { args, hooks: runtimeHook } = ctx.getArgs();
    const hooks = ctx.getHooks();
    const result = await hooks.graphCopySelection.call(
      args,
      async () => {
        const graph = await ctx.getX6Graph();
        const cells = graph.getSelectedCells();
        graph.removeCells(cells);
        // 处理 Group cells/过滤无效的edges
        const jsonObject: NsGraph.IGraphData = this.parseCells(cells);
        const oldJsonString = window.localStorage.getItem(LOCAL_STORAGE_KEY);
        // 写cache
        window.localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(jsonObject));
        // undo 写cache
        ctx.addUndo(
          Disposable.create(async () => {
            window.localStorage.setItem(LOCAL_STORAGE_KEY, oldJsonString + '');
          }),
        );
        return { err: null };
      },
      runtimeHook,
    );
    result ? ctx.setResult(result) : null;
    return this;
  };

  /** undo cmd */
  undo = async () => {
    const ctx = this.contextProvider();
    if (this.isUndoable()) {
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
