import React from "react";
import {Button, Descriptions, Popover, Space, Tag, Typography} from 'antd';
import {HolderOutlined, InfoCircleOutlined, MenuOutlined} from '@ant-design/icons';
import {useIntl} from "@umijs/max";
import {useDnd} from "@antv/xflow";
import {Dict, Props} from "@/typings";
import {DAG_NODE} from "./canvas-node";
import './base-node.less';
import {titleCase} from "./init-node";

export const DndNode: React.FC<Props<Record<string, any>>> = ({data}) => {
  const intl = useIntl();
  const {startDrag} = useDnd();

  const handleMouseDown = (e: React.MouseEvent<Element, MouseEvent>,) => {
    startDrag(
      {
        shape: DAG_NODE,
        data: {
          label: data.title + " " + titleCase(data.category),
          meta: data.meta,
          attrs: {}
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
        <Descriptions style={{ maxWidth: '380px' }} size="small" column={1}>
          <Descriptions.Item>{data.docString}</Descriptions.Item>
        </Descriptions>
          {data.health && <Tag color="red">{data.health?.label}</Tag>}
          {data.features &&
            data.features.map((item: Dict) => {
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
      placement="right"
    >
      <div className="base-node" style={{backgroundColor: nodeStyle()}} onMouseDown={(e) => handleMouseDown(e)}>
        <span className="icon">
          <MenuOutlined style={{color: '#3057e3', fontSize: '12px'}}/>
        </span>
        <span className="label">
          <Space direction="vertical">
            <Typography.Text ellipsis={true}>{data.title}</Typography.Text>
          </Space>
        </span>
        <div className="icon">
          <HolderOutlined/>
        </div>
      </div>
    </Popover>
  );
};
