import {DiJob} from '@/services/project/typings';
import {
  CloseOutlined,
  CompressOutlined,
  DeleteOutlined,
  EditOutlined,
  EyeOutlined,
  FullscreenExitOutlined,
  FullscreenOutlined,
  FundProjectionScreenOutlined,
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
  IApplication,
  IAppLoad,
  IconStore,
  KeyBindings,
  NodeCollapsePanel,
  NsGraph,
  XFlow,
  XFlowCanvas,
} from '@antv/xflow';
import {Button, Drawer, Popover, Space, Tag, Tooltip} from 'antd';
import React, {useState} from 'react';
import {useIntl} from 'umi';
/** config graph */
import {useGraphCOnfig, useGraphHookConfig} from './Dag/config-graph';
/** config command */
import {initGraphCmds, useCmdConfig} from './Dag/config-cmd';
/** config key bind */
import {useKeybindingConfig} from './Dag/config-keybinding';
/** 配置Model */
/** config dnd panel */
import '@antv/xflow/dist/index.css';
import * as dndPanelConfig from './Dag/config-dnd-panel';
import {useMenuConfig} from './Dag/config-menu';
import {useScaleToolbarConfig, useToolbarConfig} from './Dag/config-toolbar';
import {DagService} from './Dag/service';
import './index.less';

interface DiJobFlowPorps {
  visible: boolean;
  data: DiJob;
  onVisibleChange: (visible: boolean, data: any) => void;
  onCancel: () => void;
  meta: { flowId?: string; origin?: DiJob };
}

const DiJobFlow: React.FC<DiJobFlowPorps> = (props) => {
  const intl = useIntl();
  const {visible, data, onVisibleChange, onCancel, meta} = props;
  const graphConfig = useGraphCOnfig(props);
  const [graphData, setGraphData] = useState<NsGraph.IGraphData>({nodes: [], edges: []});
  const cmdConfig = useCmdConfig();
  const menuConfig = useMenuConfig();
  const toolbarConfig = useToolbarConfig();
  const scaleToolbarConfig = useScaleToolbarConfig();
  const graphHookConfig = useGraphHookConfig(props);
  const keybindingConfig = useKeybindingConfig();

  /**register icons */
  IconStore.set('DeleteOutlined', DeleteOutlined);
  IconStore.set('EditOutlined', EditOutlined);
  IconStore.set('PlaySquareOutlined', PlaySquareOutlined);
  IconStore.set('StopOutlined', StopOutlined);
  IconStore.set('SaveOutlined', SaveOutlined);
  IconStore.set('SendOutlined', SendOutlined);
  IconStore.set('EyeOutlined', EyeOutlined);
  IconStore.set('FundProjectionScreenOutlined', FundProjectionScreenOutlined);
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
    initGraphCmds(cache.app, meta.origin || {id: data.id});
  };

  React.useEffect(() => {
    if (cache.app) {
      initGraphCmds(cache.app, meta.origin || {id: data.id});
    }
    refreshJobGraph();

  }, [meta]);

  const refreshJobGraph = async () => {
    DagService.loadJobInfo(meta.origin?.id as number).then((resp) => {
      setGraphData(resp);
    });
  };

  return (
    <>
      <Drawer
        title={
          <Space>
            <Popover
              content={
                <>
                  <p>
                    {intl.formatMessage({id: 'pages.project.di.jobName'}) + ' : ' + data.jobName}
                  </p>
                  <p>
                    {intl.formatMessage({id: 'pages.project.di.jobStatus'}) +
                      ' : ' +
                      data.jobStatus?.label}
                  </p>
                  <p>
                    {intl.formatMessage({id: 'pages.project.di.jobVersion'}) +
                      ' : ' +
                      data.jobVersion}
                  </p>
                  <p>
                    {intl.formatMessage({id: 'pages.project.di.createTime'}) +
                      ' : ' +
                      data.createTime}
                  </p>
                  <p>
                    {intl.formatMessage({id: 'pages.project.di.updateTime'}) +
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
                {intl.formatMessage({id: 'pages.project.di.job.batch'}) + ' : ' + data.jobCode}
              </Tag>
            </Popover>
          </Space>
        }
        bodyStyle={{padding: '0px'}}
        placement="top"
        width="100%"
        height="100%"
        closable={false}
        onClose={onCancel}
        extra={
          <Space>
            <Tooltip title={intl.formatMessage({id: 'app.common.operate.close.label'})}></Tooltip>
            <Button
              shape="default"
              type="text"
              icon={<CloseOutlined/>}
              onClick={onCancel}
            ></Button>
          </Space>
        }
        visible={visible}
      >
        <XFlow
          className="dag-user-custom-clz"
          hookConfig={graphHookConfig}
          commandConfig={cmdConfig}
          onLoad={onLoad}
          graphData={graphData}
          meta={meta}
        >
          <NodeCollapsePanel
            className="xflow-node-panel"
            position={{width: 240, top: 0, bottom: 0, left: 0}}
            bodyPosition={{top: 40, bottom: 0, left: 0}}
            footerPosition={{height: 0}}
            nodeDataService={dndPanelConfig.nodeDataService}
            onNodeDrop={dndPanelConfig.onNodeDrop}
            searchService={dndPanelConfig.searchService}
          ></NodeCollapsePanel>
          <CanvasToolbar
            className="xflow-workspace-toolbar-top"
            layout="horizontal"
            config={toolbarConfig}
            position={{top: 0, left: 240, right: 0, bottom: 0}}
          />
          <XFlowCanvas config={graphConfig} position={{top: 40, left: 240, right: 0, bottom: 0}}>
            <CanvasToolbar
              position={{top: 12, right: 12}}
              config={scaleToolbarConfig}
              layout="vertical"
            />
            <CanvasContextMenu config={menuConfig}/>
            <CanvasSnapline color="#faad14"/>
            <CanvasMiniMap minimapOptions={{width: 200, height: 120}}/>
            <CanvasNodePortTooltip/>
          </XFlowCanvas>
          <KeyBindings config={keybindingConfig}/>
        </XFlow>
      </Drawer>
    </>
  );
};

export default DiJobFlow;
