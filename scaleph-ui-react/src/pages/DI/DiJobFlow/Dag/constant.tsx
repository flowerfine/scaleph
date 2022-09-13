import { IGraphCommand, XFlowGraphCommands } from '@antv/xflow';

export const DND_RENDER_ID = 'DND_NDOE';
export const GROUP_NODE_RENDER_ID = 'GROUP_NODE_RENDER_ID';
export const EDGE_NODE_RENDER_ID = 'EDGE_NODE_RENDER_ID';
export const NODE_WIDTH = 180;
export const NODE_HEIGHT = 36;
export const ZOOM_OPTIONS = { maxScale: 2, minScale: 0.5 };
export const CONNECTION_PORT_TYPE = { source: 'outPort', target: 'inPort' };

/** custom commands */
export namespace CustomCommands {
  export const GRAPH_CUT: IGraphCommand = {
    id: 'xflow:graph-cut-selection',
    label: 'cut',
    category: XFlowGraphCommands.GRAPH_COPY.category,
  };
}
// export const CustomCommands = {}
// // export namespace CustomCommands {
// const category = '节点操作'
// /** 异步请求demo */
// CustomCommands.TEST_ASYNC_CMD = {
//   id: 'xflow:async-cmd',
//   label: '异步请求',
//   category,
// }
// /** 重命名节点弹窗 */
// CustomCommands.SHOW_RENAME_MODAL = {
//   id: 'xflow:rename-node-modal',
//   label: '打开重命名弹窗',
//   category,
// }
// /** 部署服务 */
// CustomCommands.DEPLOY_SERVICE = {
//   id: 'xflow:deploy-service',
//   label: '部署服务',
//   category,
// }
// // }
