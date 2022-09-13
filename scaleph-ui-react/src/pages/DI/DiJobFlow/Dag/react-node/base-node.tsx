import { MenuOutlined } from '@ant-design/icons';
import { Typography } from 'antd';
import './base-node.less';

export const BaseNode = (props: any) => {
  console.log(props);
  return (
    <div className="base-node">
      <span className="icon">
        <MenuOutlined style={{ color: '#3057e3', fontSize: '16px' }} />
      </span>
      <span className="label">
        <Typography.Text>{props.data.label}</Typography.Text>
      </span>
    </div>
  );
};
