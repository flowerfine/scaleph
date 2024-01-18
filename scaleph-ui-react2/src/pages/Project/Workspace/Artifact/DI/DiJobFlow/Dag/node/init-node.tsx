import React, {useEffect} from 'react';
import {useIntl} from '@umijs/max';
import {useGraphStore} from '@antv/xflow';
import {EdgeOptions, NodeOptions} from "@antv/xflow/src/types";
import {Props} from "@/typings";
import {WsDiJob} from "@/services/project/typings";
import {WsDiJobService} from "@/services/project/WsDiJobService";
import {DAG_EDGE, DAG_NODE} from './canvas-node';

const titleCase = (title: string) => {
  let tmpStrArr: string[] = title.split(' ');
  for (let i = 0; i < tmpStrArr.length; i++) {
    tmpStrArr[i] = tmpStrArr[i].slice(0, 1).toUpperCase() + tmpStrArr[i].slice(1).toLowerCase();
  }
  return tmpStrArr.join(' ');
}

const InitShape: React.FC<Props<WsDiJob>> = ({data}) => {
    const intl = useIntl()
    const addNodes = useGraphStore((state) => state.addNodes);
    const addEdges = useGraphStore((state) => state.addEdges);

    useEffect(() => {
      WsDiJobService.selectJobById(data.id).then((response) => {
        let jobInfo = response;
        let nodes: NodeOptions[] = [];
        let edges: EdgeOptions[] = [];
        jobInfo.jobStepList?.map((step) => {
          nodes.push({
            id: step.stepCode,
            shape: DAG_NODE,
            position: {
              x: step.positionX,
              y: step.positionY
            },
            ports: createPorts(step.stepMeta.type as string, step.stepCode),
            data: {
              label: step.stepTitle,
              meta: step.stepMeta,
              attrs: step.stepAttrs
            }
          });
        });
        addNodes(nodes)

        jobInfo.jobLinkList?.map((link) => {
          edges.push({
            id: link.linkCode,
            shape: DAG_EDGE,
            source: {
              cell: link.fromStepCode,
              port: link.fromStepCode + '-bottom'
            },
            target: {
              cell: link.toStepCode,
              port: link.fromStepCode + '-top',
            },
            zIndex: -1
          });
        });
        addEdges(edges)
      });
    }, []);

    const createPorts = (type: string, stepCode: string) => {
      if (type === 'source') {
        return [{
          id: stepCode + '-bottom',
          group: 'bottom'
        }]
      } else if (type === 'sink') {
        return [{
          id: stepCode + '-top',
          group: 'top'
        }]
      } else if (type === 'transform') {
        [
          {
            id: stepCode + '-top',
            group: 'top'
          },
          {
            id: stepCode + '-bottom',
            group: 'bottom'
          }
        ]
      } else {
        return []
      }
    }
    return null;
  }
;

export {InitShape, titleCase};
