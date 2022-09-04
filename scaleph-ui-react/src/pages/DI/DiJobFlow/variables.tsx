import { SaveOutlined } from '@ant-design/icons';
import {
  createGraphConfig,
  createToolbarConfig,
  IconStore,
  IToolbarItemOptions,
  NsGraphCmd,
  XFlowGraphCommands,
} from '@antv/xflow';
import { message } from 'antd';
import { useIntl } from 'umi';

export const graphLayout: NsGraphCmd.GraphLayout.IArgs = {
  layoutType: 'dagre',
  layoutOptions: {
    type: 'dagre',
    rankdir: 'TB', // from top to bottom
    nodesep: 50,
    ranksep: 50,
  },
};

export const configGraph = createGraphConfig((config) => {
  config.setX6Config({
    grid: {
      size: 10,
      visible: true,
      type: 'doubleMesh',
      args: [
        {
          color: '#E7E8EA',
          thickness: 1,
        },
        {
          color: '#CBCED3',
          thickness: 1,
          factor: 4,
        },
      ],
    },
    history: { enabled: true },
    background: { color: '#F8F8FA' },
    snapline: {
      enabled: true,
      sharp: true,
    },
    clipboard: {
      enabled: true,
    },
    keyboard: {
      enabled: true,
    },
    selecting: {
      enabled: true,
      rubberband: true,
      multiple: true,
      movable: true,
      strict: true,
      showNodeSelectionBox: true,
      showEdgeSelectionBox: true,
    },
    scaling: {
      min: 0.2,
      max: 1,
    },
    panning: {
      enabled: true,
      modifiers: 'shift',
    },
  });
});

export const configToolbar = createToolbarConfig((config) => {
  const intl = useIntl();
  IconStore.set('SaveOutlined', SaveOutlined);
  const toolbarGroup: IToolbarItemOptions[] = [
    {
      id: XFlowGraphCommands.SAVE_GRAPH_DATA.id,
      iconName: 'SaveOutlined',
      tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.save' }),
      onClick: async ({ commandService }) => {
        commandService.executeCommand<NsGraphCmd.SaveGraphData.IArgs>(
          XFlowGraphCommands.SAVE_GRAPH_DATA.id,
          {
            saveGraphDataService: async (meta, data) => {
              console.log(data);
              message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
            },
          },
        );
      },
    },
  ];
  config.setToolbarModelService(async (toolbarModel) => {
    const toolbarItems = [{ name: 'actionGropu', items: toolbarGroup }];
    toolbarModel.setValue((toolbar) => {
      toolbar.mainGroups = toolbarItems;
    });
  });
});
