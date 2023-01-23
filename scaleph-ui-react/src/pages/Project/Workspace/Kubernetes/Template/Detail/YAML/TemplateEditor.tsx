import {useModel} from "umi";
import React, {useEffect, useRef} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {Props} from '@/app.d';
import {WsFlinkKubernetesDeploymentTemplate} from "@/services/project/typings";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";

const TemplateEditor: React.FC<Props<WsFlinkKubernetesDeploymentTemplate>> = ({data}) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const {deploymentTemplate, setDeploymentTemplate} = useModel('deploymentTemplateYAMLEditor');

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    WsFlinkKubernetesDeploymentTemplateService.asTemplate(data).then((response) => {
      if (response.data) {
        setDeploymentTemplate(YAML.stringify(response.data))
      }
    })
  }, [data]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  const handleValueChange = (value, event) => {
    setDeploymentTemplate(value)
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={deploymentTemplate}
      options={{
        selectOnLineNumbers: true,
        readOnly: false,
        minimap: {
          enabled: false
        }
      }}
      onMount={handleEditorDidMount}
      onChange={handleValueChange}
    />
  );
}

export default TemplateEditor;
