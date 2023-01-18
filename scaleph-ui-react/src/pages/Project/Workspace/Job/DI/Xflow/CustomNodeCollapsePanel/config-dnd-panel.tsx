import {
  NsNodeCollapsePanel,
  NsNodeCmd,
  uuidv4,
  XFlowNodeCommands,
  IFlowchartGraphProps,
} from '@antv/xflow';
import Nodes from './Nodes';
import getPorts from './ports';

export const onNodeDrop: NsNodeCollapsePanel.IOnNodeDrop = async (
  nodeConfig,
  commandService,
) => {
  const args: NsNodeCmd.AddNode.IArgs = {
    nodeConfig: { ...nodeConfig, id: uuidv4(), ports: getPorts() },
  };
  commandService.executeCommand<NsNodeCmd.AddNode.IArgs>(
    XFlowNodeCommands.ADD_NODE.id,
    args,
  );
};

const renderNode = (
  props: {
    data: NsNodeCollapsePanel.IPanelNode;
    isNodePanel: boolean;
  },
  style: { width: number; height: number },
) => {
  const Node = Nodes[props.data.renderKey!];
  return Node ? <Node {...props} style={style} /> : null;
};

const nodeList = (arr: any[]) => {
  const newArr = arr.map((s) => {
    const attrs = s.attrs;
    return {
      popoverContent: () => <div>{s.label}</div>,
      renderKey: s.renderKey || 'CustomNode',
      renderComponent: (props: any) => renderNode(props, attrs.style),
      label: s.label,
      id: s.id,
      attrs: attrs,
      ...attrs.canvansStyle,
    };
  });
  return newArr;
};

export const nodeDataService: NsNodeCollapsePanel.INodeDataService =
  async () => {
    // 这里可以通过接口获取节点列表
    const resData = [
      {
        id: 1,
        renderKey: 'CustomNode',
        label: '开始',
        attrs: {
          attribute: {
            type: 'aaa',
            tag: [1, 2],
          },
          style: {
            width: 280,
            height: 40,
          },
          canvansStyle: {
            width: 120,
            height: 40,
          },
        },
      },
      {
        id: 2,
        renderKey: 'CustomConnecto',
        label: '审核节点',
        attrs: {
          attribute: {
            type: 'bbb',
            tag: [2],
          },
          style: {
            width: 80,
            height: 80,
          },
          canvansStyle: {
            width: 80,
            height: 80,
          },
        },
      },
    ];
    return [
      {
        id: 'NODE',
        header: '节点',
        children: nodeList(resData),
      },
    ];
  };

export const useGraphConfig: IFlowchartGraphProps['useConfig'] = (config) => {
  Object.keys(Nodes).map((key) => {
    config.setNodeRender(key, (props) => {
      return renderNode({ data: props.data, isNodePanel: false }, props.size);
    });
  });
  // config.setDefaultNodeRender((props) => {
  //   return <Nodes.DefaultNode {...props} />;
  // });
};

export const searchService: NsNodeCollapsePanel.ISearchService = async (
  nodes: NsNodeCollapsePanel.IPanelNode[] = [],
  keyword: string,
) => {
  const list = nodes.filter((node) => node?.label?.includes(keyword));
  return list;
};
