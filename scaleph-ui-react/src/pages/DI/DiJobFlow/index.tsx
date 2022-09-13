import { DiJob } from '@/services/project/typings';
import {
  CloseOutlined,
  CompressOutlined,
  DeleteOutlined,
  EditOutlined,
  FullscreenExitOutlined,
  FullscreenOutlined,
  PlaySquareOutlined,
  ProfileOutlined,
  SaveOutlined,
  SendOutlined,
  StopOutlined,
  ZoomInOutlined,
  ZoomOutOutlined,
} from '@ant-design/icons';
import {
  CanvasContextMenu,
  CanvasMiniMap,
  CanvasNodePortTooltip,
  CanvasSnapline,
  CanvasToolbar,
  createCtxMenuConfig,
  createToolbarConfig,
  IApplication,
  IAppLoad,
  IconStore,
  IModelService,
  IToolbarGroupOptions,
  KeyBindings,
  MenuItemType,
  MODELS,
  NodeCollapsePanel,
  NsGraphCmd,
  XFlow,
  XFlowCanvas,
  XFlowEdgeCommands,
  XFlowGraphCommands,
  XFlowNodeCommands,
} from '@antv/xflow';
import { Button, Drawer, message, Popover, Space, Tag, Tooltip } from 'antd';
import React from 'react';
import { useAccess, useIntl } from 'umi';
/** config graph */
import { useGraphCOnfig, useGraphHookConfig } from './Dag/config-graph';
/** config command */
import { initGraphCmds, useCmdConfig } from './Dag/config-cmd';
/** config key bind */
import { useKeybindingConfig } from './Dag/config-keybinding';
/** 配置Model */
// import { useModelServiceConfig } from './Dag/config-model-service';
/** config dnd panel */
import '@antv/xflow/dist/index.css';
import * as dndPanelConfig from './Dag/config-dnd-panel';
import './index.less';
import { ZOOM_OPTIONS } from './Dag/constant';
interface DiJobFlowPorps {
  visible: boolean;
  data: DiJob;
  onVisibleChange: (visible: boolean, data: any) => void;
  onCancel: () => void;
  meta: { flowId?: string };
}

