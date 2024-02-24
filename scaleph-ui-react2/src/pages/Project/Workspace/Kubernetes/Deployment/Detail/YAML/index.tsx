import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {Props} from '@/app.d';
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";

const FlinkKubernetesDeploymentDetailYAMLWeb: React.FC<Props<WsFlinkKubernetesDeployment>> = ({data}) => {

  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [deployment, setDeployment] = useState<string>()

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    WsFlinkKubernetesDeploymentService.asYaml(data).then((response) => {
      setDeployment(YAML.stringify(response.data))
    })
  }, []);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={deployment}
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

export default FlinkKubernetesDeploymentDetailYAMLWeb;
