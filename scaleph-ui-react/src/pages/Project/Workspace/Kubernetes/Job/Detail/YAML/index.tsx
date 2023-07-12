import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {Props} from '@/app.d';
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";


const FlinkKubernetesJobDetailYAMLWeb: React.FC<Props<WsFlinkKubernetesJob>> = ({data}) => {

  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [job, setJob] = useState<string>()

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    WsFlinkKubernetesJobService.asYaml(data.id).then((response) => {
      setJob(response.data)
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
      value={job}
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

export default FlinkKubernetesJobDetailYAMLWeb;
