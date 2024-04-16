/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.dag.algorithm;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DAG<N> {

    private Graph<N, DefaultEdge> jgrapht = GraphTypeBuilder.<N, DefaultEdge>directed()
            .allowingSelfLoops(false)
            .weighted(false)
            .buildGraph();

    public Set<N> getSources() {
        return jgrapht.vertexSet().stream()
                .filter(node -> jgrapht.inDegreeOf(node) == 0)
                .collect(Collectors.toSet());
    }

    public Set<N> getSinks() {
        return jgrapht.vertexSet().stream()
                .filter(node -> jgrapht.outDegreeOf(node) == 0)
                .collect(Collectors.toSet());
    }

    public Integer getMaxDepth() {
        AllDirectedPaths<N, DefaultEdge> paths = new AllDirectedPaths<>(jgrapht);
        return paths.getAllPaths(getSources(), getSinks(), true, null)
                .stream().map(GraphPath::getLength)
                .sorted()
                .findFirst().get();
    }

    public void topologyTraversal(Consumer<N> consumer) {
        TopologicalOrderIterator<N, DefaultEdge> iterator = new TopologicalOrderIterator<>(jgrapht);
        while (iterator.hasNext()) {
            consumer.accept(iterator.next());
        }
    }

    public void breadthFirstTraversal(Consumer<N> consumer) {
        BreadthFirstIterator<N, DefaultEdge> iterator = new BreadthFirstIterator<>(jgrapht);
        while (iterator.hasNext()) {
            N node = iterator.next();
            int depth = iterator.getDepth(node);
            consumer.accept(node);
        }
    }

    /**
     * 一层一层执行
     */
    public void executeBFS(Consumer<N> consumer) {
        doExecuteBFS(0, getMaxDepth(), consumer);
    }

    public void doExecuteBFS(Integer depth, Integer maxDepth, Consumer consumer) {
        // 使用 BFS，计算每个节点所在的层级
        BreadthFirstIterator<N, DefaultEdge> iterator = new BreadthFirstIterator<>(jgrapht);
        while (iterator.hasNext()) {
            N node = iterator.next();
            if (iterator.getDepth(node) == depth) {
                consumer.accept(node);
            }
        }
        if (depth <= maxDepth) {
            // 开始遍历第二层
            doExecuteBFS(depth + 1, maxDepth, consumer);
        }
    }
}
