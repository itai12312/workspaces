var dinicServer = {
turnsPassed:0,
turnsComputed:0,
edges:new Array(),
nodes:new Array(),
nodeDisks:{},
moves:new Array(),

regularCapacity: 5,

otherEnd:function(edge,end) {
    if (edge.startNode == end)
        return edge.endNode
    return edge.startNode
},

outEdges:function(nid) {
    var i
    var result = new Array()
    for (i=0;i < dinicServer.edges.length;i++) {
        var edge = dinicServer.edges[i]
        if (edge.startNode == nid)
            result.push(edge)
    }
    return result
},

edgesExcept:function(nid,except) {
    var i
    var result = new Array()
    for (i=0;i < dinicServer.edges.length;i++) {
        var edge = dinicServer.edges[i]
        if (edge.startNode == nid && edge.endNode != except)
            result.push(edge)
        if (edge.endNode == nid && edge.startNode != except)
            result.push(edge)
    }
    return result
},

innerEdgesExcept:function(nid,except) {
    var i
    var result = new Array()
    for (i=0;i < dinicServer.edges.length;i++) {
        var edge = dinicServer.edges[i]
        if (edge.startNode == nid || edge.endNode == nid) {
            var other = dinicServer.otherEnd(edge,nid)
            var otherNode = dinicServer.nodes[other]
            if (otherNode.type != "bridge" && other != except)
                result.push(edge)
        }
    }
    return result
},

doMoves:function() {
    var i
    for (i=0;i < dinicServer.moves.length;i++) {
        var move = dinicServer.moves[i]
        dinicServer.nodeDisks[move.endNode]
            .push({type: move.type, owner:move.owner, source:move.startNode})
    }
},

healNodes:function() {
    var i
    for (i=0;i < dinicServer.nodes.length;i++) {
        var node = dinicServer.nodes[i]
        if (node.damage > 0)
            node.damage-=1
    }
},

collapse:function(nid) {
    var edgesLeft = new Array()
    var i
    for (i=0;i < dinicServer.edges.length;i++) {
        var edge = dinicServer.edges[i]
        if (edge.startNode != nid && edge.endNode != nid)
            edgesLeft.push(edge)
    }
    dinicServer.edges = edgesLeft
    dinicServer.nodeDisks[nid] = new Array()
    dinicServer.nodes[nid].damage = 4
},

checkCollapse:function(nid) {
    var i
    var node = dinicServer.nodes[nid]
    var queue = dinicServer.nodeDisks[nid]
    if (node.type == "regular") {
        if (queue.length > dinicServer.regularCapacity) {
            dinicServer.collapse(nid)
            return
        }
    }
},

runFactory:function(nid) {
    var outs = dinicServer.outEdges(nid)
    var owner = dinicServer.nodes[nid].owner
    if (outs.length == 0)
        return
    var selectedOut = parseInt(Math.random()*outs.length)
    var endNode = outs[selectedOut].endNode
    dinicServer.moves.push({startNode: nid,
                            endNode: endNode,
                            type: "atk",
                            owner: owner})
},

runRegular:function(nid) {
    var i
    var queue = dinicServer.nodeDisks[nid]
    if (queue.length == 0)
        return
    if (queue.length > dinicServer.regularCapacity) {
        dinicServer.collapse(nid)
        return
    }
    var disk = queue.shift()
    var node = dinicServer.nodes[nid]
    if (disk.owner == node.owner)
        var outs = dinicServer.outEdges(nid)
    else
        var outs = dinicServer.innerEdgesExcept(nid,disk.from)
    if (outs.length == 0) {
        queue.unshift(disk)
        return
    }
    var selectedOut = parseInt(Math.random()*outs.length)
    var endNode = outs[selectedOut].endNode
    dinicServer.moves.push({startNode: nid,
                            endNode: endNode,
                            type: disk.type,
                            owner: disk.owner})
},

runBridge:function(nid) {
    var i
    var node = dinicServer.nodes[nid]
    var queue = dinicServer.nodeDisks[nid]
    var outs = dinicServer.outEdges(nid)
    var enemyOuts = dinicServer.innerEdgesExcept(nid,-1)
    while (queue.length != 0) {
        var disk = queue.shift()
        if (disk.source == node.partner) {
            if (disk.owner == node.owner)
                diskOuts = outs
            else
                diskOuts = enemyOuts
            if (diskOuts.length != 0) {
                if (diskOuts == enemyOuts)
                    console.log(diskOuts)
                var selectedOut = parseInt(Math.random()*diskOuts.length)
                var endNode = dinicServer.otherEnd(diskOuts[selectedOut],nid)
                dinicServer.moves.push({startNode: nid,
                            endNode: endNode,
                            type: disk.type,
                            owner: disk.owner})
            }  
        } else {
            dinicServer.moves.push({startNode: nid,
                            endNode: node.partner,
                            type: disk.type,
                            owner: disk.owner})
        }
    }
},      

computeTurn:function() {
    var i;
    dinicServer.doMoves()
    dinicServer.moves = new Array()
    for (i=0;i < dinicServer.nodes.length;i++) {
        var node = dinicServer.nodes[i]
        if (node.type == "regular")
            dinicServer.checkCollapse(i)
    }
    for (i=0;i < dinicServer.nodes.length;i++) {
        var node = dinicServer.nodes[i]
        if ("damage" in node && node.damage > 0)
            continue
        if (node.type == "factory")
            dinicServer.runFactory(i)
        if (node.type == "regular")
            dinicServer.runRegular(i)
        if (node.type == "bridge")
            dinicServer.runBridge(i)
    }
    dinicServer.healNodes()
    dinicServer.turnsComputed+=1
},

updateState:function() {
    while (dinicServer.turnsComputed < dinicServer.turnsPassed)
        dinicServer.computeTurn()
},

requestEdge:function(from,to) {
    dinicServer.updateState()
    var i
    for (i=0;i < dinicServer.edges.length;i++) {
        var edge = dinicServer.edges[i]
        if (edge.startNode == from && edge.endNode == to) {
            dinicServer.edges.splice(i,1)  //remove the edge
            return
        }
    }
    if (dinicServer.nodes[from].damage > 0)
        return;
    if (dinicServer.nodes[to].damage > 0)
        return;
    dinicServer.edges.push({startNode: from, endNode: to, type:"regular"})
},

diskList:function() {
    var result = new Array()
    for (var nid in dinicServer.nodeDisks) {
        var disks = dinicServer.nodeDisks[nid]
        for (i=0;i < disks.length;i++) {
            var clientDisk = JSON.parse(JSON.stringify(disks[i]))
            clientDisk.node = nid
            result.push(clientDisk)
        }
    }
    return result
},

nodesDict:function() {
    var i
    var result = {}
    for (i=0;i < dinicServer.nodes.length;i++) {
        var node = dinicServer.nodes[i]
        if ("damage" in node)
            result[i] = {damage: node.damage,
                         type: node.type}
    }
    return result
},

translate:function(array) {
    var result = new Array()
    for (i=0;i < array.length;i++) {
        var translated = {}
        for (attr in array[i]) {
            if (attr != "owner" && attr != "type" && attr != "source")
                translated[attr] = array[i][attr]
        }
        if (array[i].type == "atk") {
            if (array[i].owner == 0)
                translated["type"] = "enemy"
             else
                translated["type"] = "my"
        }
        result.push(translated)
    }
    return result
},      

ask:function() {
    dinicServer.updateState()
    var moves = dinicServer.translate(dinicServer.moves)
    var diskList = dinicServer.diskList()
    var disks = dinicServer.translate(diskList)
    var nodesDict = dinicServer.nodesDict()
    var response = {turn: dinicServer.turnsComputed,
                    edges: dinicServer.edges,
                    moves: moves,
                    disks: disks,
                    nodes: nodesDict}
    return JSON.stringify(response)
},

advanceTimer:function() {
    dinicServer.turnsPassed+=1
}
}

