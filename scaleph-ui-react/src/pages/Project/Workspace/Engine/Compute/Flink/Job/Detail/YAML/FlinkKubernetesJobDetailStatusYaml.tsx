import React, {useEffect, useRef, useState} from "react";
import {connect} from "@umijs/max";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {Props} from '@/typings';
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

const FlinkKubernetesJobDetailStatusYAMLWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {

  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [jobStatus, setJobStatus] = useState<string>()

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    if (props.flinkKubernetesJobDetail.job?.jobInstance) {
      WsFlinkKubernetesJobService.getInstanceStatusYaml(props.flinkKubernetesJobDetail.job?.jobInstance?.id).then((response) => {
        if (response.success) {
          setJobStatus(response.data)
        }
      })
    }
  }, [props.flinkKubernetesJobDetail.job?.jobInstance]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={jobStatus}
      options={{
        selectOnLineNumbers: true,
        readOnly: true,
        minimap: {
          enabled: false
        }
      }}
      onMount={handleEditorDidMount}
    />
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailStatusYAMLWeb);
