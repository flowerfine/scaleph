import {useModel} from "umi";
import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";

const TemplateEditor: React.FC = () => {
  const [conf, setConf] = useState<string>();

  const confData = "apiVersion: v1\n" +
    "kind: DeploymentDefaults\n" +
    "metadata:\n" +
    "  createdAt: '2023-01-20T03:34:46.336356Z'\n" +
    "  id: 9b89a9c7-fda4-488a-8fb7-073f176ba591\n" +
    "  modifiedAt: '2023-01-20T03:34:46.336359Z'\n" +
    "  name: default\n" +
    "  namespace: default\n" +
    "  resourceVersion: 1\n" +
    "spec:\n" +
    "  deploymentTargetName: '111'\n" +
    "  restoreStrategy:\n" +
    "    allowNonRestoredState: false";

  const editorRef = useRef(null);
  const monaco = useMonaco();

  const {setDeploymentTemplate} = useModel('deploymentTemplateYAMLEditor', (model) => ({
    setDeploymentTemplate: model.setDeploymentTemplate
  }));

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    setConf(confData)
  }, []);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  const handleValueChange = (value, event) => {
    setDeploymentTemplate(value)
    console.log("handleValueChange", YAML.parse(value))
    // setConf(YAML.stringify(YAML.parse(value)))
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={conf}
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
