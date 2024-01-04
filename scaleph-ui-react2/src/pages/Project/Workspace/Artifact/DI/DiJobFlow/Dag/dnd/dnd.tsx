import React, {useEffect, useState} from 'react';
import {Descriptions, Popover, Tag, Tree} from 'antd';
import {DatabaseFilled, HolderOutlined} from '@ant-design/icons';
import {useDnd} from '@antv/xflow';
import SearchInput from './search';
import {DAG_NODE} from '../shape';
import {Props} from "@/typings";
import {WsDiJob} from "@/services/project/typings";
import {WsSeaTunnelService} from "@/services/project/SeaTunnelService";
import styles from './dnd.less';
import {BaseNode} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/base-node";

const {DirectoryTree} = Tree;

type ComponentTreeItem = {
    category: string;
    docString: string;
    isLeaf: boolean;
    key: string;
    title: string;
    children?: ComponentTreeItem[];
    ports?: {
        id: string;
        group: string;
    }[];
};

const Dnd: React.FC<Props<WsDiJob>> = ({data}) => {
    const {startDrag} = useDnd();
    const [treeItems, setTreeItems] = useState<ComponentTreeItem[]>([]);
    const [searchComponents, setSearchComponents] = useState<ComponentTreeItem[]>([]);
    let id = 0;

    useEffect(() => {
        WsSeaTunnelService.getDnds(data.jobEngine?.value).then((response) => {
            if (response.success) {
                setTreeItems(response.data)
            }
        })
    }, []);

    const handleMouseDown = (
        e: React.MouseEvent<Element, MouseEvent>,
        item: ComponentTreeItem,
    ) => {
        id += 1;
        startDrag(
            {
                id: id.toString(),
                shape: DAG_NODE,
                data: {
                    id: id.toString(),
                    label: item.title,
                    status: 'default',
                },
                ports: item.ports,
            },
            e,
        );
    };

    const handleSearchComponent = (keyword?: string) => {
        if (!keyword) {
            setSearchComponents([]);
            return;
        }
        const searchResult = treeItems.flatMap((group) =>
            group.children.filter((child) =>
                child.title.toLowerCase().includes(keyword.toLowerCase()),
            ),
        );
        setSearchComponents(searchResult);
    };

    const treeNodeRender = (treeNode: ComponentTreeItem) => {
        const {isLeaf, docString, title} = treeNode;
        if (isLeaf) {
          return <BaseNode data={treeNode}/>
            // return (
            //     <Popover
            //         content={
            //             <div
            //                 style={{
            //                     width: 200,
            //                     wordWrap: 'break-word',
            //                     whiteSpace: 'pre-wrap',
            //                     overflow: 'auto',
            //                 }}
            //             >
            //                 {docString}
            //                 <Descriptions style={{ maxWidth: '240px' }} size="small" column={1}>
            //                     <Descriptions.Item>{docString}</Descriptions.Item>
            //                 </Descriptions>
            //                 {treeNode?.health && <Tag color="red">{treeNode?.health?.label}</Tag>}
            //                 {treeNode?.features &&
            //                     treeNode.features.map((item) => {
            //                         return <Tag color="green">{item.label}</Tag>;
            //                     })}
            //             </div>
            //         }
            //         placement="right"
            //     >
            //         <div
            //             className={styles.node}
            //             onMouseDown={(e) => handleMouseDown(e, treeNode)}
            //         >
            //             <div className={styles.nodeTitle}>
            //   <span className={styles.icon}>
            //     <DatabaseFilled style={{color: '#A1AABC'}}/>
            //   </span>
            //                 <span>{title}</span>
            //             </div>
            //             <div className={styles.nodeDragHolder}>
            //                 <HolderOutlined/>
            //             </div>
            //         </div>
            //     </Popover>
            // );
        } else {
            return <span className={styles.dir}>{title}</span>;
        }
    };

    return (
        <div className={styles.components}>
            <div className={styles.action}>
                <SearchInput
                    className={styles.search}
                    placeholder="请输入搜索关键字"
                    onSearch={(key) => handleSearchComponent(key)}
                />
            </div>
            {treeItems.length && (
                <DirectoryTree
                    rootClassName={styles.tree}
                    blockNode
                    showIcon={false}
                    defaultExpandAll
                    titleRender={(node) => treeNodeRender(node)}
                    treeData={searchComponents.length ? searchComponents : treeItems}
                />
            )}
        </div>
    );
};

export {Dnd};
