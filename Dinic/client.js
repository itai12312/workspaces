var dinicClient = {

nodes:new Array(),
edges:new Array(),
disks:new Array(),
moves:new Array(),
bridges:new Array(),
serverResponse:{},
startNode:-1,
stepFrac:0,
heldEdges:{},
heldNodes:{},
nodeRadius:10,
diskRadius:6,
arrowSize:15,
shownTurn:-1,

dist:function(p1,p2) {
	return Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2))
},

screenDist:function(p1,p2) {
    var scr1 = dinicClient.toScreen(p1.x,p1.y)
    var scr2 = dinicClient.toScreen(p2.x,p2.y)
	return dinicClient.dist(scr1,scr2)
},

interpolate:function(a,b,t) {
	return {x:a.x*(1-t)+b.x*t, y:a.y*(1-t)+b.y*t}
},

toScreen:function(x,y) {
    var can = dinicClient.canvas
    return {x:parseInt(x*can.width),
            y:parseInt(y*can.height)}
},

scrEventPos:function(event) {
    var can = dinicClient.canvas
    var rect = can.getBoundingClientRect()
    var eventX = event.pageX-rect.left
    var eventY = event.pageY-rect.top
    return {x: eventX, y: eventY}
},

relEventPos:function(event) {
    scrPos = dinicClient.scrEventPos(event)
    return {x: parseFloat(scrPos.x)/can.width,
            y: parseFloat(scrPos.y)/can.height}
},

fillTriangle:function(ctx,x0,y0,x1,y1,x2,y2) {
	ctx.beginPath()
	ctx.moveTo(x0,y0)
	ctx.lineTo(x1,y1)
	ctx.lineTo(x2,y2)
	ctx.fill()
},

drawArrow:function(ctx,from,to) {
    var edgeColor = "#C0C080"
    var scrFrom = dinicClient.toScreen(from.x, from.y)
    var scrTo = dinicClient.toScreen(to.x, to.y)
	var headFrac = parseFloat(dinicClient.arrowSize)/dinicClient.dist(scrFrom,scrTo)
	var scrHeadBase = {x:scrTo.x*(1-headFrac)+scrFrom.x*headFrac, 
					   y:scrTo.y*(1-headFrac)+scrFrom.y*headFrac}
    //vector from HeadBase to To
	var scrHeadVect = {x:scrTo.x-scrHeadBase.x, y:scrTo.y-scrHeadBase.y}
	//the two base points of the head
	var scrHeadBase1 = {x:scrHeadBase.x-scrHeadVect.y/2,
						y:scrHeadBase.y+scrHeadVect.x/2}
	var scrHeadBase2 = {x:scrHeadBase.x+scrHeadVect.y/2,
						y:scrHeadBase.y-scrHeadVect.x/2}
    ctx.strokeStyle = edgeColor
	ctx.fillStyle = edgeColor
    ctx.lineWidth = 2
    ctx.lineCap = "round"
	ctx.globalAlpha = 0.7
    	ctx.beginPath()
    	ctx.moveTo(scrFrom.x, scrFrom.y)
    	ctx.lineTo(scrHeadBase.x, scrHeadBase.y)
		ctx.stroke()
		ctx.beginPath()
		ctx.moveTo(scrTo.x,scrTo.y)
		ctx.lineTo(scrHeadBase1.x,scrHeadBase1.y)
		ctx.lineTo(scrHeadBase2.x,scrHeadBase2.y)
    	ctx.fill()
	ctx.globalAlpha = 1.0
},

drawEdge:function(ctx,from,to) {
    var radFrac = dinicClient.nodeRadius/dinicClient.screenDist(from,to)
    var middle = dinicClient.interpolate(from,to,0.55)
    var outStart = dinicClient.interpolate(from,to,radFrac)
    var outEnd = dinicClient.interpolate(from,to,1-radFrac)
    dinicClient.drawArrow(ctx,outStart,middle)
    dinicClient.drawArrow(ctx,middle,outEnd)
},

