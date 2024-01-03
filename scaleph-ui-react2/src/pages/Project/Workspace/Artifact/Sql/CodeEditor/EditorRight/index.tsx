import { useRef, useState } from 'react';
import Split from 'react-split';
import Editor from '../Editor';
import EditorRightResult from '../EditorRightResult';
import './index.less';

export default function EditorLeft() {
  const [verticalSplitSizes, setVerticalSplitSizes] = useState<number[]>([70, 30]);
  const [tableSize, setTableSize] = useState<number[]>([70, 30]);
  const editorRef = useRef<monaco.editor.IEditor>();

  const handleDrag = (sizes: number[]) => {
    setVerticalSplitSizes(sizes);
    if (editorRef.current) {
      const editorHeight = sizes[0];
      editorRef.current.layout({ height: `${editorHeight}%` });
    }
  };

  return (
    <Split
      className="split-vertical"
      direction="vertical"
      gutterSize={4}
      sizes={verticalSplitSizes}
      minSize={[0, 0]}
      maxSize={[Infinity, Infinity]}
      snapOffset={100}
      onDrag={handleDrag}
      onDragEnd={(sizes: number[]) => {
        setTableSize(sizes)
      }}
    >
      <div>
        <Editor editorRef={editorRef} />
      </div>
      <div>
        <EditorRightResult editorRef={editorRef} verticalSplitSizes={tableSize} />
      </div>
    </Split>

    // <div>
    //    <Editor editorRef={editorRef} />
    // </div>
  );
}
