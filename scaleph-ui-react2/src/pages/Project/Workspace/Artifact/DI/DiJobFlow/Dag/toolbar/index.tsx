import React from 'react';
import {Button, Space, Tooltip} from 'antd';
import {
  CopyOutlined,
  DeleteOutlined,
  QuestionCircleOutlined,
  RedoOutlined,
  SnippetsOutlined,
  UndoOutlined
} from '@ant-design/icons';
import {useIntl} from "@umijs/max";
import type {Edge, Node, NodeOptions} from '@antv/xflow';
import {useClipboard, useGraphEvent, useGraphInstance, useGraphStore, useKeyboard,} from '@antv/xflow';
import {Toolbar} from "@antv/x6-react-components";

const CustomToolbar: React.FC = () => {
  const intl = useIntl();
  const graph = useGraphInstance();
  const {copy, paste} = useClipboard();
  const nodes = useGraphStore((state) => state.nodes);
  const updateNode = useGraphStore((state) => state.updateNode);
  const updateEdge = useGraphStore((state) => state.updateEdge);
  const removeNodes = useGraphStore((state) => state.removeNodes);

  useKeyboard('ctrl+c', () => onCopy());

  useKeyboard('ctrl+v', () => onPaste());

  useKeyboard('backspace', () => {
    const selected = nodes.filter((node) => node.selected);
    const ids: string[] = selected.map((node) => node.id || '');
    removeNodes(ids);
  });

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

  const handleExcute = () => {
    if (graph) {
      nodes.forEach((node: Node | NodeOptions, index: number) => {
        const edges = graph.getOutgoingEdges(node as Node);
        updateNode(node.id!, {
          data: {
            ...node.data,
            status: 'running',
          },
        });

        setTimeout(() => {
          updateNode(node.id!, {
            data: {
              ...node.data,
              status: edges
                ? 'success'
                : Number(node.id!.slice(-1)) % 2 !== 0
                  ? 'success'
                  : 'failed',
            },
          });
        }, 1000 * index + 1);
      });
    }
  };

  const onUndo = () => {
    console.log('onUndo', graph)
  };

  const onRedo = () => {
    console.log('onRedo', graph)
  };

  const onCopy = () => {
    const selected = nodes.filter((node) => node.selected);
    const ids: string[] = selected.map((node) => node.id || '');
    copy(ids);
  };

  const onPaste = () => {
    paste();
  };

  const onDelete = () => {
    console.log('onDelete', graph)
  };

  const onHelp = () => {
    console.log('onHelp', graph)
  };

  return (
    <Toolbar extra={<Space>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.undo'})}>
        <Button
          size="small"
          icon={<UndoOutlined/>}
          onClick={onUndo}
        />
      </Tooltip>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.redo'})}>
        <Button
          size="small"
          icon={<RedoOutlined/>}
          onClick={onRedo}
        />
      </Tooltip>

      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.copy'})}>
        <Button
          size="small"
          icon={<CopyOutlined/>}
          onClick={onCopy}
        />
      </Tooltip>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.paste'})}>
        <Button
          size="small"
          icon={<SnippetsOutlined />}
          onClick={onPaste}
        />
      </Tooltip>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.delete'})}>
        <Button
          size="small"
          icon={<DeleteOutlined/>}
          onClick={onDelete}
        />
      </Tooltip>
      <Tooltip title={intl.formatMessage({id: 'pages.project.di.flow.dag.help'})}>
        <Button
          size="small"
          icon={<QuestionCircleOutlined/>}
          onClick={onHelp}
        />
      </Tooltip>
    </Space>}/>
  );
};

export {CustomToolbar};