drawNode:function(ctx,node,style) {
    var markColor = "#FF8080"
    var factoryColor1 = "#C0C0FF"
    var factoryColor2 = "#A0A0FF"
    var factoryColor3 = "#8080FF"
    var rad = dinicClient.nodeRadius
    var screen = dinicClient.toScreen(node.x,node.y)
    if (node.type == "regular" || node.type == "splitter") {
        var gradient = ctx.createRadialGradient(screen.x,screen.y,rad/4,
                                                screen.x,screen.y,rad)
        if (node.damage == 0)
            //healthy node
            gradient.addColorStop(0,"#C0C0C0")
        else
            //damaged node
            gradient.addColorStop(0,"#000000")
        gradient.addColorStop(1,"#606060")
        ctx.fillStyle = gradient
        ctx.beginPath()
        ctx.arc(screen.x,screen.y,rad,0,2*Math.PI)
        ctx.fill()
        if (node.type == "splitter") {
            ctx.fillStyle = "#C00000"
            ctx.beginPath()
            ctx.moveTo(screen.x-rad,screen.y)
            ctx.lineTo(screen.x+rad,screen.y)
            ctx.moveTo(screen.x,screen.y-rad)
            ctx.lineTo(screen.x,screen.y+rad)
            ctx.stroke()
        }
    }
    if (node.type == "factory") {
		var trig = dinicClient.fillTriangle
        ctx.fillStyle = factoryColor1
		trig(ctx,screen.x,screen.y,screen.x+rad/1.5,screen.y,screen.x,screen.y-rad)
		ctx.fillStyle = factoryColor2
		trig(ctx,screen.x,screen.y,screen.x+rad/1.5,screen.y,screen.x,screen.y+rad)
		trig(ctx,screen.x,screen.y,screen.x-rad/1.5,screen.y,screen.x,screen.y-rad)
		ctx.fillStyle = factoryColor3
		trig(ctx,screen.x,screen.y,screen.x-rad/1.5,screen.y,screen.x,screen.y+rad)
    }
    if (style == "mark") {
        ctx.globalAlpha = 0.3
        ctx.fillStyle = markColor
        ctx.beginPath()
        ctx.arc(screen.x,screen.y,rad*1.2,0,2*Math.PI)
        ctx.fill()
        ctx.globalAlpha = 1.0
    }
},

drawDisk:function(ctx,type,scrPos) {
    var diskColors = {my:"#4040C0", enemy:"#C04040", green:"#40C040"}
    var rad = dinicClient.diskRadius
    var gradient = ctx.createRadialGradient(scrPos.x+rad/2,scrPos.y-rad/2,0,
                                            scrPos.x,scrPos.y,rad)
    gradient.addColorStop(0,"#FFFFFF")
    gradient.addColorStop(1,diskColors[type])
    ctx.fillStyle = gradient
    ctx.beginPath()
    ctx.arc(scrPos.x,scrPos.y,rad,0,2*Math.PI)
    ctx.fill()
    ctx.beginPath()
},

drawDisks:function(ctx,node) {
    var i,j;
    var rad = dinicClient.diskRadius
    for (i=0;i < dinicClient.nodes.length;i++) {
        var node = dinicClient.nodes[i]
        var counts = dinicClient.diskCounts(i)
        var scrDisk = dinicClient.toScreen(node.x, node.y)
        for (var type in counts)
            for (j=0;j < counts[type];j++) {
                scrDisk.x+=rad
                dinicClient.drawDisk(ctx,type,scrDisk)
            }
    }
},

drawEdgeMovingDisks:function(ctx,edge) {
    var i
    var rad = dinicClient.diskRadius
    var counts = dinicClient.edgeDiskCounts(edge)
    var startNode = dinicClient.nodes[edge.startNode]
    var endNode = dinicClient.nodes[edge.endNode]
    var scrStart = dinicClient.toScreen(startNode.x,startNode.y)
    var scrEnd = dinicClient.toScreen(endNode.x,endNode.y)
    var scrDisk = dinicClient.interpolate(scrStart,scrEnd,dinicClient.stepFrac)
    for (var type in counts)
        for (i=0;i < counts[type];i++) {
            //here the order of operations is reversed in respect to
            //drawDisks because we want the first disk to be on the
            //edge centerline
            dinicClient.drawDisk(ctx,type,scrDisk)
            scrDisk.x+=rad
        }
},

drawMovingDisks:function(ctx) {
    var i
    for (i=0;i < dinicClient.edges.length;i++) {
        var edge = dinicClient.edges[i]
        dinicClient.drawEdgeMovingDisks(ctx,edge)
        dinicClient.drawEdgeMovingDisks(ctx,{startNode: edge.endNode, endNode: edge.startNode})
    }
},

