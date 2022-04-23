// workbench config

export const WORKBENCH_CONFIG = {
  history: {
    enabled: true,
  },
  background: {
    color: '#F8F8FA', // 设置画布背景颜色
  },
  snapline: {
    enabled: true,
    sharp: true,
  },
  clipboard: {
    enabled: true,
  },
  keyboard: {
    enabled: true,
  },
  selecting: {
    enabled: true,
    rubberband: true,
    multiple: true,
    movable: true,
    strict: true,
    showNodeSelectionBox: true,
    showEdgeSelectionBox: true,
  },

  grid: {
    size: 10,
    visible: true,
    type: 'doubleMesh',
    args: [
      {
        color: '#E7E8EA',
        thickness: 1,
      },
      {
        color: '#CBCED3',
        thickness: 1,
        factor: 4,
      },
    ],
  },
  panning: {
    enabled: true,
    modifiers: 'shift',
  },
};
