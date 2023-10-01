import { useState } from 'react';
import Split from 'react-split';
import EditMenu from './EditMenu';
import EditorRight from './EditorRight';
import './index.less';

const Index: React.FC = () => {
  const [horizontalSplitSizes, setHorizontalSplitSizes] = useState<number[]>([15, 85]); // 水平分割条大小比例的状态

  const [showLeft, setShowLeft] = useState<boolean>(true); // 是否显示左侧菜单栏

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
        <EditMenu showLeft={showLeft} />
        <EditorRight />
      </Split>
    </>
  );
};

export default Index;
