import type { FC } from 'react';
import type { NsNodeCollapsePanel } from '@antv/xflow';
import styles from './index.less';

interface CustomNodeProps {
  data: NsNodeCollapsePanel.IPanelNode;
  isNodePanel: boolean;
  style: React.CSSProperties;
}

const CustomNode: FC<CustomNodeProps> = ({ data, style, isNodePanel }) => {
  return (
    <div
      style={style}
      className={`${styles.customNode} ${isNodePanel ? 'isNodePanel' : ''}`}
    >
      {data.label}
    </div>
  );
};

export default CustomNode;
