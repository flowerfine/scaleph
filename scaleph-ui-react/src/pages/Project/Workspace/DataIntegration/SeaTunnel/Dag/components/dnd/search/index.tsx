import { useRef, useState } from 'react';
import { Input } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import classnames from 'classnames';

import styles from './index.less';

interface IProps {
  className?: string;
  placeholder: string;
  onSearch: (key: string) => void;
}

export const SearchInput = (props: IProps) => {
  const inputRef = useRef(null);
  const searchTimeout = useRef(0);
  const [value, setValue] = useState('');

  const handleSearch = (evt: React.FocusEvent<HTMLInputElement>) => {
    const key = evt.target.value;
    setValue(key);
    if (searchTimeout.current) clearTimeout(searchTimeout.current);
    searchTimeout.current = window.setTimeout(() => {
      props.onSearch(key);
      searchTimeout.current = 0;
    }, 200);
  };

  return (
    <Input
      ref={inputRef}
      className={classnames(styles.search, props.className)}
      prefix={<SearchOutlined />}
      allowClear
      value={value}
      onChange={handleSearch}
      size="small"
      placeholder={props.placeholder}
    />
  );
};

export default SearchInput;