function serverStart() {
    dinicServer.nodes[0] = {x:0.3,y:0.375,type:"bridge",owner:0}
    dinicServer.nodes[1] = {x:0.3,y:0.625,type:"bridge",owner:1}
    dinicServer.nodes[2] = {x:0.7,y:0.375,type:"bridge",owner:0}
    dinicServer.nodes[3] = {x:0.7,y:0.625,type:"bridge",owner:1}
    
    dinicServer.nodes[4] = {x:0.5,y:0.1,type:"factory",owner:0,damage:0}
    dinicServer.nodes[5] = {x:0.3,y:0.2,type:"regular",owner:0,damage:0}
    dinicServer.nodes[6] = {x:0.3,y:0.3,type:"regular",owner:0,damage:0}
    dinicServer.nodes[7] = {x:0.7,y:0.2,type:"regular",owner:0,damage:0}
    dinicServer.nodes[8] = {x:0.7,y:0.3,type:"regular",owner:0,damage:0}
    
    dinicServer.nodes[9] = {x:0.5,y:0.9,type:"factory",owner:1,damage:0}
    dinicServer.nodes[10] = {x:0.3,y:0.8,type:"regular",owner:1,damage:0}
    dinicServer.nodes[11] = {x:0.3,y:0.7,type:"splitter",owner:1,damage:0}
    dinicServer.nodes[12] = {x:0.7,y:0.8,type:"regular",owner:1,damage:0}
    dinicServer.nodes[13] = {x:0.7,y:0.7,type:"regular",owner:1,damage:0}
    
    var i
    for (i=0;i < dinicServer.nodes.length;i++)
        dinicServer.nodeDisks[i] = new Array()
    
    setInterval(dinicServer.advanceTimer,2000)
}