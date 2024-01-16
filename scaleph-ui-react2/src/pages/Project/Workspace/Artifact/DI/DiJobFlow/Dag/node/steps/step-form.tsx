import {ModalFormProps} from "@/typings";
import SourceFakeStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-fake-step";
import SinkConsoleStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-console-step";
import {Node} from "@antv/xflow";

const SeaTunnnelConnectorForm: React.FC<ModalFormProps<Node>> = ({visible, onVisibleChange, onCancel, onOK, data}) => {

  const switchStep = (node: Node) => {
    const name = node.data.meta.name;
    const type = node.data.meta.type;
    if (type === 'source' && name === 'FakeSource') {
      return (<SourceFakeStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onOK}/>);
    } else if (type === 'sink' && name === 'Console') {
      return (<SinkConsoleStepForm visible data={node} onVisibleChange={onVisibleChange} onCancel={onCancel} onOK={onCancel}/>);
    } else {
      return <></>;
    }
  };

  return (<div>{switchStep(data)}</div>);
}

export default SeaTunnnelConnectorForm;
