import {
  createKeybindingConfig,
  MODELS,
  XFlowEdgeCommands,
  XFlowGraphCommands,
  XFlowNodeCommands,
} from '@antv/xflow';
import { CustomCommands } from './constant';

export const useKeybindingConfig = createKeybindingConfig((config) => {
  config.setKeybindingFunc((register) => {
    return register.registerKeybinding([
      {
        id: 'delete',
        keybinding: ['delete', 'backspace'],
        callback: async function (item, modelService, cmd, e) {
          e.preventDefault();
          const cells = await MODELS.SELECTED_CELLS.useValue(modelService);
          await Promise.all(
            cells.map((cell) => {
              if (cell.isEdge()) {
                return cmd.executeCommand(XFlowEdgeCommands.DEL_EDGE.id, {
                  edgeConfig: { ...cell.getData(), id: cell.id },
                });
              } else if (cell.isNode()) {
                return cmd.executeCommand(XFlowNodeCommands.DEL_NODE.id, {
                  nodeConfig: { ...cell.getData(), id: cell.id },
                });
              } else {
                return null;
              }
            }),
          );
        },
      },
      {
        id: 'copy',
        keybinding: ['command+c', 'ctrl+c'],
        callback: async function (item, modelService, cmd, e) {
          e.preventDefault();
          cmd.executeCommand(XFlowGraphCommands.GRAPH_COPY.id, {});
        },
      },
      {
        id: 'cut',
        keybinding: ['command+x', 'ctrl+x'],
        callback: async function (item, modelService, cmd, e) {
          e.preventDefault();
          cmd.executeCommand(CustomCommands.GRAPH_CUT.id, {});
        },
      },
      {
        id: 'paste',
        keybinding: ['command+v', 'ctrl+v'],
        callback: async function (item, modelService, cmd, e) {
          e.preventDefault();
          cmd.executeCommand(XFlowGraphCommands.GRAPH_PASTE.id, {});
        },
      },
      {
        id: 'redo',
        keybinding: ['command+y', 'ctrl+y'],
        callback: async function (item, modelService, cmd, e) {
          e.preventDefault();
          cmd.executeCommand(XFlowGraphCommands.GRAPH_HISTORY_REDO.id, {});
        },
      },
      {
        id: 'undo',
        keybinding: ['command+z', 'ctrl+z'],
        callback: async function (item, modelService, cmd, e) {
          e.preventDefault();
          cmd.executeCommand(XFlowGraphCommands.GRAPH_HISTORY_UNDO.id, {});
        },
      },
    ]);
  });
});
