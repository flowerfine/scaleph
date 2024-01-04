import React, {useEffect, useState} from 'react';
import {useIntl} from '@umijs/max';
import {useGraphStore} from '@antv/xflow';
import {EdgeOptions, NodeOptions} from "@antv/xflow/src/types";
import {Props} from "@/typings";
import {WsDiJob} from "@/services/project/typings";
import {WsDiJobService} from "@/services/project/WsDiJobService";

import {DAG_EDGE, DAG_NODE} from './connector-shape';

const InitShape: React.FC<Props<WsDiJob>> = ({data}) => {
    const intl = useIntl()
    const addNodes = useGraphStore((state) => state.addNodes);
    const addEdges = useGraphStore((state) => state.addEdges);
    const updateNode = useGraphStore((state) => state.updateNode);
    const updateEdge = useGraphStore((state) => state.updateEdge);

    const [wsDiJob, setWsDiJob] = useState<WsDiJob>()

    useEffect(() => {
      WsDiJobService.selectJobById(data.id).then((response) => {
        let jobInfo = response;
        let nodes: NodeOptions[] = [];
        let edges: EdgeOptions[] = [];
        jobInfo.jobStepList?.map((step) => {
          nodes.push({
            id: step.stepCode,
            shape: DAG_NODE,
            x: step.positionX,
            y: step.positionY,
            label: step.stepTitle,
            ports: createPorts(step.stepType.value as string, step.stepName.value as string),
            data: {
              meta: {
                jobId: step.jobId,
                name: step.stepName.value,
                type: step.stepType.value,
                displayName: titleCase(step.stepName.label + ' ' + step.stepType.value),
                createTime: step.createTime,
                updateTime: step.updateTime
              },
              attrs: step.stepAttrs
            },
          });
        });
        addNodes(nodes)

        jobInfo.jobLinkList?.map((link) => {
          edges.push({
            id: link.linkCode,
            shape: DAG_EDGE,
            source: {
              cell: link.fromStepCode,
            },
            target: {
              cell: link.toStepCode
            },
            data: {
              foo: 'bar'
            }
          });
        });
        addEdges(edges)
      });
    }, []);

    const createPorts = (type: string, name: string) => {
      if (type === 'source') {
        return [{
          id: name + '-bottom',
          group: 'bottom'
        }]
      } else if (type === 'sink') {
        return [{
          id: name + '-top',
          group: 'top'
        }]
      } else if (type === 'transform') {
        [
          {
            id: name + '-top',
            group: 'top'
          },
          {
            id: name + '-bottom',
            group: 'bottom'
          }
        ]
      } else {
        return []
      }
    }

    const titleCase = (title: string) => {
      let tmpStrArr: string[] = title.split(' ');
      for (let i = 0; i < tmpStrArr.length; i++) {
        tmpStrArr[i] = tmpStrArr[i].slice(0, 1).toUpperCase() + tmpStrArr[i].slice(1).toLowerCase();
      }
      return tmpStrArr.join(' ');
    }

    return null;
  }
;

export {InitShape};
