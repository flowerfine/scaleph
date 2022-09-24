import { NsNodeCmd, NsNodeCollapsePanel, uuidv4, XFlowNodeCommands } from '@antv/xflow';
import { DagService } from './service';

export const onNodeDrop: NsNodeCollapsePanel.IOnNodeDrop = async (
  nodeConfig,
  commandService,
  modelService,
) => {
  const args: NsNodeCmd.AddNode.IArgs = {
    nodeConfig: { ...nodeConfig, id: uuidv4() },
  };
  commandService.executeCommand(XFlowNodeCommands.ADD_NODE.id, args);
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
