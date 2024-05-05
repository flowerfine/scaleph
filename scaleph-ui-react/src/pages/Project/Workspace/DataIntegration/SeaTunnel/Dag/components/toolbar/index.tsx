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
import {useClipboard, useGraphEvent, useGraphInstance, useGraphStore, useKeyboard,} from '@antv/xflow';
import {Toolbar} from "@antv/x6-react-components";

const CustomToolbar: React.FC = () => {
  const intl = useIntl();
  const graph = useGraphInstance();
  const {copy, paste} = useClipboard();
  const nodes = useGraphStore((state) => state.nodes);
  const edges = useGraphStore((state) => state.edges);
  const removeNodes = useGraphStore((state) => state.removeNodes);
  const removeEdges = useGraphStore((state) => state.removeEdges);

  useKeyboard('ctrl+c', () => onCopy());
  useKeyboard('ctrl+v', () => onPaste());
  useKeyboard('backspace', (e: KeyboardEvent) => {
    const selectedNodes = nodes.filter((node) => node.selected);
    const nodeIds: string[] = selectedNodes.map((node) => node.id || '');
    removeNodes(nodeIds);
    const selectedEdges = edges.filter((edge) => edge.selected);
    const edgeIds: string[] = selectedEdges.map((edge) => edge.id || '');
    removeEdges(edgeIds);
  });

  useGraphEvent('node:change:data', ({node}) => {
    if (graph) {
      const edges = graph.getIncomingEdges(node);
    }
  });

  const onUndo = () => {
    if (graph?.canUndo) {
      graph.undo()
    }
  };

  const onRedo = () => {
    if (graph?.canRedo()) {
      graph?.redo()
    }
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
    const selected = nodes.filter((node) => node.selected);
    const ids: string[] = selected.map((node) => node.id || '');
    removeNodes(ids);
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
          icon={<SnippetsOutlined/>}
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
