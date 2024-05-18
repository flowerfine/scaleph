import {Node} from "@antv/xflow";
import {ModalFormProps} from "@/typings";
import SourceMySQLStepForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Dag/components/node/steps/source/source-mysql-step";
import SinkDorisStepForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Dag/components/node/steps/sink/sink-doris-step";
import SinkStarRocksStepForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Dag/components/node/steps/sink/sink-starrocks-step";
import RouteStepForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Dag/components/node/steps/route/route-step";

const FlinkCDCConnectorForm: React.FC<ModalFormProps<Node>> = ({visible, onVisibleChange, onCancel, onOK, data}) => {

  const switchStep = (node: Node) => {
    const name = node.data.meta.name;
    const type = node.data.meta.type;
    if (type === 'source' && name === 'MySQL') {
      return (<SourceMySQLStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Doris') {
      return (<SinkDorisStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'StarRocks') {
      return (<SinkStarRocksStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'route' && name === 'Route') {
      return (<RouteStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else {
      return <></>;
    }
  };

  return (<div>{switchStep(data)}</div>);
}

export default FlinkCDCConnectorForm;
