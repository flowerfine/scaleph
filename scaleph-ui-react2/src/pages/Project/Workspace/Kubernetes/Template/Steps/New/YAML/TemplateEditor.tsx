import React, {useEffect, useRef} from "react";
import {connect} from "umi";
import Editor, {loader, Monaco, useMonaco} from "@monaco-editor/react";
import * as monaco from "monaco-editor";
import {Props} from '@/app';
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";

loader.config({monaco})

const FlinkKubernetesTemplateYAMLStepEditor: React.FC<Props<WsFlinkKubernetesTemplate>> = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={props.flinkKubernetesTemplateSteps.templateYaml}
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

const mapModelToProps = ({flinkKubernetesTemplateSteps}: any) => ({flinkKubernetesTemplateSteps})
export default connect(mapModelToProps)(FlinkKubernetesTemplateYAMLStepEditor);
