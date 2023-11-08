import * as icons from '@ant-design/icons';
import React from 'react';

const Icon = (props: { icon: string }) => {
  const { icon } = props;
  const antIcon: { [key: string]: any } = icons;

  if (!antIcon[icon]) {
    return null;
  }

  return React.createElement(antIcon[icon]);
};

export default Icon;