const DiJobFlow: React.FC<DiJobFlowPorps> = (props) => {
  const intl = useIntl();
  const access = useAccess();
  const { visible, data, onVisibleChange, onCancel, meta } = props;

  const graphConfig = useGraphCOnfig(props);
  const graphHooksConfig = useGraphHookConfig(props);
  const cmdConfig = useCmdConfig();
  // const modelServiceConfig = useModelServiceConfig();
  const keybindingConfig = useKeybindingConfig();

  /**register icons */
  IconStore.set('DeleteOutlined', DeleteOutlined);
  IconStore.set('EditOutlined', EditOutlined);
  IconStore.set('PlaySquareOutlined', PlaySquareOutlined);
  IconStore.set('StopOutlined', StopOutlined);
  IconStore.set('SaveOutlined', SaveOutlined);
  IconStore.set('SendOutlined', SendOutlined);
  IconStore.set('ProfileOutlined', ProfileOutlined);
  IconStore.set('FullscreenOutlined', FullscreenOutlined);
  IconStore.set('FullscreenExitOutlined', FullscreenExitOutlined);
  IconStore.set('ZoomInOutlined', ZoomInOutlined);
  IconStore.set('ZoomOutOutlined', ZoomOutOutlined);
  IconStore.set('CompressOutlined', CompressOutlined);

  const cache = React.useMemo<{ app: IApplication | null }>(
    () => ({
      app: null,
    }),
    [],
  );

  const onLoad: IAppLoad = async (app) => {
    cache.app = app;
    initGraphCmds(cache.app);
  };

  React.useEffect(() => {
    if (cache.app) {
      initGraphCmds(cache.app);
    }
  }, [cache.app, meta]);

  /**
   * menu config
   */
  const menuConfig = createCtxMenuConfig((config) => {
    config.setMenuModelService(async (target, model, modelService, toDispose) => {
      switch (target?.type) {
        /** 节点菜单 */
        case 'node':
          model.setValue({
            id: 'root',
            type: MenuItemType.Root,
            submenu: [
              // {
              //   id: CustomCommands.SHOW_RENAME_MODAL.id,
              //   type: MenuItemType.Submenu,
              //   label: intl.formatMessage({ id: 'app.common.operate.edit.label' }),
              //   isVisible: true,
              //   iconName: 'EditOutlined',
              //   onClick: async ({ target, commandService }) => {
              //     // const nodeConfig = target.data
              //     // commandService.executeCommand(CustomCommands.SHOW_RENAME_MODAL.id, {
              //     //   nodeConfig,
              //     //   updateNodeNameService: MockApi.renameNode,
              //     // })
              //   },
              // },
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
                type: MenuItemType.Submenu,
                label: intl.formatMessage({ id: 'app.common.operate.delete.label' }),
                iconName: 'DeleteOutlined',
                onClick: async ({ target, commandService }) => {
                  commandService.executeCommand(XFlowEdgeCommands.DEL_EDGE.id, {
                    edgeConfig: target.data,
                  });
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
                id: XFlowEdgeCommands.DEL_EDGE.id,
                type: MenuItemType.Submenu,
                label: intl.formatMessage({ id: 'app.common.operate.delete.label' }),
                iconName: 'DeleteOutlined',
                onClick: async ({ target, commandService }) => {
                  //todo config job info
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
  /**
   * toolbar config
   * todo disable stop button when job is not running
   */
  const getMainToolbarConfig = () => {
    return [
      {
        name: 'main',
        items: [
          {
            id: 'main01',
            iconName: 'PlaySquareOutlined',
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.start' }),
            onClick: (args) => {},
          },
          {
            id: 'main02',
            iconName: 'StopOutlined',
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.stop' }),
            onClick: (args) => {},
          },
        ],
      },
      {
        name: 'main',
        items: [
          {
            id: 'main03',
            iconName: 'SaveOutlined',
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.save' }),
            onClick: (args) => {},
          },
          {
            id: 'main04',
            iconName: 'SendOutlined',
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.publish' }),
            onClick: (args) => {},
          },
        ],
      },
      {
        name: 'main',
        items: [],
      },
    ] as IToolbarGroupOptions[];
  };
  const getExtraToolbarConfig = () => {
    return [
      {
        name: 'extra',
        items: [
          {
            id: 'extra01',
            iconName: 'ProfileOutlined',
            text: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
            onClick: ({ commandService }) => {},
          },
        ],
      },
    ] as IToolbarGroupOptions[];
  };
  const getScaleToolbarConfig = ({
    zoomFactor,
    fullScreen,
  }: {
    zoomFactor?: Number;
    fullScreen?: boolean;
  }) => {
    return [
      {
        name: 'scale',
        items: [
          {
            id: 'scale01',
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.zoomIn' }),
            iconName: 'ZoomInOutlined',
            onClick: ({ commandService, modelService }) => {
              commandService
                .executeCommand<NsGraphCmd.GraphZoom.IArgs>(XFlowGraphCommands.GRAPH_ZOOM.id, {
                  factor: 0.5,
                  zoomOptions: ZOOM_OPTIONS,
                })
                .then(() => {
                  scaleMessage(modelService);
                });
            },
          },
          {
            id: 'scale02',
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.zoomOut' }),
            iconName: 'ZoomOutOutlined',
            onClick: ({ commandService, modelService }) => {
              commandService
                .executeCommand<NsGraphCmd.GraphZoom.IArgs>(XFlowGraphCommands.GRAPH_ZOOM.id, {
                  factor: -0.5,
                  zoomOptions: ZOOM_OPTIONS,
                })
                .then(() => {
                  scaleMessage(modelService);
                });
            },
          },
          {
            id: 'scale03',
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.zoomFit' }),
            iconName: 'CompressOutlined',
            onClick: ({ commandService, modelService }) => {
              commandService
                .executeCommand<NsGraphCmd.GraphZoom.IArgs>(XFlowGraphCommands.GRAPH_ZOOM.id, {
                  factor: 'fit',
                  zoomOptions: ZOOM_OPTIONS,
                })
                .then(() => {
                  scaleMessage(modelService);
                });
            },
          },
          {
            id: 'scale04',
            iconName: !fullScreen ? 'FullscreenOutlined' : 'FullscreenExitOutlined',
            tooltip: !fullScreen
              ? intl.formatMessage({ id: 'pages.project.di.flow.dag.fullScreen' })
              : intl.formatMessage({ id: 'pages.project.di.flow.dag.fullScreenExit' }),
            onClick: ({ commandService }) => {
              commandService.executeCommand<NsGraphCmd.GraphFullscreen.IArgs>(
                XFlowGraphCommands.GRAPH_FULLSCREEN.id,
                {},
              );
            },
          },
        ],
      },
    ] as IToolbarGroupOptions[];
  };
  const scaleMessage = async (modelService: IModelService) => {
    const graphScale = await MODELS.GRAPH_SCALE.useValue(modelService);
    message.info(
      intl.formatMessage(
        { id: 'pages.project.di.flow.dag.zoomTo' },
        { factor: graphScale.zoomFactor },
      ),
    );
  };
  const toolbarConfig = createToolbarConfig((toolbarConfig) => {
    /** toolbar item */
    toolbarConfig.setToolbarModelService(async (toolbarModel, modelService, toDispose) => {
      //init toolbar
      toolbarModel.setValue((toolbar) => {
        toolbar.mainGroups = getMainToolbarConfig();
        toolbar.extraGroups = getExtraToolbarConfig();
      });
    });
  });

  const scaleToolbarConfig = createToolbarConfig((toolbarConfig) => {
    /** toolbar item */
    toolbarConfig.setToolbarModelService(async (toolbarModel, modelService, toDispose) => {
      const graphScale = await MODELS.GRAPH_SCALE.useValue(modelService);
      //init toolbar
      toolbarModel.setValue((toolbar) => {
        toolbar.mainGroups = getScaleToolbarConfig({
          zoomFactor: graphScale.zoomFactor,
          fullScreen: false,
        });
      });
      // fullscreen
      const graphFullScreenModel = await MODELS.GRAPH_FULLSCREEN.getModel(modelService);
      graphFullScreenModel.setValue(false);
      graphFullScreenModel.watch((fullScreen) => {
        toolbarModel.setValue((toolbar) => {
          toolbar.mainGroups = getScaleToolbarConfig({
            zoomFactor: graphScale.zoomFactor,
            fullScreen,
          });
        });
      });
      // graph scale
      const graphScaleModel = await MODELS.GRAPH_SCALE.getModel(modelService);
      graphScaleModel.watch(async ({ zoomFactor }) => {
        const fullScreen = await MODELS.GRAPH_FULLSCREEN.useValue(modelService);
        toolbarModel.setValue((toolbar) => {
          toolbar.mainGroups = getScaleToolbarConfig({ zoomFactor: zoomFactor, fullScreen });
        });
      });
    });
  });

  return (
    <>
      <Drawer
        title={
          <Space>
            <Popover
              content={
                <>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.jobName' }) + ' : ' + data.jobName}
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
          isAutoCenter={true}
          hookConfig={graphHooksConfig}
          // modelServiceConfig={modelServiceConfig}
          commandConfig={cmdConfig}
          onLoad={onLoad}
          meta={meta}
        >
          <NodeCollapsePanel
            className="xflow-node-panel"
            position={{ width: 240, top: 0, bottom: 0, left: 0 }}
            bodyPosition={{ top: 40, bottom: 0, left: 0 }}
            footerPosition={{ height: 0 }}
            nodeDataService={dndPanelConfig.nodeDataService}
            onNodeDrop={dndPanelConfig.onNodeDrop}
            searchService={dndPanelConfig.searchService}
          ></NodeCollapsePanel>
          <CanvasToolbar
            className="xflow-workspace-toolbar-top"
            layout="horizontal"
            config={toolbarConfig()}
            position={{ top: 0, left: 240, right: 0, bottom: 0 }}
          />
          <XFlowCanvas config={graphConfig} position={{ top: 40, left: 240, right: 0, bottom: 0 }}>
            <CanvasToolbar
              position={{ top: 12, right: 12 }}
              config={scaleToolbarConfig()}
              layout="vertical"
            />
            <CanvasContextMenu config={menuConfig()} />
            <CanvasSnapline color="#faad14" />
            <CanvasMiniMap minimapOptions={{ width: 200, height: 120 }} />
            <CanvasNodePortTooltip />
          </XFlowCanvas>
          <KeyBindings config={keybindingConfig} />
        </XFlow>
      </Drawer>
    </>
  );
};

export default DiJobFlow;
