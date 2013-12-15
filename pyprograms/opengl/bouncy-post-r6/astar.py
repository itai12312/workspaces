# taken from http://arainyday.se/projects/python/AStar/ with thanks!

# Revamped to use much saner data structures

import sets, time
import collide, objects, euclid

class Path:
    def __init__(self,nodes):
        self.nodes = nodes

    def getNodes(self): 
        return self.nodes    

    def __repr__(self):
        return repr([n.l for n in self.nodes])

    def __nonzero__(self):
        return bool(self.nodes)

class Node:
    parent = None
    score = 0
    def __init__(self, l):
        self.l = l           # where is this node located

    def __eq__(self, n):
        return n.l == self.l

    def __repr__(self):
        return '<Node 0x%X %r>'%(id(self), self.l)

class AStar:

    def __init__(self,maphandler):
        self.mh = maphandler
                
    def _getBestOpenNode(self):
        bestNode = None        
        for nid in self.on:
            n = self.on[nid]
            if not bestNode:
                bestNode = n
            elif n.score <= bestNode.score:
                bestNode = n
        return bestNode

    def _tracePath(self, n):
        nodes = []
        p = n.parent
        nodes.append(n)       
        while  p.parent is not None:
            nodes.append(p)
            p = p.parent
        return Path(nodes)

    def _handleNode(self, node, end):        
        del self.on[node.l]
        self.c.add(node.l)

        for n in self.mh.getAdjacentNodes(node, end, self.c):
            if n.l == end:
                # reached the destination
                return n
            elif n.l in self.on:
                # already in open, check if better score
                on = self.on[n.l]
                if n.score < on.score:
                    self.on[n.l] = n
            else:
                # new node, add to open list
                self.on[n.l] = n

        return None

    def findPath(self, fromlocation, tolocation):
        #s = time.time()
        ret = self._findPath(fromlocation, tolocation)
        #e = time.time()
        #print 'findPath took', e - s
        return ret

    def _findPath(self, fromlocation, tolocation):
        self.on = {}
        self.c = sets.Set()

        end = tolocation
        fnode = self.mh.getNode(fromlocation)
        self.on[fnode.l] = fnode
        nextNode = fnode 
               
        while nextNode is not None: 
            finish = self._handleNode(nextNode, end)
            if finish:                
                return self._tracePath(finish)
            nextNode = self._getBestOpenNode()

        return None
      

class AStarGrid:
    resolution = 4
    IMPASSABLE = 'X'
    PASSABLE = '.'
    def __init__(self):
        self.m = {}

    def toCoords(self, x, y):
        r = self.resolution
        return (int(x - x%r), int(y - y%r))

    def findPath(self, fromlocation, tolocation):
        return AStar(self).findPath(fromlocation, tolocation)

    def add(self, elem):
        box = elem.hitbox
        if not isinstance(box, collide.AABox):
            return
        x = int(box.xmin)
        self.max_y = 0
        r = self.resolution

        for x in range(int(box.xmin - box.xmin%r),
                int(box.xmax - box.xmax % r) + r, r):
            for z in range(int(box.zmin - box.zmin%r),
                    int(box.zmax - box.zmax % r) + r, r):
                c = (x, z)
                #cost = box.dy
                #if isinstance(elem, objects.Fence): cost = self.IMPASSABLE
                #else: cost = self.PASSABLE
                cost = self.IMPASSABLE
                self.max_y = max(self.max_y, cost)
                if c in self.m:
                    self.m[c] = max(self.m[c], cost)
                else:
                    self.m[c] = cost

    def addFarmer(self, farmer):
        ''' fills up the A* map from the farmer to indicate places the
        farmer can go on the map '''
        self._addFarmer(*self.toCoords(farmer.position.x,
            farmer.position.y))
    def _addFarmer(self, x, y):
        self.m[(x,y)] = self.PASSABLE
        r = self.resolution
        for xr in (-r, 0, r):
            for yr in (-r, 0, r):
                if xr == yr == 0: continue
                xm = xr + x
                ym = yr + y
                if self.m.get((xm, ym)) is not None: continue
                self.m[(xm, ym)] = self.PASSABLE
                self._addFarmer(xm, ym)

    def getNode(self, location):
        d = self.m.get(location)
        if d is self.IMPASSABLE:
            return None
        return Node(location)

    def getAdjacentNodes(self, curnode, dest, closed):
        result = []
        cx, cy = curnode.l
        dx, dy = dest
        r = self.resolution
        for xm in (-r, 0, r):
            for ym in (-r, 0, r):
                if xm == ym == 0: continue
                x = cx + xm
                y = cy + ym
                if (x, y) in closed: continue
                n = self._handleNode(x, y, curnode, dx, dy)
                if n: result.append(n)
        return result

    def _handleNode(self, x, y, fromnode, destx, desty):
        n = self.getNode((x, y))
        if n is None: return None
        dx = max(x, destx) - min(x, destx)
        dy = max(y, desty) - min(y, desty)
        n.score = dx + dy
        n.parent = fromnode
        return n

    SEARCH = [
        (resolution, 0),
        (0, resolution),
        (-resolution, 0),
        (0, -resolution),
        (resolution, resolution),
        (-resolution, resolution),
        (-resolution, -resolution),
        (resolution, -resolution),
    ]
    def findClosest(self, sx, sy):
        x, y = self.toCoords(sx, sy)
        sx -= self.resolution / 2
        sy -= self.resolution / 2
        to_search = sets.Set([(x,y)])
        searched = sets.Set()
        while 1:
            if len(searched) == len(self.m):
                raise ValueError, "didn't find a closest point?!?"
            distances = []
            for t in list(to_search):
                if t in searched: continue
                x, y = t
                if self.m.get((x,y)) is self.PASSABLE:
                    distances.append(((sx-x)**2 + (sy-y)**2, (x, y)))
                searched.add(t)
                to_search.remove(t)
                for xm, ym in self.SEARCH:
                    xm += x
                    ym += y
                    t = (xm, ym)
                    if self.m.get(t) is self.PASSABLE:
                        distances.append(((sx-xm)**2 + (sy-ym)**2, t))
                    to_search.add(t)
            if distances:
                distances.sort()
                return distances[0][1]

    def __str__(self):
        xmin = 99; xmax = 0
        ymin = 99; ymax = 0
        for x,y in self.m.keys():
            xmin = min(xmin, x); xmax = max(xmax, x)
            ymin = min(ymin, y); ymax = max(ymax, y)
        print 'X %s -> %s;   Y %s -> %s'%(xmin, xmax, ymin, ymax)
        l = []
        for y in range(ymin, ymax + 4, 4):
            r = ['%3d '%y]
            for x in range(xmin, xmax + 4, 4):
                r.append(self.m.get((x,y)) or ' ')
            l.append(''.join(r))
        return '\n'.join(l)

