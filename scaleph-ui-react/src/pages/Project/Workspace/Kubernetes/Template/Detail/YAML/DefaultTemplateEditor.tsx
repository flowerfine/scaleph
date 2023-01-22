import {useModel} from "umi";
import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";

const DefaultTemplateEditor: React.FC = () => {
  const [editorValue, setEditorValue] = useState<string>()
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const {deploymentTemplate} = useModel('deploymentTemplateYAMLEditor', (model) => ({
    deploymentTemplate: model.deploymentTemplate
  }));

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    if (deploymentTemplate) {
      WsFlinkKubernetesDeploymentTemplateService.mergeDefault(YAML.parse(deploymentTemplate)).then((response) => {
        console.log("response", response)
        // setEditorValue(deploymentTemplate)
      })
    }
    setEditorValue(deploymentTemplate)
  }, [deploymentTemplate]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={editorValue}
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

export default DefaultTemplateEditor;
