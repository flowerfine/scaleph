import { DiJob } from '@/services/project/typings';
import { CloseOutlined, CloudSyncOutlined, DeleteOutlined, EditOutlined, PlaySquareOutlined, ProfileOutlined, SaveOutlined, SendOutlined, StopOutlined } from '@ant-design/icons';
import { Button, Drawer, Popover, Space, Tag, Tooltip } from 'antd';
import { useAccess, useIntl } from 'umi';
import React from 'react'
/** app 核心组件 */
import { XFlow, XFlowCanvas, KeyBindings, IApplication, IAppLoad, createCtxMenuConfig, MenuItemType, XFlowNodeCommands, XFlowEdgeCommands, IconStore, createToolbarConfig, createKeybindingConfig, XFlowGraphCommands } from '@antv/xflow'
/** 交互组件 */
import {
  /** 触发Command的交互组件 */
  CanvasScaleToolbar,
  NodeCollapsePanel,
  CanvasContextMenu,
  CanvasToolbar,
  /** Graph的扩展交互组件 */
  CanvasSnapline,
  CanvasMiniMap,
  CanvasNodePortTooltip,
  DagGraphExtension,
} from '@antv/xflow'

/** app 组件配置  */
/** 配置画布 */
import { useGraphHookConfig } from './Dag/config-graph'
/** 配置Command */
import { useCmdConfig, initGraphCmds } from './Dag/config-cmd'
/** 配置Model */
import { useModelServiceConfig } from './Dag/config-model-service'
/** 配置快捷键 */
import { useKeybindingConfig } from './Dag/config-keybinding'
/** 配置Dnd组件面板 */
import * as dndPanelConfig from './Dag/config-dnd-panel'
/** 配置JsonConfigForm */

import '@antv/xflow/dist/index.css'

import './index.less';
import { CustomCommands } from './Dag/cmd-extensions/constants';
interface DiJobFlowPorps {
  visible: boolean;
  data: DiJob;
  onVisibleChange: (visible: boolean, data: any) => void;
  onCancel: () => void;
  meta: { flowId?: string; }
}