drawBridges:function(ctx) {
	var bridgeColor = "#C78C40"
	var bridgeWidth = 12
	var bridges = dinicClient.bridges
	
	
	ctx.strokeStyle = bridgeColor
	ctx.fillStyle = "#C08000"

	var i,j
    for (i = 0;i < bridges.length;i++) {
		var start = bridges[i].start
		var end = bridges[i].end
		start = dinicClient.toScreen(start.x,start.y)
		end = dinicClient.toScreen(end.x,end.y)
		ctx.lineWidth = 2
		ctx.beginPath()
		ctx.moveTo(start.x+bridgeWidth*0.5,start.y)
		ctx.lineTo(end.x+bridgeWidth*0.5,end.y)
		ctx.lineTo(end.x+bridgeWidth*0.7,end.y+bridgeWidth*0.5)
		ctx.lineTo(end.x-bridgeWidth*0.7,end.y+bridgeWidth*0.5)
		ctx.lineTo(end.x-bridgeWidth*0.5,end.y)
		ctx.lineTo(start.x-bridgeWidth*0.5,start.y)
		ctx.lineTo(start.x-bridgeWidth*0.7,start.y-bridgeWidth*0.5)
		ctx.lineTo(start.x+bridgeWidth*0.7,start.y-bridgeWidth*0.5)
		ctx.closePath()
		ctx.globalAlpha = 0.7
		    ctx.fill()
		ctx.globalAlpha = 1.0
		ctx.stroke()
		ctx.lineWidth = 2
		ctx.beginPath()
		for (j=0;j < 10;j++) {
			var scrLevel = dinicClient.interpolate(start,end,(j+0.5)/10.0)
			ctx.moveTo(scrLevel.x,scrLevel.y+5)
			ctx.lineTo(scrLevel.x,scrLevel.y-5)
		}
		ctx.stroke()
	}
},

sinePhases:new Array(),
sineSpeeds:new Array(),
drawRiver:function(ctx) {
    var riverColor = "#4040C0"
    var sinePoints = 100
    var numWaves = 10
    var wave
    var i
    if (dinicClient.sinePhases.length == 0) {
        //initialize sinePhases and sineSpeeds
        for (i=0;i < numWaves;i++) {
            dinicClient.sinePhases[i] = Math.random()*2*Math.PI
            var speed = Math.random()-0.5
            if (speed > 0)
                speed+=0.5
            else
                speed-=0.5
            dinicClient.sineSpeeds[i] = speed
        }
    }
    ctx.strokeStyle = riverColor
    ctx.lineWidth = 2
    for (wave=0;wave < numWaves;wave++) {
        var pos = dinicClient.toScreen(0,0.4+0.02*(wave+0.5))
        ctx.beginPath()
        for (i=0;i < sinePoints;i++) {
            var angle = i*40/sinePoints+dinicClient.sinePhases[wave]
            var sinePoint = dinicClient.toScreen((i+0.5)/sinePoints, Math.sin(angle)/40)
            if (i == 0)
                ctx.moveTo(pos.x+sinePoint.x,pos.y+sinePoint.y)
            else
                ctx.lineTo(pos.x+sinePoint.x,pos.y+sinePoint.y)
        }
        ctx.stroke()
    }              
},

moveSines:function() {
    var i
    for (i=0;i < dinicClient.sinePhases.length;i++)
        dinicClient.sinePhases[i]+=
            dinicClient.sineSpeeds[i]*0.1
},  

clear:function(ctx) {
    var can = dinicClient.canvas
    ctx.fillStyle = "#000000"
    ctx.fillRect(0,0,can.width,can.height)
},
/*
viaNode:function(fromNode,toNode) {
    var i
    var bridge
    if ("bridge" in toNode)
        bridge = toNode.bridge
    else
        bridge = fromNode.bridge
    document.getElementById("log").innerHTML = "e"+dinicClient.edges.lengt
    for (i=0;i < dinicClient.edges.length;i++) {
        var edge = dinicClient.edges[i]
        var otherEnd = -1
        if (edge.startNode == fromNode)
            otherEnd = edge.endNode
        if (edge.endNode == fromNode)
            otherEnd = edge.startNode
        document.getElementById("log").innerHTML = ""+otherEnd
        if (otherEnd != -1) {
            var otherNode = dinicClient.nodes[otherEnd]
            document.getElementById("log").innerHTML = ""+otherEnd
            if (("bridge" in otherNode) && otherNode.bridge == bridge) {
                
                return otherNode
            }
        }
    }
},*/

