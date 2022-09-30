import enUS from 'antd/es/locale/en_US';
import zhCN from 'antd/es/locale/zh_CN';

import {
  IArgsBase,
  ICmdHooks as IHooks,
  ICommandContextProvider,
  ICommandHandler,
  ManaSyringe,
  NsGraph,
  XFlowGraphCommands,
} from '@antv/xflow';
import type {HookHub} from '@antv/xflow-hook';
import {ConfigProvider} from 'antd';
import {render, unmount} from 'rc-util/lib/React/render';
import {getLocale} from 'umi';
import {CustomCommands} from '../constant';
import {DagService} from '../service';
import SinkJdbcStepForm from '../steps/sink-jdbc-step';
import SourceJdbcStepForm from '../steps/source-jdbc-step';
import SourceHudiStepForm from "@/pages/DI/DiJobFlow/Dag/steps/source-hudi-step";
import SourceIcebergStepForm from "@/pages/DI/DiJobFlow/Dag/steps/source-iceberg-step";
import SourceLocalFileStepForm from "@/pages/DI/DiJobFlow/Dag/steps/source-local-file-step";
import SourceHdfsFileStepForm from "@/pages/DI/DiJobFlow/Dag/steps/source-hdfs-file-step";
import SinkLocalFileStepForm from '../steps/sink-local-file-step';
import SourceFakeStepForm from "@/pages/DI/DiJobFlow/Dag/steps/source-fake-step";


const {inject, injectable, postConstruct} = ManaSyringe;
type ICommand = ICommandHandler<NsEditNode.IArgs, NsEditNode.IResult, NsEditNode.ICmdHooks>;

export namespace NsEditNode {
  /** Command: 用于注册named factory */
  export const command = CustomCommands.NODE_EDIT;
  /** hook name */
  export const hookKey = 'editNode';

  /** hook 参数类型 */
  export interface IArgs extends IArgsBase {
    nodeConfig: NsGraph.INodeConfig;
  }

  /** hook handler 返回类型 */
  export interface IResult {
    err: any;
  }

  /** hooks 类型 */
  export interface ICmdHooks extends IHooks {
    editNode: HookHub<IArgs, IResult>;
  }
}

@injectable({
  token: {token: ICommandHandler, named: NsEditNode.command.id},
})
/** 创建节点命令 */
export class EditNodeCommand implements ICommand {
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
    const {nodeConfig} = args;
    const result = await hooks.editNode.call(
      args,
      async () => {
        // const { nodeConfig, callback } = handlerArgs;
        const graphMeta = await ctx.getGraphMeta();
        const x6Graph = await ctx.getX6Graph();
        const x6Nodes = x6Graph.getNodes();
        const x6Edges = x6Graph.getEdges();

        const nodes = x6Nodes.map((node) => {
          const data = node.getData<NsGraph.INodeConfig>();
          const position = node.position();
          const size = node.size();
          const model = {
            ...data,
            ...position,
            ...size,
          };
          return model;
        });
        const edges = x6Edges.map((edge) => {
          const data = edge.getData<NsGraph.IEdgeConfig>();
          const model = {
            ...data,
          };
          return model;
        });
        const graphData = {nodes, edges};
        this.showModal(nodeConfig, graphData, graphMeta);
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

  showModal = (
    node: NsGraph.INodeConfig,
    graphData: NsGraph.IGraphData,
    graphMeta: NsGraph.IGraphMeta,
  ) => {
    const container = document.createDocumentFragment();
    return render(
      <ConfigProvider locale={this.getCurrentLocale()}>
        {this.switchStep({node, graphData, graphMeta}, container)}
      </ConfigProvider>,
      container,
    );
  };

  onCancel = (container: DocumentFragment) => {
    unmount(container);
  };

  onOk = (
    data: {
      node: NsGraph.INodeConfig;
      graphData: NsGraph.IGraphData;
      graphMeta: NsGraph.IGraphMeta;
    },
    container: DocumentFragment,
  ) => {
    const jobId = data.graphMeta.origin.id;
    const ctx = this.contextProvider();
    DagService.loadJobInfo(jobId).then((d) => {
      ctx.getCommands().executeCommand(XFlowGraphCommands.GRAPH_RENDER.id, {
        graphData: d,
      });
    });
    unmount(container);
  };

  switchStep = (
    data: {
      node: NsGraph.INodeConfig;
      graphData: NsGraph.IGraphData;
      graphMeta: NsGraph.IGraphMeta;
    },
    container: DocumentFragment,
  ) => {
    const {name, type} = data.node.data.data;
    if (type === 'source' && name === 'LocalFile') {
      return (<SourceLocalFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'HdfsFile') {
      return (<SourceHdfsFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'Jdbc') {
      return (<SourceJdbcStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'Hudi') {
      return (<SourceHudiStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'Iceberg') {
      return (<SourceIcebergStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'sink' && name === 'LocalFile') {
      return (<SinkLocalFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    }else if  (type === 'source' && name === 'Fake') {
      return (<SourceFakeStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'sink' && name === 'Jdbc') {
      return (<SinkJdbcStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else {
      return <></>;
    }
  };
}
