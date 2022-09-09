import { DiJob } from '@/services/project/typings';
import {
  CloseOutlined,
  CloudSyncOutlined,
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
import { Button, Drawer, message, Popover, Select, Space, Tag, Tooltip } from 'antd';
import React from 'react';
import { useAccess, useIntl } from 'umi';
/** app 核心组件 */
import {
  createCtxMenuConfig,
  createGraphConfig,
  createKeybindingConfig,
  createToolbarConfig,
  IApplication,
  IAppLoad,
  IconStore,
  IModelService,
  IToolbarGroupOptions,
  KeyBindings,
  MenuItemType,
  MODELS,
  NsGraphCmd,
  XFlow,
  XFlowCanvas,
  XFlowEdgeCommands,
  XFlowGraphCommands,
  XFlowNodeCommands,
} from '@antv/xflow';
/** 交互组件 */
import {
  CanvasContextMenu,
  CanvasMiniMap,
  CanvasNodePortTooltip,
  CanvasScaleToolbar,
  /** Graph的扩展交互组件 */
  CanvasSnapline,
  CanvasToolbar,
  DagGraphExtension,
  NodeCollapsePanel,
} from '@antv/xflow';

/** app 组件配置  */
/** 配置画布 */
import { useGraphHookConfig } from './Dag/config-graph';
/** 配置Command */
import { initGraphCmds, useCmdConfig } from './Dag/config-cmd';
/** 配置Model */
import { useModelServiceConfig } from './Dag/config-model-service';
/** 配置Dnd组件面板 */
import * as dndPanelConfig from './Dag/config-dnd-panel';
/** 配置JsonConfigForm */

import '@antv/xflow/dist/index.css';

import { CustomCommands } from './Dag/cmd-extensions/constants';
import './index.less';
import { layout } from '@/app';
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

  const graphHooksConfig = useGraphHookConfig(props);
  // const toolbarConfig = useToolbarConfig()
  // const menuConfig = useMenuConfig()
  const cmdConfig = useCmdConfig();
  const modelServiceConfig = useModelServiceConfig();
  // const keybindingConfig = useKeybindingConfig()
  const zoomOptions ={   maxScale: 2,
    minScale: 0.5,}
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
  const datag = {
    // 节点
    nodes: [
      {
        id: 'node1', // String，可选，节点的唯一标识
        x: 40,       // Number，必选，节点位置的 x 值
        y: 40,       // Number，必选，节点位置的 y 值
        width: 80,   // Number，可选，节点大小的 width 值
        height: 40,  // Number，可选，节点大小的 height 值
        label: 'hello', // String，节点标签
      },
      {
        id: 'node2', // String，节点的唯一标识
        x: 160,      // Number，必选，节点位置的 x 值
        y: 180,      // Number，必选，节点位置的 y 值
        width: 80,   // Number，可选，节点大小的 width 值
        height: 40,  // Number，可选，节点大小的 height 值
        label: 'world', // String，节点标签
      },
    ],
    // 边
    edges: [
      {
        id:'edge1',
        source: 'node1', // String，必须，起始节点 id
        target: 'node2', // String，必须，目标节点 id
      },
    ],
  };

  /**graph config */
  const graphConfig = createGraphConfig((config) => {
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
      scaling: { min: zoomOptions.minScale, max: zoomOptions.maxScale },
      mousewheel: { enabled: true, zoomAtMousePosition: true },
      history: {
        enabled: true,
      },
      background: {
        color: '#F8F8FA', // 设置画布背景颜色
      },
      clipboard: {
        enabled: true,
      },
      panning: {
        enabled: true,
        modifiers: 'shift',
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
    });
  });
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
              {
                id: CustomCommands.SHOW_RENAME_MODAL.id,
                type: MenuItemType.Submenu,
                label: intl.formatMessage({ id: 'app.common.operate.edit.label' }),
                isVisible: true,
                iconName: 'EditOutlined',
                onClick: async ({ target, commandService }) => {
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
  const getMainToolbarConfig = ()=>{
    return [  {
      name: 'main',
      items: [
        {
          id: 'main01',
          iconName: 'PlaySquareOutlined',
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.start' }),
          onClick: (args) => {
          },
        },
        {
          id: 'main02',
          iconName: 'StopOutlined',
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.stop' }),
          onClick: (args) => {
          },
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
          onClick: (args) => {
          },
        },
        {
          id: 'main04',
          iconName: 'SendOutlined',
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.publish' }),
          onClick: (args) => {
          },
        },
      ],
    },
    {
      name: 'main',
      items: [
        
      ],
    }] as IToolbarGroupOptions[];
  };
  const getExtraToolbarConfig = ()=>{
    return [
      {
        name: 'extra',
        items: [
          {
            id: 'extra01',
            iconName: 'ProfileOutlined',
            text:intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
            tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
            onClick: ({commandService}) => {
            },
          },
        ],
      },
    ] as IToolbarGroupOptions[];
  }

  const getScaleToolbarConfig = ({zoomFactor,fullScreen}:{zoomFactor?:Number,fullScreen?:boolean})=>{
    return [
      {
        name: 'scale',
        items: [
          {
            id:'scale01',
            tooltip:intl.formatMessage({id:'pages.project.di.flow.dag.zoomIn'}),
            iconName: 'ZoomInOutlined',
            onClick: ({ commandService,modelService }) => {
              commandService.executeCommand<NsGraphCmd.GraphZoom.IArgs>(
                XFlowGraphCommands.GRAPH_ZOOM.id,
                {
                  factor: 0.5,
                  zoomOptions: zoomOptions,
                },
              ).then(()=>{
                scaleMessage(modelService);
              });
            },
          },
          {
            id:'scale02',
            tooltip:intl.formatMessage({id:'pages.project.di.flow.dag.zoomOut'}),
            iconName: 'ZoomOutOutlined',
            onClick: ({ commandService,modelService }) => {
              commandService.executeCommand<NsGraphCmd.GraphZoom.IArgs>(
                XFlowGraphCommands.GRAPH_ZOOM.id,
                {
                  factor: -0.5,
                  zoomOptions: zoomOptions,
                },
              ).then(()=>{
                scaleMessage(modelService);
              });
            },
          },
          {
            id:'scale03',
            tooltip:intl.formatMessage({id:'pages.project.di.flow.dag.zoomFit'}),
            iconName: 'CompressOutlined',
            onClick: ({ commandService,modelService }) => {
              commandService.executeCommand<NsGraphCmd.GraphZoom.IArgs>(
                XFlowGraphCommands.GRAPH_ZOOM.id,
                {
                  factor: 'fit',
                  zoomOptions: zoomOptions,
                },
              ).then(()=>{
                scaleMessage(modelService);
              });
             
            },
          },
          {
            id: 'scale04',
            iconName: !fullScreen? 'FullscreenOutlined':'FullscreenExitOutlined',
            tooltip:!fullScreen ? intl.formatMessage({ id: 'pages.project.di.flow.dag.fullScreen' }) :intl.formatMessage({ id: 'pages.project.di.flow.dag.fullScreenExit' }) ,
            onClick: ({commandService}) => {
              commandService.executeCommand<NsGraphCmd.GraphFullscreen.IArgs>(
                XFlowGraphCommands.GRAPH_FULLSCREEN.id,
                {},
              )
            },
          },
        ],
      },
    ] as IToolbarGroupOptions[];
  }

  const scaleMessage = async (modelService:IModelService)=>{
    const graphScale = await MODELS.GRAPH_SCALE.useValue(modelService);
    message.info(intl.formatMessage({id:'pages.project.di.flow.dag.zoomTo'},{factor:graphScale.zoomFactor}));
  }
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
        toolbar.mainGroups =  getScaleToolbarConfig({zoomFactor:graphScale.zoomFactor,fullScreen:false});
      });
      // fullscreen 
      const graphFullScreenModel = await MODELS.GRAPH_FULLSCREEN.getModel(modelService);
      graphFullScreenModel.setValue(false);
      graphFullScreenModel.watch(fullScreen=>{
        toolbarModel.setValue(toolbar=>{
          toolbar.mainGroups= getScaleToolbarConfig({zoomFactor:graphScale.zoomFactor,fullScreen});
        })
      });
      // graph scale 
      const graphScaleModel = await MODELS.GRAPH_SCALE.getModel(modelService);
      graphScaleModel.watch(async ({zoomFactor})=>{
        const fullScreen = await MODELS.GRAPH_FULLSCREEN.useValue(modelService);
        toolbarModel.setValue(toolbar=>{
          toolbar.mainGroups=getScaleToolbarConfig({zoomFactor:zoomFactor,fullScreen});
        })
      })
    });
  });
