import {Node} from "@antv/xflow";

export const switchStep = (name: string, type: String, node: Node) => {
  if (type === 'source' && name === 'FakeSource') {
    return (<SourceFakeStepForm visible data={data} onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
  } else if(type==='sink' && name ==='Console'){
    return (<SinkConsoleStepForm visible data={data}  onCancel={() => this.onCancel(container)} onOK={() => this.onOk(data, container)}/>);
  } else {
    return <></>;
  }
};
