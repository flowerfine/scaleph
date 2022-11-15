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
import SinkJdbcStepForm from '../steps/sink/sink-jdbc-step';
import SourceJdbcStepForm from '../steps/source/source-jdbc-step';
import SourceHudiStepForm from "../steps/source/source-hudi-step";
import SourceIcebergStepForm from "../steps/source/source-iceberg-step";
import SourceLocalFileStepForm from "../steps/source/source-local-file-step";
import SourceHdfsFileStepForm from "../steps/source/source-hdfs-file-step";
import SinkLocalFileStepForm from '../steps/sink/sink-local-file-step';
import SourceFakeStepForm from "../steps/source/source-fake-step";
import SourceFtpFileStepForm from "../steps/source/source-ftp-file-step";
import SinkFtpFileStepForm from "../steps/sink/sink-ftp-file-step";
import SinkHdfsFileStepForm from "../steps/sink/sink-hdfs-file-step";
import SourceOSSFileStepForm from "../steps/source/source-oss-file-step";
import SinkOSSFileStepForm from "../steps/sink/sink-oss-file-step";
import SinkConsoleStepForm from '../steps/sink/sink-console-step';
import SinkHttpFileStepForm from "../steps/sink/sink-http-step";
import SourceHttpFileStepForm from "../steps/source/source-http-step";
import SourceSocketStepForm from '../steps/source/source-socket-step';
import SinkSocketStepForm from '../steps/sink/sink-socket-step';
import SinkClickHouseStepForm from '../steps/sink/sink-clickhouse-step';
import SourceClickHouseStepForm from "../steps/source/source-clickhouse-step";
import SinkWeChatStepForm from "../steps/sink/sink-wechat-step";
import SinkFeishuStepForm from "../steps/sink/sink-feishu-step";
import SinkDingTalkStepForm from "../steps/sink/sink-dingtalk-step";
import SinkEmailStepForm from "../steps/sink/sink-email-step";
import SourceHiveStepForm from '../steps/source/source-hive-step';
import SinkHiveStepForm from '../steps/sink/sink-hive-step';
import SourceKuduStepForm from '../steps/source/source-kudu-step';
import SinkKuduStepForm from '../steps/sink/sink-kudu-step';
import SourceKafkaStepForm from '../steps/source/source-kafka-step';
import SourceIoTDBStepForm from "../steps/source/source-iotdb-step";
import SinkIoTDBStepForm from "../steps/sink/sink-iotdb-step";
import SourceMongoDBStepForm from "../steps/source/source-mongodb-step";
import SinkMongoDBStepForm from "../steps/sink/sink-mongodb-step";
import SourceRedisStepForm from "../steps/source/source-redis-step";
import SinkRedisStepForm from "../steps/sink/sink-redis-step";
import SourcePulsarStepForm from "../steps/source/source-pulsar-step";
import SinkDatahubStepForm from "../steps/sink/sink-datahub-step";
import SinkElasticsearchStepForm from "../steps/sink/sink-elasticsearch-step";
import SinkNeo4jStepForm from "../steps/sink/sink-neo4j-step";
import SinkSentryStepForm from "../steps/sink/sink-sentry-step";
import SourceS3FileStepForm from "../steps/source/source-s3-file-step";
import SinkS3FileStepForm from "../steps/sink/sink-s3-file-step";

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
    } else if (type === 'sink' && name === 'LocalFile') {
      return (<SinkLocalFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'FtpFile') {
      return (<SourceFtpFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'sink' && name === 'FtpFile') {
      return (<SinkFtpFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'HdfsFile') {
      return (<SourceHdfsFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'sink' && name === 'HdfsFile') {
      return (<SinkHdfsFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'OssFile') {
      return (<SourceOSSFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'sink' && name === 'OssFile') {
      return (<SinkOSSFileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'S3File') {
      return (<SourceS3FileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'sink' && name === 'S3File') {
      return (<SinkS3FileStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'Jdbc') {
      return (<SourceJdbcStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'sink' && name === 'Jdbc') {
      return (<SinkJdbcStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'Hudi') {
      return (<SourceHudiStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if (type === 'source' && name === 'Iceberg') {
      return (<SourceIcebergStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if  (type === 'source' && name === 'FakeSource') {
      return (<SourceFakeStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type==='sink' && name ==='Console'){
      return (<SinkConsoleStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type==='source' && name ==='Http'){
      return (<SourceHttpFileStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type==='sink' && name ==='Http'){
      return (<SinkHttpFileStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type==='sink' && name ==='Feishu'){
      return (<SinkFeishuStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type==='sink' && name ==='WeChat'){
      return (<SinkWeChatStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type==='sink' && name ==='DingTalk'){
      return (<SinkDingTalkStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type==='sink' && name ==='Email'){
      return (<SinkEmailStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'Socket'){
      return (<SourceSocketStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'Socket'){
      return (<SinkSocketStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'ClickHouse'){
      return (<SourceClickHouseStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'ClickHouse'){
      return (<SinkClickHouseStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'Hive'){
      return (<SourceHiveStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'Hive'){
      return (<SinkHiveStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'Kudu'){
      return (<SourceKuduStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'Kudu'){
      return (<SinkKuduStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'Kafka'){
      return (<SourceKafkaStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'IoTDB'){
      return (<SourceIoTDBStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'IoTDB'){
      return (<SinkIoTDBStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'MongoDB'){
      return (<SourceMongoDBStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'MongoDB'){
      return (<SinkMongoDBStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'Redis'){
      return (<SourceRedisStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'Redis'){
      return (<SinkRedisStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'source' && name === 'Pulsar'){
      return (<SourcePulsarStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'DataHub'){
      return (<SinkDatahubStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'elasticsearch'){
      return (<SinkElasticsearchStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'Neo4j'){
      return (<SinkNeo4jStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else if(type === 'sink' && name === 'Sentry'){
      return (<SinkSentryStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
    } else {
      return <></>;
    }
  };
}
