import {DsCategoryService} from "@/services/datasource/category.service";
import {useEffect, useState} from "react";
import {Menu} from "antd";
import {ItemType} from "antd/lib/menu/hooks/useItems";

const DataSourceCategoryMenu: React.FC<{ onCategoryChange: (id: number) => void }> = ({onCategoryChange}) => {
  const [categoryMenus, setCategoryMenus] = useState<ItemType[]>([])

  useEffect(() => {
    DsCategoryService.list().then((response) => {
      if (response.data) {
        const cateories = response.data.map((category) => {
          return {
            key: category.id,
            label: category.name,
            title: category.remark
          }
        });
        setCategoryMenus(cateories)
      }
    })
  }, []);

  return (
    <Menu
      items={categoryMenus}
      onSelect={({item, key, keyPath, selectedKeys, domEvent}) => {
        onCategoryChange(parseInt(key))
      }}
    />
  );
}

export default DataSourceCategoryMenu;
