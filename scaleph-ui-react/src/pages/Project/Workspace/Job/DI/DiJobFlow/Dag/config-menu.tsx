import {
  createCtxMenuConfig,
  MenuItemType,
  NsEdgeCmd,
  NsGraph,
  XFlowEdgeCommands,
  XFlowNodeCommands,
} from '@antv/xflow';
import { getIntl, getLocale } from 'umi';
import { CustomCommands } from './constant';

/**
 * menu config
 */
export const useMenuConfig = createCtxMenuConfig((config) => {
  const intl = getIntl(getLocale(), true);
  config.setMenuModelService(async (target, model, modelService, toDispose) => {
    switch (target?.type) {
      /** 节点菜单 */
      case 'node':
        model.setValue({
          id: 'root',
          type: MenuItemType.Root,
          submenu: [
            {
              id: XFlowNodeCommands.UPDATE_NODE.id,
              type: MenuItemType.Submenu,
              label: intl.formatMessage({ id: 'app.common.operate.edit.label' }),
              iconName: 'EditOutlined',
              onClick: async ({ target, commandService }) => {
                commandService.executeCommand(CustomCommands.NODE_EDIT.id, {
                  nodeConfig: target,
                });
              },
            },
            {
              id: XFlowNodeCommands.DEL_NODE.id,
              type: MenuItemType.Submenu,
              label: intl.formatMessage({ id: 'app.common.operate.delete.label' }),
              iconName: 'DeleteOutlined',
              onClick: async ({ target, commandService }) => {
                commandService.executeCommand(XFlowNodeCommands.DEL_NODE.id, {
                  nodeConfig: { id: target.data?.id + '' },
                });
              },
            },
          ],
        });
        break;
      /** 边菜单 */
      case 'edge':
        model.setValue({
          id: 'root',
          type: MenuItemType.Root,
          submenu: [
            {
              id: XFlowEdgeCommands.DEL_EDGE.id,
              label: intl.formatMessage({ id: 'app.common.operate.delete.label' }),
              iconName: 'DeleteOutlined',
              onClick: async ({ target, commandService }) => {
                commandService.executeCommand<NsEdgeCmd.DelEdge.IArgs>(
                  XFlowEdgeCommands.DEL_EDGE.id,
                  {
                    edgeConfig: target.data as NsGraph.IEdgeConfig,
                  },
                );
              },
            },
          ],
        });
        break;
      /** 画布菜单 */
      case 'blank':
        model.setValue({
          id: 'root',
          type: MenuItemType.Root,
          submenu: [
            {
              id: 'job_params_conf',
              type: MenuItemType.Submenu,
              label: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
              iconName: 'ProfileOutlined',
              onClick: async ({ target, commandService }) => {
                commandService.executeCommand(CustomCommands.GRAPH_PARAMS_SETTING.id, {});
              },
            },
          ],
        });
        break;
      /** 默认菜单 */
      default:
        model.setValue({
          id: 'root',
          type: MenuItemType.Root,
          submenu: [],
        });
        break;
    }
  });
});
