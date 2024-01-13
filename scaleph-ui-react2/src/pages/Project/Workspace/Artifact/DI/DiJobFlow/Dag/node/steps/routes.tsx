import {Node} from "@antv/xflow";
import SourceFakeStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/source/source-fake-step";
import SinkConsoleStepForm from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/sink/sink-console-step";

export const switchStep = (node: Node) => {
  const name = node.data.meta.name;
  const type  = node.data.meta.type;
  console.log('switchStep', name, type)
  if (type === 'source' && name === 'FakeSource') {
    return (<SourceFakeStepForm visible data={node} onCancel={() => {}}/>);
  } else if(type==='sink' && name ==='Console'){
    return (<SinkConsoleStepForm visible data={node} onCancel={() => {}}/>);
  } else {
    return <></>;
  }
};
