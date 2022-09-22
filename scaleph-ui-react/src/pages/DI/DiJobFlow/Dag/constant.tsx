import { IGraphCommand, XFlowGraphCommands, XFlowNodeCommands } from '@antv/xflow';

export const DND_RENDER_ID = 'DND_NODE';
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

  export const NODE_EDIT: IGraphCommand = {
    id: 'xflow:node-edit',
    label: 'edit',
    category: XFlowNodeCommands.UPDATE_NODE.category,
  };
}
