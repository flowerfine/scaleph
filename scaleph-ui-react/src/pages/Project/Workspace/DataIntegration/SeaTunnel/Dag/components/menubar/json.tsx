import React, {useEffect, useRef, useState} from 'react';
import {Drawer} from "antd";
import {useGraphStore} from '@antv/xflow';
import Editor, {loader, useMonaco} from "@monaco-editor/react";
import * as monaco from "monaco-editor";
import {ModalFormProps} from "@/typings";

loader.config({monaco})

const JSONDebugModal: React.FC<ModalFormProps<null>> = ({visible, onVisibleChange, onCancel}) => {
  const nodes = useGraphStore((state) => state.nodes);
  const edges = useGraphStore((state) => state.edges);
  const editorRef = useRef(null);
  const monaco = useMonaco();
  const [conf, setConf] = useState<string>();

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
    // or make sure that it exists by other ways
    if (monaco) {
      // console.log("here is the monaco instance:", monaco);
    }
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco) => {
    editorRef.current = editor;
  }
  useEffect(() => {
    setConf(JSON.stringify({nodes, edges}, null, 2))
  }, [nodes, edges]);

  return (
    <Drawer
      open={visible}
      width={780}
      bodyStyle={{overflowY: 'scroll'}}
      destroyOnClose={true}
      onClose={onCancel}
    >
      <Editor
        width="780"
        language="json"
        theme="vs-dark"
        value={conf}
        options={{
          selectOnLineNumbers: true,
          readOnly: true,
          minimap: {
            enabled: false
          }
        }}
        onMount={handleEditorDidMount}
      />
    </Drawer>
  );
};

export {JSONDebugModal};
