import React, {useEffect, useRef} from "react";
import Editor, {Monaco, loader, useMonaco} from "@monaco-editor/react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {connect} from "umi";

const DorisInstanceDetailYAMLStatus: React.FC<Props<WsFlinkKubernetesTemplate>> = (props: any) => {
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
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={props.dorisInstanceDetail.instanceStatusYaml}
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
const mapModelToProps = ({dorisInstanceDetail}: any) => ({dorisInstanceDetail})
export default connect(mapModelToProps)(DorisInstanceDetailYAMLStatus);
