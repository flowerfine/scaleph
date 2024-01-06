import React from 'react';
import {Button, Space, Tooltip} from 'antd';
import {CalculatorOutlined, EyeOutlined, SaveOutlined} from '@ant-design/icons';
import {useIntl} from "@umijs/max";
import type {Edge} from '@antv/xflow';
import {useGraphEvent, useGraphInstance, useGraphStore,} from '@antv/xflow';
import {Menubar} from "@antv/x6-react-components";

const CustomMenubar: React.FC = () => {
  const intl = useIntl();
  const graph = useGraphInstance();
  const nodes = useGraphStore((state) => state.nodes);
  const updateEdge = useGraphStore((state) => state.updateEdge);
  const removeNodes = useGraphStore((state) => state.removeNodes);

  useGraphEvent('node:change:data', ({node}) => {
    if (graph) {
      const edges = graph.getIncomingEdges(node);
      const {status} = node.data;
      edges?.forEach((edge: Edge) => {
        if (status === 'running') {
          updateEdge(edge.id, {
            animated: true,
          });
        } else {
          updateEdge(edge.id, {
            animated: false,
          });
        }
      });
    }
  });

  const onPreview = () => {
    console.log('onPreview', graph)
  };

  const onDebug = () => {
    console.log('onDebug', graph)
  };

  const onSave = () => {
    console.log('onSave', graph)
  };

  return (
    <Menubar extra={<Space>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.debug'})}>
        <Button
          size="small"
          icon={<CalculatorOutlined/>}
          onClick={onDebug}
        />
      </Tooltip>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.preview'})}>
        <Button
          size="small"
          icon={<EyeOutlined/>}
          onClick={onPreview}
        />
      </Tooltip>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.save'})}>
        <Button
          size="small"
          icon={<SaveOutlined/>}
          onClick={onSave}
        />
      </Tooltip>

    </Space>}/>
  );
};

export {CustomMenubar};