diskCounts:function(nodeId) {
    var counts = {my:0, enemy:0, green:0}
    var i
    for (i=0;i < dinicClient.disks.length;i++) {
        var disk = dinicClient.disks[i]
        if (disk.node == nodeId)
            counts[disk.type]+=1
    }
    return counts
},

edgeDiskCounts:function(edge) {
    var counts = {my:0, enemy:0, green:0}
    var i;
    for (i=0;i < dinicClient.moves.length;i++) {
        var move = dinicClient.moves[i]
        if (move.startNode == edge.startNode && move.endNode == edge.endNode)
            counts[move.type]+=1
    }
    return counts
},

frameCount:0,
update:function() {
    
    var ctx = dinicClient.canvas
                  .getContext("2d")

    dinicClient.clear(ctx)
    dinicClient.moveSines()
    dinicClient.drawRiver(ctx)
	dinicClient.drawBridges(ctx)
    
    var markedNodes = new Array()
    for (var eid in dinicClient.heldEdges) {
        var edge = dinicClient.heldEdges[eid]
        markedNodes.push(edge.startNode)
    }
    for (i=0;i < dinicClient.nodes.length;i++) {
        var style;
        if (markedNodes.indexOf(i) != -1)
            style = "mark"
        else
            style = ""
        dinicClient.drawNode(ctx,dinicClient.nodes[i],style)
    }
    
    var nodes = dinicClient.nodes
    for (var eid in dinicClient.heldEdges)  {
        var edge = dinicClient.heldEdges[eid]
        var startNode = nodes[edge.startNode]
        dinicClient.drawEdge(ctx, startNode, edge.endPos)
    }
    
    for (i=0;i < dinicClient.edges.length;i++) {
        if (dinicClient.edges[i].type == "bridge")
            continue
        var start = dinicClient.edges[i].startNode
        var end = dinicClient.edges[i].endNode
        dinicClient.drawEdge(ctx, nodes[start], nodes[end])
    }
    
    if (dinicClient.stepFrac < 1) {
        //move disks
        dinicClient.stepFrac+=0.04
        if (dinicClient.stepFrac >= 1) {
            document.getElementById("log").innerHTML="m"+dinicClient.moves.length
            for (i=0;i < dinicClient.moves.length;i++) {
                var move = dinicClient.moves[i]
                var newDisk = {node: move.endNode, type: move.type}
                dinicClient.disks.push(newDisk)
            }
            dinicClient.moves={}
        }
    }
       
    dinicClient.drawMovingDisks(ctx)
    dinicClient.drawDisks(ctx)
    if ("edges" in dinicClient.serverResponse) {
        //the response is ready
        var response = dinicClient.serverResponse
        if (response.turn != dinicClient.shownTurn) {
            dinicClient.edges = response.edges
            dinicClient.moves = response.moves
            dinicClient.disks = response.disks
            for (i=0;i < dinicClient.bridges.length;i++) {
                var bridge = dinicClient.bridges[i]
                dinicClient.edges.push({startNode: bridge.startNode, endNode: bridge.endNode, type:"bridge"})
                dinicClient.edges.push({startNode: bridge.endNode, endNode: bridge.startNode, type:"bridge"})
            }
            for (nid in response.nodes) {
                dinicClient.nodes[nid].damage = response.nodes[nid].damage
                dinicClient.nodes[nid].type = response.nodes[nid].type
            }
            dinicClient.stepFrac = 0
            dinicClient.shownTurn = response.turn
        }
        dinicClient.serverResponse = {}
    }
    
    dinicClient.frameCount+=1
    //document.getElementById("log").innerHTML=""+dinicClient.disks.length
},

mouseDownFunc:function(event) {
    var scrClick = dinicClient.scrEventPos(event)
    var clickPos = dinicClient.relEventPos(event)
    for (i=0;i < dinicClient.nodes.length;i++) {
        node = dinicClient.nodes[i]
        scrPos = dinicClient.toScreen(node.x,node.y)
        var dist = dinicClient.dist(scrPos,scrClick)
        if (dist < dinicClient.nodeRadius) {
            if (event.which == 3) {
                //right click
                if (node.type != "bridge")
                    dinicClient.heldNodes[7] = i
            } else
                //dragged - was the edge dragged outside the start node
                dinicClient.heldEdges[7] = {startNode: i, endPos: clickPos, dragged: 0}
            break
        }
    }
},

