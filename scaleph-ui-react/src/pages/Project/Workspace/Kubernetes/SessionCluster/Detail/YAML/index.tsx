import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {Props} from '@/app.d';
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";

const FlinkKubernetesSessinClusterDetailYAMLWeb: React.FC<Props<WsFlinkKubernetesSessionCluster>> = ({data}) => {

  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [sessionCluster, setSessionCluster] = useState<string>()

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    if (data.state) {
      WsFlinkKubernetesSessionClusterService.status(data).then((response) => {
        setSessionCluster(YAML.stringify(response.data))
      })
    } else {
      WsFlinkKubernetesSessionClusterService.asYAML(data).then((response) => {
        setSessionCluster(YAML.stringify(response.data))
      })
    }
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
      value={sessionCluster}
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

export default FlinkKubernetesSessinClusterDetailYAMLWeb;
