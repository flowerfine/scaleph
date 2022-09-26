import {
  createToolbarConfig,
  IModelService,
  IToolbarGroupOptions,
  MODELS,
  NsGraphCmd,
  XFlowGraphCommands,
} from '@antv/xflow';
import { message } from 'antd';
import { getIntl, getLocale } from 'umi';
import { NsGraphPublish } from './cmd-extensions/graph-publish';
import { CustomCommands, ZOOM_OPTIONS } from './constant';
import { DagService } from './service';

export const useToolbarConfig = createToolbarConfig((toolbarConfig) => {
  /** toolbar item */
  toolbarConfig.setToolbarModelService(async (toolbarModel, modelService, toDispose) => {
    //init toolbar
    toolbarModel.setValue((toolbar) => {
      toolbar.mainGroups = getMainToolbarConfig();
      toolbar.extraGroups = getExtraToolbarConfig();
    });
  });
});

export const useScaleToolbarConfig = createToolbarConfig((toolbarConfig) => {
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

/**
 * toolbar config
 * todo disable stop button when job is not running
 */
const getMainToolbarConfig = () => {
  const intl = getIntl(getLocale(), true);
  return [
    {
      name: 'main',
      items: [
        {
          id: 'main03',
          iconName: 'SaveOutlined',
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.save' }),
          onClick: async ({ commandService, modelService }) => {
            commandService.executeCommand<NsGraphCmd.SaveGraphData.IArgs>(
              XFlowGraphCommands.SAVE_GRAPH_DATA.id,
              {
                saveGraphDataService: (meta, graphData) =>
                  DagService.saveGraphData(meta, graphData).then((resp) => {
                    if (resp.success) {
                      message.info(intl.formatMessage({ id: 'app.common.operate.success' }));
                      DagService.loadJobInfo(meta.origin.id).then((d) => {
                        commandService.executeCommand(XFlowGraphCommands.GRAPH_RENDER.id, {
                          graphData: d,
                        });
                      });
                    }
                  }),
              },
            );
          },
        },
        {
          id: 'main04',
          iconName: 'SendOutlined',
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.publish' }),
          onClick: async ({ commandService }) => {
            commandService.executeCommand<NsGraphPublish.IArgs>(
              CustomCommands.GRAPH_PUBLISH.id,
              {},
            );
          },
        },
      ],
    },
    // {
    //   name: 'main',
    //   items: [
    //     {
    //       id: 'main01',
    //       iconName: 'PlaySquareOutlined',
    //       tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.start' }),
    //       onClick: (args) => {},
    //     },
    //     {
    //       id: 'main02',
    //       iconName: 'StopOutlined',
    //       tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.stop' }),
    //       onClick: (args) => {},
    //     },
    //   ],
    // },
  ] as IToolbarGroupOptions[];
};

const getExtraToolbarConfig = () => {
  const intl = getIntl(getLocale(), true);
  return [
    {
      name: 'extra',
      items: [
        {
          id: 'extra01',
          iconName: 'ProfileOutlined',
          text: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.prop' }),
          onClick: ({ commandService }) => {
            commandService.executeCommand(CustomCommands.GRAPH_PARAMS_SETTING.id, {});
          },
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
  const intl = getIntl(getLocale(), true);
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
  const intl = getIntl(getLocale(), true);
  const graphScale = await MODELS.GRAPH_SCALE.useValue(modelService);
  message.info(
    intl.formatMessage(
      { id: 'pages.project.di.flow.dag.zoomTo' },
      { factor: graphScale.zoomFactor },
    ),
  );
};
