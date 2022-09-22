import { saveJobDetail, selectJobById } from '@/services/project/job.service';
import { NsGraph } from '@antv/xflow';
import { NsNodeCollapsePanel } from '@antv/xflow-extension/es';
import { request } from 'umi';

export const DagService = {
  url: '/api/di/job',
  loadNodeMeta: async () => {
    return request<NsNodeCollapsePanel.ICollapsePanel[]>(`${DagService.url}/node/meta`, {
      method: 'GET',
    });
  },
  saveGraphData: async (meta: NsGraph.IGraphMeta, graphData: NsGraph.IGraphData) => {
    meta.origin.jobGraph = graphData;
    return saveJobDetail(meta.origin);
  },
  loadJobInfo: async (id: number) => {
    return selectJobById(id);
  },
};
