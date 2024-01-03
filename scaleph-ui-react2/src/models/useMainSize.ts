import { useLayoutEffect, useState } from "react";
import { throttle } from "lodash";

export default (mainCl = ".ant-pro-basicLayout-content") => {
  const [container, setContainer] = useState<{ height: number; width: number }>({
    width: 100,
    height: 100
  });
  // const mainCl = '.ant-pro-basicLayout-content';
  useLayoutEffect(() => {
    const handler = throttle(() => {
      const rt = document.querySelector(mainCl)?.getBoundingClientRect();
      setContainer({
        height: rt?.height,
        width: rt?.width
      });
    }, 100);
    handler();
    window.addEventListener("resize", handler, false);
    return () => {
      window.removeEventListener("resize", handler, false);
    };
  }, []);

  return container;
};