const DiJobFlow: React.FC<DiJobFlowPorps> = props => {
  const intl = useIntl();
  const access = useAccess();
  const { visible, data, onVisibleChange, onCancel, meta } = props;

  const graphHooksConfig = useGraphHookConfig(props)
  // const toolbarConfig = useToolbarConfig()
  // const menuConfig = useMenuConfig()
  const cmdConfig = useCmdConfig()
  const modelServiceConfig = useModelServiceConfig()
  // const keybindingConfig = useKeybindingConfig()

  /**register icons */
  IconStore.set('DeleteOutlined', DeleteOutlined);
  IconStore.set('EditOutlined', EditOutlined);
  IconStore.set('PlaySquareOutlined', PlaySquareOutlined);
  IconStore.set('StopOutlined', StopOutlined);
  IconStore.set('SaveOutlined', SaveOutlined);
  IconStore.set('SendOutlined', SendOutlined);
  IconStore.set('ProfileOutlined', ProfileOutlined);
  IconStore.set('CloudSyncOutlined', CloudSyncOutlined);

  const cache = React.useMemo<{ app: IApplication | null }>(
    () => ({
      app: null,
    }),
    [],
  )

  const onLoad: IAppLoad = async app => {
    cache.app = app
    initGraphCmds(cache.app)
  }

  React.useEffect(() => {
    if (cache.app) {
      initGraphCmds(cache.app)
    }
  }, [cache.app, meta])


  /**
   * menu config 
   */
  const menuConfig = createCtxMenuConfig(config => {
    config.setMenuModelService(async (target, model, modelService, toDispose) => {
      switch (target?.type) {
        /** 节点菜单 */
        case 'node':
          model.setValue({
            id: 'root',
            type: MenuItemType.Root,
            submenu: [
              {
                id: CustomCommands.SHOW_RENAME_MODAL.id,
                type: MenuItemType.Submenu,
                label: intl.formatMessage({ id: 'app.common.operate.edit.label' }),
                isVisible: true,
                iconName: 'EditOutlined',
                onClick: async ({ target, commandService }) => {
                  console.log('edit:', target, commandService);
                  // const nodeConfig = target.data
                  // commandService.executeCommand(CustomCommands.SHOW_RENAME_MODAL.id, {
                  //   nodeConfig,
                  //   updateNodeNameService: MockApi.renameNode,
                  // })
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
              // {
              //   id: CustomCommands.SHOW_RENAME_MODAL.id,
              //   label: '重命名',
              //   isVisible: true,
              //   iconName: 'EditOutlined',
              //   onClick: async ({ target, commandService }) => {
              //     const nodeConfig = target.data
              //     commandService.executeCommand(CustomCommands.SHOW_RENAME_MODAL.id, {
              //       nodeConfig,
              //       updateNodeNameService: MockApi.renameNode,
              //     })
              //   },
              // }
            ],
          })
          break;
        /** 边菜单 */
        case 'edge':
          model.setValue({
            id: 'root',
            type: MenuItemType.Root,
            submenu: [{
              id: XFlowEdgeCommands.DEL_EDGE.id,
              type: MenuItemType.Submenu,
              label: intl.formatMessage({ id: 'app.common.operate.delete.label' }),
              iconName: 'DeleteOutlined',
              onClick: async ({ target, commandService }) => {
                commandService.executeCommand(XFlowEdgeCommands.DEL_EDGE.id, {
                  edgeConfig: target.data,
                })
              },
            }],
          })
          break;
        /** 画布菜单 */
        case 'blank':
          model.setValue({
            id: 'root',
            type: MenuItemType.Root,
            submenu: [{
              id: XFlowEdgeCommands.DEL_EDGE.id,
              type: MenuItemType.Submenu,
              label: intl.formatMessage({ id: 'app.common.operate.delete.label' }),
              iconName: 'DeleteOutlined',
              onClick: async ({ target, commandService }) => {
                //todo config job info 
              },
            }],
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
    })
  });
  /**
   * toolbar config
   * todo disable stop button when job is not running
   */
  const toolbarConfig = createToolbarConfig(toolbarConfig => {
    /** toolbar item */
    toolbarConfig.setToolbarModelService(async (toolbarModel, modelService, toDispose) => {
      toolbarModel.setValue(toolbar => {
        toolbar.mainGroups = [
          {
            name: 'main',
            items: [
              {
                id: 'main01',
                iconName: 'PlaySquareOutlined',
                tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.start' }),
                onClick: (args) => {
                  console.log('start job');
                }
              },
              {
                id: 'main02',
                iconName: 'StopOutlined',
                tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.stop' }),
                onClick: (args) => {
                  console.log('stop job');
                }
              }
            ]
          },
          {
            name: 'main',
            items: [
              {
                id: 'main03',
                iconName: 'SaveOutlined',
                tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.save' }),
                onClick: (args) => {
                  console.log('save job');
                }
              },
              {
                id: 'main04',
                iconName: 'SendOutlined',
                tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.publish' }),
                onClick: (args) => {
                  console.log('publish job');
                }
              }
            ]
          },
        ];
        toolbar.extraGroups = [
          {
            name: 'extra',
            items: [
              {
                id: 'extra01',
                iconName: 'ProfileOutlined',
                text: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
                tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
                onClick: (args) => {
                  console.log(args);
                },
              }
            ]
          }
        ]
      });
    })
  })

  /**
   * key bind config
   */
  const keybindingConfig = createKeybindingConfig(config => {
    config.setKeybindingFunc(register => {
      return register.registerKeybinding([
        {
          id: 'delete',
          keybinding: ['delete', 'backspace'],
          callback: async function (item, modeService, cmd, e) {
            e.preventDefault();
            console.log('item:', item);
            console.log('modeService:', modeService);
            console.log('cmd:', cmd);
            console.log('e:', e);
          }
        },
        {
          id: 'copy',
          keybinding: ['command+c', 'ctrl+c'],
          callback: async function (item, modeService, cmd, e) {
            e.preventDefault();
            cmd.executeCommand(XFlowGraphCommands.GRAPH_COPY.id, {});
          }
        },
        {
          id: 'cut',
          keybinding: ['command+x', 'ctrl+x'],
          callback: async function (item, modeService, cmd, e) {
            e.preventDefault();
            // cmd.executeCommand(XFlowGraphCommands.GRAPH_.id, {});
          }
        },
        {
          id: 'paste',
          keybinding: ['command+v', 'ctrl+v'],
          callback: async function (item, modeService, cmd, e) {
            e.preventDefault();
            cmd.executeCommand(XFlowGraphCommands.GRAPH_PASTE.id, {});
          }
        },
        {
          id: 'redo',
          keybinding: ['command+y', 'ctrl+y'],
          callback: async function (item, modeService, cmd, e) {
            e.preventDefault();
            console.log('redo');
            cmd.executeCommand(XFlowGraphCommands.GRAPH_HISTORY_REDO.id, {});
          }
        },
        {
          id: 'undo',
          keybinding: ['command+z', 'ctrl+z'],
          callback: async function (item, modeService, cmd, e) {
            e.preventDefault();
            console.log('undo');
            cmd.executeCommand(XFlowGraphCommands.GRAPH_HISTORY_UNDO.id, {});
          }
        },
      ]);
    })
  })

  return (
    <>
      <Drawer
        title={
          <Space>
            <Popover
              content={
                <>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.jobName' }) +
                      ' : ' +
                      data.jobName}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.jobStatus' }) +
                      ' : ' +
                      data.jobStatus?.label}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.jobVersion' }) +
                      ' : ' +
                      data.jobVersion}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.createTime' }) +
                      ' : ' +
                      data.createTime}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.updateTime' }) +
                      ' : ' +
                      data.updateTime}
                  </p>
                </>
              }
              title={false}
              placement="bottom"
              trigger="hover"
            >
              <Tag color="blue">
                {intl.formatMessage({ id: 'pages.project.di.job.batch' }) + ' : ' + data.jobCode}
              </Tag>
            </Popover>
          </Space>
        }
        bodyStyle={{ padding: '0px' }}
        placement="top"
        width="100%"
        height="100%"
        closable={false}
        onClose={onCancel}
        extra={
          <Space>
            <Tooltip title={intl.formatMessage({ id: 'app.common.operate.close.label' })}></Tooltip>
            <Button
              shape="default"
              type="text"
              icon={<CloseOutlined />}
              onClick={onCancel}
            ></Button>
          </Space>
        }
        visible={visible}
      >
        <XFlow
          className="dag-user-custom-clz"
          hookConfig={graphHooksConfig}
          modelServiceConfig={modelServiceConfig}
          commandConfig={cmdConfig}
          onLoad={onLoad}
          meta={meta}
        >
          <DagGraphExtension />
          <NodeCollapsePanel
            className="xflow-node-panel"
            searchService={dndPanelConfig.searchService}
            nodeDataService={dndPanelConfig.nodeDataService}
            onNodeDrop={dndPanelConfig.onNodeDrop}
            position={{ width: 240, top: 0, bottom: 0, left: 0 }}
            footerPosition={{ height: 0 }}
            bodyPosition={{ top: 40, bottom: 0, left: 0 }}
          />
          <CanvasToolbar
            className="xflow-workspace-toolbar-top"
            layout="horizontal"
            config={toolbarConfig()}
            position={{ top: 0, left: 240, right: 0, bottom: 0 }}
          />
          <XFlowCanvas position={{ top: 40, left: 240, right: 0, bottom: 0 }}>
            <CanvasScaleToolbar position={{ top: 12, right: 12 }} />
            <CanvasContextMenu config={menuConfig()} />
            <CanvasSnapline color="#faad14" />
            <CanvasMiniMap minimapOptions={{ width: 200, height: 120 }} />
            <CanvasNodePortTooltip />
          </XFlowCanvas>
          <KeyBindings config={keybindingConfig()} />
        </XFlow>
      </Drawer>
    </>
  );
};

export default DiJobFlow;