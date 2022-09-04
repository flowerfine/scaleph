import { selectJobById } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { CloseOutlined, SaveOutlined } from '@ant-design/icons';
import {
  CanvasToolbar,
  createGraphConfig,
  createToolbarConfig,
  IAppLoad,
  IconStore,
  IToolbarItemOptions,
  NsGraph,
  NsGraphCmd,
  XFlow,
  XFlowCanvas,
  XFlowGraphCommands,
} from '@antv/xflow';
import { Button, Col, Drawer, message, Popover, Row, Space, Tag, Tooltip } from 'antd';
import React, { useLayoutEffect, useState } from 'react';
import { useAccess, useIntl } from 'umi';
import styles from './index.less';
interface DiJobFlowPorps {
  visible: boolean;
  data: DiJob;
  onVisibleChange: (visible: boolean, data: any) => void;
  onCancel: () => void;
}

export const useGraphConfig = createGraphConfig<{}>((config) => {
  config.setDefaultNodeRender((props) => {
    return <div className="react-node"> {props.data.label} </div>;
  });
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

export const useToolbarConfig = createToolbarConfig<{}>((config) => {
  const intl = useIntl();
  IconStore.set('SaveOutlined', SaveOutlined);
  const toolbarGroup: IToolbarItemOptions[] = [
    {
      id: XFlowGraphCommands.SAVE_GRAPH_DATA.id,
      iconName: 'SaveOutlined',
      tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.save' }),
    //   onClick: async ({ commandService }) => {
    //     commandService.executeCommand<NsGraphCmd.SaveGraphData.IArgs>(
    //       XFlowGraphCommands.SAVE_GRAPH_DATA.id,
    //       {
    //         saveGraphDataService: async (meta, data) => {
    //           console.log(data);
    //           message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
    //         },
    //       },
    //     );
    //   },
    },
  ];
  config.setToolbarModelService(async (toolbarModel) => {
    const toolbarItems = [{ name: 'actionGropu', items: toolbarGroup }];
    toolbarModel.setValue((toolbar) => {
      toolbar.mainGroups = toolbarItems;
    });
  });
});

const DiJobFlow: React.FC<DiJobFlowPorps> = ({ visible, data, onVisibleChange, onCancel }) => {
  const intl = useIntl();
  const access = useAccess();
  const [jobInfo, setJobInfo] = useState<DiJob>({});
  const graphConfig = useGraphConfig({});
  const toolbarConfig = useToolbarConfig({});
  const [graphData, setGraphData] = useState<NsGraph.IGraphData>();
  useLayoutEffect(() => {
    console.log(visible);
    refreshGraph();
  }, []);

  const refreshGraph = () => {
    selectJobById(data.id as number).then((d) => {
      setJobInfo(d);
    });
  };

  const onGraphLoad: IAppLoad = async (app) => {
    console.log(app);
    //graph instance
    const graph = await app.getGraphInstance();
    //todo set graph data
    //todo init graph
    //todo event listener
  };

  return (
    <>
      <Button type="primary">Open</Button>
      <Drawer
        title={
          <Space>
            <Popover
              content={
                <>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.jobName' }) +
                      ' : ' +
                      jobInfo.jobName}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.jobStatus' }) +
                      ' : ' +
                      jobInfo.jobStatus?.label}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.jobVersion' }) +
                      ' : ' +
                      jobInfo.jobVersion}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.createTime' }) +
                      ' : ' +
                      jobInfo.createTime}
                  </p>
                  <p>
                    {intl.formatMessage({ id: 'pages.project.di.updateTime' }) +
                      ' : ' +
                      jobInfo.updateTime}
                  </p>
                </>
              }
              title={false}
              placement="bottom"
              trigger="hover"
            >
              <Tag color="blue">
                {intl.formatMessage({ id: 'pages.project.di.job.batch' }) + ' : ' + jobInfo.jobCode}
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
        <Row>
          <Col span={24}>
            <XFlow
              className={styles.xflowWorkspace}
              isAutoCenter={true}
              graphData={graphData}
              onLoad={onGraphLoad}
              graphLayout={{
                layoutType: 'dagre',
                layoutOptions: {
                  type: 'dagre',
                  rankdir: 'TB', // from top to bottom
                  nodesep: 50,
                  ranksep: 50,
                },
              }}
            >
              <CanvasToolbar
                layout="horizontal"
                position={{ top: 0, left: 0, right: 0, height: 40 }}
                config={toolbarConfig}
              ></CanvasToolbar>
              <XFlowCanvas
                config={graphConfig}
                position={{ top: 40, bottom: 0, left: 0, right: 0 }}
              ></XFlowCanvas>
            </XFlow>
          </Col>
        </Row>
      </Drawer>
    </>
  );
};

export default DiJobFlow;