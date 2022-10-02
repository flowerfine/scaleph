import { JobService } from '@/services/project/job.service';
import { NsGraph } from '@antv/xflow';
import { NsNodeCollapsePanel } from '@antv/xflow-extension/es';
import { getIntl, getLocale, request } from 'umi';
import { CONNECTION_PORT_TYPE, DND_RENDER_ID, NODE_HEIGHT, NODE_WIDTH } from './constant';

export const DagService = {
  url: '/api/di/job',
  loadNodeMeta: async () => {
    return request<NsNodeCollapsePanel.ICollapsePanel[]>(`${DagService.url}/node/meta`, {
      method: 'GET',
    });
  },
  saveGraphData: async (meta: NsGraph.IGraphMeta, graphData: NsGraph.IGraphData) => {
    meta.origin.jobGraph = graphData;
    return JobService.saveJobDetail(meta.origin);
  },

  loadJobInfo: async (id: number) => {
    let result: NsGraph.IGraphData = { nodes: [], edges: [] };
    await JobService.selectJobById(id).then((resp) => {
      let jobInfo = resp;
      let nodes: NsGraph.INodeConfig[] = [];
      let edges: NsGraph.IEdgeConfig[] = [];
      jobInfo.jobStepList?.map((step) => {
        nodes.push({
          id: step.stepCode,
          x: step.positionX,
          y: step.positionY,
          label: step.stepTitle,
          renderKey: DND_RENDER_ID,
          width: NODE_WIDTH,
          height: NODE_HEIGHT,
          ports: DagService.createPorts(step.stepType.value as string),
          data: {
            jobId: step.jobId,
            name: step.stepName,
            type: step.stepType.value as string,
            displayName: DagService.titleCase(step.stepName + ' ' + step.stepType.value),
            createTime: step.createTime,
            updateTime: step.updateTime,
            attrs:step.stepAttrs
          },
        });
      });
      jobInfo.jobLinkList?.map((link) => {
        edges.push({
          id: link.linkCode,
          source: link.fromStepCode,
          target: link.toStepCode,
          sourcePortId: CONNECTION_PORT_TYPE.source,
          targetPortId: CONNECTION_PORT_TYPE.target,
        });
      });
      result = { nodes: nodes, edges: edges };
    });
    return result;
  },

  createPorts: (type: string) => {
    const intl = getIntl(getLocale(), true);
    const group = {
      top: {
        position: 'top',
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: '#31d0c6',
            strokeWidth: 2,
            fill: '#fff',
          },
        },
      },
      bottom: {
        position: 'bottom',
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: '#31d0c6',
            strokeWidth: 2,
            fill: '#fff',
          },
        },
      },
    };
    if (type === 'source') {
      const items: NsGraph.INodeAnchor[] = [
        {
          id: CONNECTION_PORT_TYPE.source,
          group: NsGraph.AnchorGroup.BOTTOM,
          type: NsGraph.AnchorType.OUTPUT,
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.port.out' }),
        },
      ];
      return { groups: group, items: items };
    } else if (type === 'trans') {
      const items: NsGraph.INodeAnchor[] = [
        {
          id: CONNECTION_PORT_TYPE.source,
          group: NsGraph.AnchorGroup.BOTTOM,
          type: NsGraph.AnchorType.OUTPUT,
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.port.out' }),
        },
        {
          id: CONNECTION_PORT_TYPE.target,
          group: NsGraph.AnchorGroup.TOP,
          type: NsGraph.AnchorType.INPUT,
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.port.in' }),
        },
      ];
      return { groups: group, items: items };
    } else if (type === 'sink') {
      const items: NsGraph.INodeAnchor[] = [
        {
          id: CONNECTION_PORT_TYPE.target,
          group: NsGraph.AnchorGroup.TOP,
          type: NsGraph.AnchorType.INPUT,
          tooltip: intl.formatMessage({ id: 'pages.project.di.flow.dag.port.in' }),
        },
      ];
      return { groups: group, items: items };
    } else {
      return { groups: group, items: [] };
    }
  },
  titleCase: (title: string) => {
    let tmpStrArr: string[] = title.split(' ');
    for (let i = 0; i < tmpStrArr.length; i++) {
      tmpStrArr[i] = tmpStrArr[i].slice(0, 1).toUpperCase() + tmpStrArr[i].slice(1).toLowerCase();
    }
    return tmpStrArr.join(' ');
  },
};
