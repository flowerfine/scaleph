import { uuidv4 } from '@antv/xflow';

const getAnchorStyle = (position: string) => {
  return {
    position: { name: position },
    attrs: {
      circle: {
        r: 4,
        magnet: true,
        stroke: '#31d0c6',
        strokeWidth: 2,
        fill: '#fff',
        style: {
          visibility: 'hidden',
        },
      },
    },
    zIndex: 10,
  };
};

const getPorts = (position = ['top', 'right', 'bottom', 'left']) => {
  return {
    items: position.map((name) => {
      return { group: name, id: uuidv4() };
    }),
    groups: {
      top: getAnchorStyle('top'),
      right: getAnchorStyle('right'),
      bottom: getAnchorStyle('bottom'),
      left: getAnchorStyle('left'),
    },
  };
};

export default getPorts;
