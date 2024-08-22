import React, {useEffect, useRef} from "react";
import {ProCard} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";
import Editor, {loader, Monaco, useMonaco} from "@monaco-editor/react";

const DataIntegrationFlinkCDCStepYaml: React.FC = (props: any) => {
  const intl = useIntl();
  const editorRef = useRef(null);
  const monaco = useMonaco();

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
    if (monaco) {
      loader.config({monaco})
    }
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <ProCard>
      <Editor
        width="730"
        height="600px"
        language="yaml"
        theme="vs-white"
        value={props.flinkCDCSteps.instanceYaml}
        options={{
          selectOnLineNumbers: true,
          readOnly: true,
          minimap: {
            enabled: false
          }
        }}
        onMount={handleEditorDidMount}
      />
    </ProCard>
  );
}

const mapModelToProps = ({flinkCDCSteps}: any) => ({flinkCDCSteps})
export default connect(mapModelToProps)(DataIntegrationFlinkCDCStepYaml);
