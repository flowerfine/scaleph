import React, {useEffect, useRef, useState} from 'react';
import {Drawer} from "antd";
import Editor, {loader, useMonaco} from "@monaco-editor/react";
import * as monaco from "monaco-editor";
import {ModalFormProps} from "@/typings";
import {WsArtifactSeaTunnel} from "@/services/project/typings";
import {WsArtifactSeaTunnelService} from "@/services/project/WsArtifactSeaTunnelService";

loader.config({monaco})

const SeaTunnelConfModal: React.FC<ModalFormProps<WsArtifactSeaTunnel>> = ({
                                                                             visible,
                                                                             onVisibleChange,
                                                                             onCancel,
                                                                             data
                                                                           }) => {
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
    WsArtifactSeaTunnelService.preview(data.id).then((reponse) => {
      setConf(reponse.data)
    })
  }, []);

  return (
    <Drawer
      open={visible}
      width={780}
      styles={{
        body: {overflowY: 'scroll'}
      }}
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

export {SeaTunnelConfModal};
