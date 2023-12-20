import {connect, useIntl} from "umi";
import React, {useEffect, useRef} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";

const DorisInstanceDetailYAML: React.FC = (props: any) => {
  const intl = useIntl();
  const editorRef = useRef(null);
  const monaco = useMonaco();

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.yaml'})}
                   direction={'row'}>
      <ProCard bordered>
        <Editor
          width="730"
          height="600px"
          language="yaml"
          theme="vs-white"
          value={props.dorisInstanceDetail.instanceYaml}
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
    </ProCard.Group>
  );
}

const mapModelToProps = ({dorisInstanceDetail}: any) => ({dorisInstanceDetail})
export default connect(mapModelToProps)(DorisInstanceDetailYAML);
