import { InfoCircleOutlined, MenuOutlined } from '@ant-design/icons';
import { Button, Descriptions, Popover, Space, Tag, Typography } from 'antd';
import { getIntl, getLocale } from 'umi';
import './base-node.less';

export const BaseNode = (props: any) => {
  const intl = getIntl(getLocale(), true);

  const nodeStyle = () => {
    switch (props.data.data.type) {
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
            {props.data.data.jobId ? (
              <>
                <Descriptions.Item
                  label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
                >
                  {props.data.label}
                </Descriptions.Item>
                <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.di.step.x' })}>
                  {props.data.x}
                </Descriptions.Item>
                <Descriptions.Item label={intl.formatMessage({ id: 'pages.project.di.step.y' })}>
                  {props.data.y}
                </Descriptions.Item>
                <Descriptions.Item
                  label={intl.formatMessage({ id: 'pages.project.di.createTime' })}
                >
                  {props.data.data.createTime}
                </Descriptions.Item>
                <Descriptions.Item
                  label={intl.formatMessage({ id: 'pages.project.di.updateTime' })}
                >
                  {props.data.data.updateTime}
                </Descriptions.Item>
              </>
            ) : (
              <Descriptions.Item>{props.data.description}</Descriptions.Item>
            )}
          </Descriptions>
          {props.data?.health && <Tag color="red">{props.data?.health?.label}</Tag>}
          {props.data?.features &&
            props.data.features.map((item) => {
              return <Tag color="green">{item.label}</Tag>;
            })}
        </>
      }
      title={
        <div>
          <Typography.Text>{props.data.data.displayName}</Typography.Text>
          <a href="https://flowerfine.github.io/scaleph-website/" target="_blank">
            <Button shape="default" type="link" icon={<InfoCircleOutlined />}></Button>
          </a>
        </div>
      }
      placement="rightTop"
    >
      <div className="base-node" style={{ backgroundColor: nodeStyle() }}>
        <span className="icon">
          <MenuOutlined style={{ color: '#3057e3', fontSize: '16px' }} />
        </span>
        <span className="label">
          <Space direction="vertical">
            <Typography.Text ellipsis={true}>{props.data.label}</Typography.Text>
          </Space>
        </span>
      </div>
    </Popover>
  );
};
