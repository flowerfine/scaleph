import { CanvasToolbar, createGraphConfig, IAppLoad, XFlow, XFlowCanvas } from '@antv/xflow';
import React from 'react';
import styles from './index.less';
import { useToolbarConfig } from './toolbar-config';
/**  Demo Props  */
export interface IDemoProps {
  anything: string;
}
/**  graphConfig：配置Graph  */
export const useGraphConfig = createGraphConfig<IDemoProps>((graphConfig) => {
  graphConfig.setDefaultNodeRender((props) => {
    return <div className="react-node"> {props.data.label} </div>;
  });
});

const DataElement: React.FC<IDemoProps> = (props) => {
  const graphConfig = useGraphConfig(props);
  const toolbarConfig = useToolbarConfig(props);

  const onLoad: IAppLoad = async (app) => {
    console.log(app);
  };

  return (
    <XFlow onLoad={onLoad} className={styles.xflowWorkspace}>
      <CanvasToolbar
        layout="horizontal"
        config={toolbarConfig}
        position={{ top: 0, left: 0, right: 0, height: 40 }}
      />
      <XFlowCanvas config={graphConfig} position={{ top: 40, bottom: 0, left: 0, right: 0 }} />
    </XFlow>
  );
};

export default DataElement;
