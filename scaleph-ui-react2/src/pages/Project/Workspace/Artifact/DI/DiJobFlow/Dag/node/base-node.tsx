import {Button, Descriptions, Popover, Space, Tag, Typography} from 'antd';
import {HolderOutlined, InfoCircleOutlined, MenuOutlined} from '@ant-design/icons';
import {useIntl} from "@umijs/max";
import {useDnd} from "@antv/xflow";
import './base-node.less';
import {Props} from "@/typings";
import styles from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/dnd/dnd.less";
import React from "react";
import {DAG_NODE} from "../shape/connector-shape";

export const BaseNode: React.FC<Props<Record<string, any>>> = ({data}) => {
  const intl = useIntl();
  const {startDrag} = useDnd();
  let id = 0;

  const handleMouseDown = (
    e: React.MouseEvent<Element, MouseEvent>,
  ) => {
    id += 1;
    startDrag(
      {
        id: id.toString(),
        shape: DAG_NODE,
        data: {
          id: id.toString(),
          label: data.title,
          status: 'default',
        },
        ports: data.ports,
      },
      e,
    );
  };

  const nodeStyle = () => {
    switch (data.meta?.type) {
      case 'source':
        // return '#e6f4ff';
      case 'transform':
        // return '#e6fffb';
      case 'sink':
        // return '#fff7e6';
      default:
        return '';
    }
  };

  return (
    <Popover
      content={
        <>
        <Descriptions style={{ maxWidth: '240px' }} size="small" column={1}>
          <Descriptions.Item>{data.docString}</Descriptions.Item>
        </Descriptions>
          {data.health && <Tag color="red">{data.health?.label}</Tag>}
          {data.features &&
            data.features.map((item) => {
              return <Tag color="green">{item.label}</Tag>;
            })}
        </>
      }
      title={
        <div>
          <Typography.Text>{data.title}</Typography.Text>
          <a href="https://flowerfine.github.io/scaleph-repress-site/" target="_blank">
            <Button shape="default" type="link" icon={<InfoCircleOutlined/>}/>
          </a>
        </div>
      }
      placement="rightTop"
    >
      <div className="base-node" style={{backgroundColor: nodeStyle()}} onMouseDown={(e) => handleMouseDown(e)}>
        <span className="icon">
          <MenuOutlined style={{color: '#3057e3', fontSize: '16px'}}/>
        </span>
        <span className="label">
          <Space direction="vertical">
            <Typography.Text ellipsis={true}>{data.title}</Typography.Text>
          </Space>
        </span>
        <div className={styles.nodeDragHolder}>
          <HolderOutlined/>
        </div>
      </div>
    </Popover>
  );
};
