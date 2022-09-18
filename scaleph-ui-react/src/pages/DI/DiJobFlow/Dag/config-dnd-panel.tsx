import {
  IGraphCommandService,
  NsGraph,
  NsNodeCmd,
  NsNodeCollapsePanel,
  uuidv4,
  XFlowNodeCommands,
} from '@antv/xflow';
import { CONNECTION_PORT_TYPE, NODE_HEIGHT, NODE_WIDTH } from './constant';
import { DagService } from './service';

const addNode = (cmd: IGraphCommandService, nodeConfig: NsGraph.INodeConfig) => {
  console.log('node', nodeConfig);
  return cmd.executeCommand<NsNodeCmd.AddNode.IArgs>(XFlowNodeCommands.ADD_NODE.id, {
    nodeConfig: {
      ...nodeConfig,
      id: nodeConfig.data.id ? nodeConfig.data.id : uuidv4(),
      width: NODE_WIDTH,
      height: NODE_HEIGHT,
      ports: {
        groups: {
          in: {
            position: 'top',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#31d0c6',
                strokeWidth: 2,
                fill: '#fff',
              },
            },
          },
          out: {
            position: 'bottom',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#31d0c6',
                strokeWidth: 2,
                fill: '#fff',
              },
            },
          },
        },
        items: createPortItems(nodeConfig.data.type, nodeConfig.data.name)
      },
    },
  });
};

const createPortItems = (type: string, label: string) => {
  if (type === 'source') {
    const items: NsGraph.INodeAnchor[] = [
      { id: CONNECTION_PORT_TYPE.source, group: 'out' }
    ];
    return items;
  } else if (type === 'trans') {
    const items: NsGraph.INodeAnchor[] = [
      { id: CONNECTION_PORT_TYPE.source, group: 'out' },
      { id: CONNECTION_PORT_TYPE.target, group: 'in' }
    ];
    return items;
  } else if (type === 'sink') {
    const items: NsGraph.INodeAnchor[] = [
      { id: CONNECTION_PORT_TYPE.target, group: 'in' }
    ];
    return items;
  } else {
    return [];
  }
}

export const onNodeDrop: NsNodeCollapsePanel.IOnNodeDrop = async (nodeConfig, commandService, modelService) => {
  const args: NsNodeCmd.AddNode.IArgs = {
    nodeConfig: { ...nodeConfig, id: uuidv4() },
  }
  commandService.executeCommand(XFlowNodeCommands.ADD_NODE.id, args)
};

export const nodeDataService: NsNodeCollapsePanel.INodeDataService = async (meta, modelService) => {
  const data = await DagService.loadNodeMeta();
  return data;
};

export const searchService: NsNodeCollapsePanel.ISearchService = async (
  nodes: NsNodeCollapsePanel.IPanelNode[] = [],
  keyword: string,
) => {
  const list = nodes.filter((node) => node.label?.includes(keyword));
  return list;
};