mouseMoveFunc:function(event) {
    var clickPos = dinicClient.relEventPos(event)
    if (7 in dinicClient.heldNodes) {
	    var node = dinicClient.nodes[dinicClient.heldNodes[7]]
	   
	    node.x = clickPos.x
	    node.y = clickPos.y
    }
    if (7 in dinicClient.heldEdges) {
        var edge = dinicClient.heldEdges[7]
        var startNode = dinicClient.nodes[edge.startNode]
        var startPos = dinicClient.toScreen(startNode.x,startNode.y)
        if (dinicClient.dist(startPos,clickPos) > dinicClient.nodeRadius)
            edge.dragged = 1
    	edge.endPos = clickPos
    }
},

mouseUpFunc:function(event) {
    if (7 in dinicClient.heldNodes)
        delete dinicClient.heldNodes[7]
    if (7 in dinicClient.heldEdges) {
        var edge = dinicClient.heldEdges[7]
        if (edge.dragged == 0) {
            //the node was clicked
            dinicClient.requestTypeChange(edge.startNode)
            return
        }
        //an edge was dragged from the node
        var startNode = dinicClient.nodes[edge.startNode]
        var scrClick = dinicClient.scrEventPos(event)
        for (i=0;i < dinicClient.nodes.length;i++) {
            node = dinicClient.nodes[i]
            if (node.owner != startNode.owner)
                continue
            scrPos = dinicClient.toScreen(node.x,node.y)
            var dist = dinicClient.dist(scrPos,scrClick)
            if (dist < dinicClient.nodeRadius && edge.startNode != i) {
                dinicClient.requestEdge(edge.startNode,i)
            }
        }
        delete dinicClient.heldEdges[7]
    }
},

///////////////////
// COMMUNICATION //
///////////////////

requestEdge:function(from,to) {
    dinicServer.requestEdge(from,to)
},

requestTypeChange:function(nid) {
    //todo
},

askServer:function() {
    document.getElementById("log").innerHTML = dinicServer.ask()
    dinicClient.serverResponse = eval('('+dinicServer.ask()+')')
}
}

function clientStart() {
    dinicClient.bridges[0] = {start: {x:0.3, y:0.375}, end: {x:0.3, y:0.625}, startNode: 0, endNode: 1}
	dinicClient.bridges[1] = {start: {x:0.7, y:0.375}, end: {x:0.7, y:0.625}, startNode: 2, endNode: 3}
	
	dinicClient.nodes[0] = {x:0.3,y:0.375,type:"bridge",owner:0}
    dinicClient.nodes[1] = {x:0.3,y:0.625,type:"bridge",owner:1}
    dinicClient.nodes[2] = {x:0.7,y:0.375,type:"bridge",owner:0}
    dinicClient.nodes[3] = {x:0.7,y:0.625,type:"bridge",owner:1}
    
    dinicClient.nodes[4] = {x:0.5,y:0.1,type:"factory",owner:0,damage:0}
    dinicClient.nodes[5] = {x:0.3,y:0.2,type:"regular",owner:0,damage:0}
    dinicClient.nodes[6] = {x:0.3,y:0.3,type:"regular",owner:0,damage:0}
    dinicClient.nodes[7] = {x:0.7,y:0.2,type:"regular",owner:0,damage:0}
    dinicClient.nodes[8] = {x:0.7,y:0.3,type:"regular",owner:0,damage:0}
    
    dinicClient.nodes[9] = {x:0.5,y:0.9,type:"factory",owner:1,damage:0}
    dinicClient.nodes[10] = {x:0.3,y:0.8,type:"regular",owner:1,damage:0}
    dinicClient.nodes[11] = {x:0.3,y:0.7,type:"splitter",owner:1,damage:0}
    dinicClient.nodes[12] = {x:0.7,y:0.8,type:"regular",owner:1,damage:0}
    dinicClient.nodes[13] = {x:0.7,y:0.7,type:"regular",owner:1,damage:0}

	//set canvas size
    var can = document.getElementById("can")
    can.width = window.innerWidth*0.95
    can.height = window.innerHeight*0.95

	//disable context menu
	can.oncontextmenu = function() {
		return false   //prevent context menu from popping
	}

    can.addEventListener("mousedown",dinicClient.mouseDownFunc)
    can.addEventListener("mousemove",dinicClient.mouseMoveFunc)
    can.addEventListener("mouseup",dinicClient.mouseUpFunc)
    dinicClient.canvas = can
    setInterval(dinicClient.update,40)
    setInterval(dinicClient.askServer,400)
}
