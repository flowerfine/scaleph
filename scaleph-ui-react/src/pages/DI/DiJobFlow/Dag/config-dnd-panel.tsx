import {
  IGraphCommandService,
  NsGraph,
  NsNodeCmd,
  NsNodeCollapsePanel,
  uuidv4,
  XFlowNodeCommands,
} from '@antv/xflow';
import { DND_RENDER_ID, NODE_HEIGHT, NODE_WIDTH } from './constant';

// const NodeDescription = props => {
//   return (
//     <Card size="small" title="算法组件介绍" style={{ width: '200px' }} bordered={false}>
//       欢迎使用：{props.name}
//       这里可以根据服务端返回的数据显示不同的内容
//     </Card>
//   )
// }

const addNode = (cmd: IGraphCommandService, nodeConfig: NsGraph.INodeConfig) => {
  console.log('node', nodeConfig);
  return cmd.executeCommand<NsNodeCmd.AddNode.IArgs>(XFlowNodeCommands.ADD_NODE.id, {
    nodeConfig: {
      ...nodeConfig,
      id: uuidv4(),
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
        items: [{ id: nodeConfig.label + '-input-1', group: 'in' }],
      },
    },
  });
};

export const onNodeDrop: NsNodeCollapsePanel.IOnNodeDrop = async (nodeConfig, commandService) => {
  addNode(commandService, nodeConfig);
};

export const nodeDataService: NsNodeCollapsePanel.INodeDataService = async (meta, modelService) => {
  console.log(meta, modelService);
  return [
    {
      id: '数据读写',
      header: '数据读写',
      children: [
        {
          id: '2',
          label: '算法组件1',
          renderKey: DND_RENDER_ID,
          // renderComponent:""
        },
        {
          id: '3',
          label: '算法组件2',
          renderKey: DND_RENDER_ID,
          // popoverContent: <div> 算法组件2的描述 </div>,
        },
        {
          id: '4',
          label: '算法组件3',
          renderKey: DND_RENDER_ID,
          // popoverContent: <div> 算法组件3的描述 </div>,
        },
      ],
    },
    {
      id: '数据加工',
      header: '数据加工',
      children: [
        {
          id: '6',
          label: '算法组件4',
          parentId: '5',
          renderKey: DND_RENDER_ID,
        },
        {
          id: '7',
          label: '算法组件5',
          parentId: '5',
          renderKey: DND_RENDER_ID,
        },
        {
          id: '8',
          label: '算法组件6',
          parentId: '5',
          renderKey: DND_RENDER_ID,
        },
      ],
    },
    {
      id: '模型训练',
      header: '模型训练',
      children: [
        {
          id: '10',
          label: '算法组件7',
          parentId: '9',
          renderKey: DND_RENDER_ID,
          isDisabled: true,
        },
        {
          id: '11',
          label: '算法组件8',
          parentId: '9',
          renderKey: DND_RENDER_ID,
        },
        {
          id: '12',
          label: '算法组件9',
          parentId: '9',
          renderKey: DND_RENDER_ID,
        },
      ],
    },
  ];
};

export const searchService: NsNodeCollapsePanel.ISearchService = async (
  nodes: NsNodeCollapsePanel.IPanelNode[] = [],
  keyword: string,
) => {
  const list = nodes.filter((node) => node.label?.includes(keyword));
  return list;
};
