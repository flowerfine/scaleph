import React from 'react';
import {Button, Descriptions, Popover, Space, Typography} from 'antd';
import {CopyOutlined, DeleteOutlined, HolderOutlined, InfoCircleOutlined, MenuOutlined,} from '@ant-design/icons';
import type {Node} from '@antv/xflow';
import {Graph, Path, register, XFlow} from '@antv/xflow';
import {Dropdown, Menu} from '@antv/x6-react-components';
import './base-node.less';

const {Item: MenuItem, Divider} = Menu;

const DAG_NODE = 'seatunnel-dag-node';
const DAG_EDGE = 'seatunnel-dag-edge';
const DAG_CONNECTOR = 'seatunnel-dag-connector';

const SeaTunnelConnectorDagNode = ({node}: { node: Node }) => {

  const onMenuItemClick = (key: string) => {
    const graph = node?.model?.graph;
    if (!graph) {
      return;
    }
    switch (key) {
      case 'copy':
        graph.copy([graph.getCellById(node.id)]);
        break;
      case 'paste':
        graph.paste();
        break;
      case 'delete':
        node.remove();
        break;
      default:
        break;
    }
  };

  const menu = (
    <Menu hasIcon={true} onClick={(key: string) => onMenuItemClick(key)}>
      <MenuItem name="copy" icon={<CopyOutlined/>} text="复制"/>
      <MenuItem name="paste" icon={<CopyOutlined/>} text="粘贴"/>
      <MenuItem name="delete" icon={<DeleteOutlined/>} text="删除"/>
    </Menu>
  );

  return (
    <XFlow>
      <Dropdown
        overlay={menu}
        trigger={['contextMenu']}
        overlayStyle={{overflowY: 'auto'}}
      >
        <Popover title={<div>
          <Typography.Text>{node.data.label}</Typography.Text>
          <a href="https://seatunnel.apache.org/docs/2.3.3/about/" target="_blank">
            <Button shape="default" type="link" icon={<InfoCircleOutlined/>}/>
          </a>
        </div>}
                 content={<Descriptions style={{maxWidth: '220px'}} size="small" column={1}>
                   <Descriptions.Item label={"Connector 名称"}>{node.data.meta?.name}</Descriptions.Item>
                   <Descriptions.Item label={"Connector 类型"}>{node.data.meta?.type}</Descriptions.Item>
                 </Descriptions>}
        >
          <div className={"base-node"}>
          <span className="icon">
            <MenuOutlined style={{color: '#3057e3', fontSize: '12px'}}/>
          </span>
            <span className="label">
            <Space direction="vertical">
              <Typography.Text ellipsis={true}>{node.data.label}</Typography.Text>
            </Space>
          </span>
            <div className="icon">
              <HolderOutlined/>
            </div>
          </div>
        </Popover>
      </Dropdown>
    </XFlow>
  );
};

register({
  shape: DAG_NODE,
  width: 180,
  height: 36,
  component: SeaTunnelConnectorDagNode,
  effect: ['data'],
  ports: {
    groups: {
      top: {
        position: 'top',
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: '#C2C8D5',
            strokeWidth: 1,
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
            stroke: '#C2C8D5',
            strokeWidth: 1,
            fill: '#fff',
          },
        },
      },
    },
  }
});

Graph.registerConnector(
  DAG_CONNECTOR,
  (s, e) => {
    const offset = 4;
    const deltaY = Math.abs(e.y - s.y);
    const control = Math.floor((deltaY / 3) * 2);

    const v1 = {x: s.x, y: s.y + offset + control};
    const v2 = {x: e.x, y: e.y - offset - control};

    return Path.normalize(
      `M ${s.x} ${s.y}
     L ${s.x} ${s.y + offset}
     C ${v1.x} ${v1.y} ${v2.x} ${v2.y} ${e.x} ${e.y - offset}
     L ${e.x} ${e.y}
    `,
    );
  },
  true,
);

Graph.registerEdge(
  DAG_EDGE,
  {
    inherit: 'edge',
    attrs: {
      line: {
        stroke: '#C2C8D5',
        strokeWidth: 1
      },

    },
    zIndex: -1,
  },
  true,
);

export {DAG_NODE, DAG_EDGE, DAG_CONNECTOR};
