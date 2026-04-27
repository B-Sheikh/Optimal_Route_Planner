export function dijkstra(graph, startNode, endNode) {
  const distances = {};
  const prev = {};
  const nodes = new Set();

  for (let node in graph) {
    distances[node] = Infinity;
    prev[node] = null;
    nodes.add(node);
  }
  distances[startNode] = 0;

  while (nodes.size > 0) {
    let closestNode = null;
    for (let node of nodes) {
      if (!closestNode || distances[node] < distances[closestNode]) {
        closestNode = node;
      }
    }

    if (distances[closestNode] === Infinity) break;
    if (closestNode === endNode) break;

    nodes.delete(closestNode);

    for (let neighbor in graph[closestNode]) {
      let alt = distances[closestNode] + graph[closestNode][neighbor];
      if (alt < distances[neighbor]) {
        distances[neighbor] = alt;
        prev[neighbor] = closestNode;
      }
    }
  }

  const path = [];
  let curr = endNode;
  while (curr) {
    path.unshift(curr);
    curr = prev[curr];
  }

  return { path, distance: distances[endNode] };
}

export function buildGraph(roads) {
  const graph = {};
  roads.forEach(({ from, to, dist }) => {
    if (!graph[from]) graph[from] = {};
    if (!graph[to]) graph[to] = {};
    graph[from][to] = dist;
    graph[to][from] = dist;
  });
  return graph;
}

export function planMultiStopRoute(graph, start, stops) {
  let currentPos = start;
  let remainingStops = [...stops];
  let fullRoute = [start];
  let totalDistance = 0;

  while (remainingStops.length > 0) {
    let nearestIdx = -1;
    let shortestPath = null;
    let minD = Infinity;

    remainingStops.forEach((stop, idx) => {
      const { path, distance } = dijkstra(graph, currentPos, stop);
      if (distance < minD) {
        minD = distance;
        nearestIdx = idx;
        shortestPath = path;
      }
    });

    if (nearestIdx === -1) break;

    totalDistance += minD;
    // Append path (excluding the start which is already in fullRoute)
    fullRoute.push(...shortestPath.slice(1));
    currentPos = remainingStops[nearestIdx];
    remainingStops.splice(nearestIdx, 1);
  }

  // Return to start
  const { path: returnPath, distance: returnDist } = dijkstra(graph, currentPos, start);
  fullRoute.push(...returnPath.slice(1));
  totalDistance += returnDist;

  return { route: fullRoute, totalDistance };
}
