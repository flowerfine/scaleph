import { useState } from 'react';
import Split from 'react-split';
import EditMenu from './EditMenu';
import EditorLeft from './EditorLeft';
import "./index.less";

export default function index() {
  const [horizontalSplitSizes, setHorizontalSplitSizes] = useState<number[]>([20, 80]);

  const [showLeft, setShowLeft] = useState<boolean>(true);

  return (
    <>
      <Split
        className={'split-horizontal'}
        direction="horizontal"
        gutterSize={4}
        sizes={horizontalSplitSizes}
        minSize={[0, 0]}
        maxSize={[680, Infinity]}
        snapOffset={100}
        onDrag={(sizes: number[]) => {
          if (sizes[0] <= 6) {
            setShowLeft(false);
          } else {
            setShowLeft(true);
          }
          setHorizontalSplitSizes(sizes);
        }}
      >
        <EditMenu showLeft={showLeft}/>
        <EditorLeft />
      </Split>
    </>
  );
}