/**
   * key bind config
   */
  const keybindingConfig = createKeybindingConfig((config) => {
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
            //TODO CUT COMMAND 
            // cmd.executeCommand(XFlowGraphCommands.GRAPH_.id, {});
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
        // hookConfig={graphHooksConfig}
          modelServiceConfig={modelServiceConfig}
          commandConfig={cmdConfig}
          onLoad={onLoad}
          meta={meta}
          graphData={datag}
        >
          {/* <NodeCollapsePanel
            className="xflow-node-panel"
            searchService={dndPanelConfig.searchService}
            nodeDataService={dndPanelConfig.nodeDataService}
            onNodeDrop={dndPanelConfig.onNodeDrop}
            position={{ width: 240, top: 0, bottom: 0, left: 0 }}
            footerPosition={{ height: 0 }}
            bodyPosition={{ top: 40, bottom: 0, left: 0 }}
          /> */}
          <CanvasToolbar
            className="xflow-workspace-toolbar-top"
            layout="horizontal"
            config={toolbarConfig()}
            position={{ top: 0, left: 240, right: 0, bottom: 0 }}
          />
          <XFlowCanvas
            config={graphConfig()}
            position={{ top: 40, left: 240, right: 0, bottom: 0 }}
          >
      <CanvasToolbar position={{ top: 12, right: 12 }} config={scaleToolbarConfig()} layout="vertical" />
      <CanvasContextMenu config={menuConfig()} />
        <CanvasMiniMap minimapOptions={{ width: 200, height: 120 }} />
          </XFlowCanvas>
          <KeyBindings config={keybindingConfig()} />
        </XFlow>
        {/* <XFlow
          className="dag-user-custom-clz"
          hookConfig={graphHooksConfig}
          modelServiceConfig={modelServiceConfig}
          commandConfig={cmdConfig}
          // onLoad={onLoad}
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
          <XFlowCanvas
            position={{ top: 40, left: 240, right: 0, bottom: 0 }}
            config={graphConfig()}
          >
            <CanvasScaleToolbar position={{ top: 12, right: 12 }} />
            <CanvasContextMenu config={menuConfig()} />
            <CanvasSnapline color="#faad14" />
            <CanvasMiniMap minimapOptions={{ width: 200, height: 120 }} />
            <CanvasNodePortTooltip />
          </XFlowCanvas>
          <KeyBindings config={keybindingConfig()} />
        </XFlow> */}
      </Drawer>
    </>
  );
};

export default DiJobFlow;
