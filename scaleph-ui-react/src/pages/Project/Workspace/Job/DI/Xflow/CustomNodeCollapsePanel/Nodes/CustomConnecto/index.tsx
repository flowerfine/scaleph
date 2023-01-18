import type { FC } from 'react';
import type { NsNodeCollapsePanel } from '@antv/xflow';
import styles from './index.less';

interface CustomConnectoProps {
  data: NsNodeCollapsePanel.IPanelNode;
  isNodePanel: boolean;
  style: React.CSSProperties;
}

const CustomConnecto: FC<CustomConnectoProps> = ({
  data,
  style,
  isNodePanel,
}) => {
  return (
    <div
      style={style}
      className={`${styles.CustomConnecto} ${isNodePanel ? 'isNodePanel' : ''}`}
    >
      {data.label}
    </div>
  );
};

export default CustomConnecto;
