import {useState} from 'react';
import {Input, Modal} from 'antd';
import {CopyOutlined, DeleteOutlined, EditOutlined,} from '@ant-design/icons';
import type {Node} from '@antv/xflow';
import {Graph, Path, register, XFlow} from '@antv/xflow';
import {Dropdown, Menu} from '@antv/x6-react-components';
import styles from './base-node.less';
import '@antv/x6-react-components/dist/index.css';

const {Item: MenuItem, Divider} = Menu;

const DAG_NODE = 'seatunnel-dag-node';
const DAG_EDGE = 'seatunnel-dag-edge';
const DAG_CONNECTOR = 'seatunnel-dag-connector';

const SeaTunnelConnectorDagNode = ({node}: { node: Node }) => {
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState<string | undefined>();

  const onMenuItemClick = (key: string) => {
    const graph = node?.model?.graph;
    if (!graph) {
      return;
    }
    switch (key) {
      case 'delete':
        node.remove();
        break;
      case 'copy':
        graph.copy([graph.getCellById(node.id)]);
        break;
      case 'paste':
        graph.paste();
        break;
      case 'rename':
        setOpen(true);
        break;
      default:
        break;
    }
  };

  const menu = (
    <Menu hasIcon={true} onClick={(key: string) => onMenuItemClick(key)}>
      <MenuItem name="rename" icon={<EditOutlined/>} text="重命名"/>
      <MenuItem name="copy" icon={<CopyOutlined/>} text="复制"/>
      <MenuItem name="paste" icon={<CopyOutlined/>} text="粘贴"/>
      <MenuItem name="delete" icon={<DeleteOutlined/>} text="删除"/>
    </Menu>
  );

  return (
    <XFlow>
      <Modal
        title="重命名"
        open={open}
        okText="确定"
        cancelText="取消"
        onCancel={() => setOpen(false)}
        onOk={() => {
          node.setData({
            ...node.data,
            label: value,
          });
          setOpen(false);
        }}
      >
        <Input value={value} onChange={(e) => setValue(e.target.value)}/>
      </Modal>
      <Dropdown
        overlay={menu}
        trigger={['contextMenu']}
        overlayStyle={{overflowY: 'auto'}}
      >
        <div className={styles.baseNode}>
          {node.data?.meta?.name}
        </div>
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
  },
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
        strokeWidth: 1,
        targetMarker: null,
      },
    },
    zIndex: -1,
  },
  true,
);

export {DAG_NODE, DAG_EDGE, DAG_CONNECTOR};
