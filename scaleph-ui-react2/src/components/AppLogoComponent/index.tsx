import React from "react";

const publicPath = process.env.REACT_APP_ENV === 'dist' ? '/scaleph/ui' : '';

const AppsLogo: React.FC<{ width: number; height: number; color: string }> = (
  props
) => {
  const { width, height, color } = props;
  return (
    <img width={width} height={height} color={color} alt="logo" src={`${publicPath}/scaleph.svg`} />
  );
};

export default AppsLogo;
