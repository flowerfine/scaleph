import {Button, Descriptions, Popover, Space, Tag, Typography} from 'antd';
import {InfoCircleOutlined, MenuOutlined} from '@ant-design/icons';
import {useIntl} from "@umijs/max";
import './base-node.less';
import {Props} from "@/typings";

export const BaseNode: React.FC<Props<Record<string, any>>> = ({data}) => {
  const intl = useIntl();

  const nodeStyle = () => {
    switch (data.meta?.type) {
      case 'source':
        return '#e6f4ff';
      case 'transform':
        return '#e6fffb';
      case 'sink':
        return '#fff7e6';
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
            <Button shape="default" type="link" icon={<InfoCircleOutlined/>}></Button>
          </a>
        </div>
      }
      placement="rightTop"
    >
      <div className="base-node" style={{backgroundColor: nodeStyle()}}>
        <span className="icon">
          <MenuOutlined style={{color: '#3057e3', fontSize: '16px'}}/>
        </span>
        <span className="label">
          <Space direction="vertical">
            <Typography.Text ellipsis={true}>{data.title}</Typography.Text>
          </Space>
        </span>
      </div>
    </Popover>
  );
};
