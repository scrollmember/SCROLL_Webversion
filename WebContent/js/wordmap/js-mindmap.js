/*
 js-mindmap

 Copyright (c) 2008/09/10 Kenneth Kufluk http://kenneth.kufluk.com/

 MIT (X11) license

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

*/

/*
	Things to do:
		- add better "make active" methods
		- remove the "root node" concept.	Tie nodes to elements better, so we can check if a parent element is root

		- allow progressive exploration
			- allow easy supplying of an ajax param for loading new kids and a loader anim
		- allow easy exploration of a ul or ol to find nodes
		- limit to an area
		- allow more content (div instead of an a)
		- test multiple canvases
		- Hidden children should not be bounded
		- Layout children in circles
		- Add/Edit nodes
		- Resize event
		- incorporate widths into the forces, so left boundaries push on right boundaries


	Make demos:
		- amazon explore
		- directgov explore
		- thesaurus
		- themes

*/

(function ($) {
	'use strict';

	var TIMEOUT = 4,	// movement timeout in seconds
		CENTRE_FORCE = 3,	// strength of attraction to the centre by the active node
		Area = null,
		Node;

	// Define all Node related functions.
	Node = function (obj, name, parent, opts) {
		this.obj = obj;
		this.options = obj.options;

		this.name = name;
		this.href = opts.href;
		this.class_name = opts.class_name;
		if (opts.url) {
			this.url = opts.url;
		}
		this.onclick = opts.onclick;

		// create the element for display
		this.el = $('<a href="' + this.href + '">' + this.name + '</a>')
		.addClass('node')
		.addClass(this.class_name)
		.css({'position': 'absolute'});
		
		if (!parent) {
			obj.activeNode = this;
			this.el.addClass('active root');
		}
		this.parent = parent;
		if (this.parent !== null) {
			this.parent.children.push(this);
			this.parent.content.push(this.el);
		}
		
		// animation handling
		this.moving = false;
		this.moveTimer = 0;
		this.obj.movementStopped = false;
		this.visible = true;
		this.x = 1;
		this.y = 1;
		this.dx = 0;
		this.dy = 0;
		this.hasPosition = false;
		
		this.children = new Array;
		this.content = new Array; // array of content elements to display onclick;
		
		this.visible = opts.visible;
		this.addNodeToArea();
	};

	// ROOT NODE ONLY:	control animation loop
	Node.prototype.animateToStatic = function () {

		clearTimeout(this.moveTimer);
		// stop the movement after a certain time
		var thisnode = this;
		this.moveTimer = setTimeout(function () {
			//stop the movement
			thisnode.obj.movementStopped = true;
		}, TIMEOUT * 1000);

		if (this.moving) {
			return;
		}
		this.moving = true;
		this.obj.movementStopped = false;
		this.animateLoop();
	};

	// ROOT NODE ONLY:	animate all nodes (calls itself recursively)
	Node.prototype.animateLoop = function () {
		var mynode = this,
			obj = this.obj;
		if (Area.is(":visible")) {
			var stable = this.findEquilibrium(); 
			obj.canvas.clear();
			this.drawLine();
			if (stable || obj.movementStopped) {
				this.moving = false;
				return;
			}
		}
		setTimeout(function () {
			mynode.animateLoop();
		}, 50);
	};

	// find the right position for this node
	Node.prototype.findEquilibrium = function () {
		var children = this.children, i, len, stable = true;
		stable = this.display() && stable;
		for (i = 0, len = children.length; i < len; i++) {
			stable = children[i].findEquilibrium() && stable;
		}
		return stable;
	};

	//Display this node, and its children
	Node.prototype.display = function (depth) {
		var stepAngle,
			angle,
			el  = this.el,
			options = this.options;
		
		depth = depth || 0;
		if (!this.visible) {
			return false;
		}
		// am I positioned?	If not, position me.
		if (!this.hasPosition) {
			this.x = options.mapArea.x / 2;
			this.y = options.mapArea.y / 2;
			el.css({'left': this.x + "px", 'top': this.y + "px"});
			this.hasPosition = true;
		}
		// are my children positioned?	if not, lay out my children around me
		if (this.children.length > 0) {
			stepAngle = Math.PI * 2 / this.children.length;
			for (var i=0, len=this.children.length; i<len; i++) {
				if (!this.children[i].hasPosition) {
					if (!options.showProgressive || depth <= 1) {
						angle = i * stepAngle;
						this.children[i].x = (50 * Math.cos(angle)) + this.x;
						this.children[i].y = (50 * Math.sin(angle)) + this.y;
						this.children[i].hasPosition = true;
						this.children[i].el.css({'left': this.children[i].x + "px", 'top': this.children[i].y + "px"});
					}
				}
			}
		}
		// update my position
		return this.updatePosition();
	};

	// updatePosition returns a boolean stating whether it's been static
	Node.prototype.updatePosition = function () {
		var forces, showx, showy,
			options = this.options,
			el = this.el,
			width_half = el.width() / 2,
			height_half = el.height() / 2;

		if (el.hasClass("ui-draggable-dragging")) {
			//var offset = el.position();
			//this.x = offset.left + width_half;
			//this.y = offset.top  + height_half;
			this.x = parseInt(el.css('left'), 10) + width_half;
			this.y = parseInt(el.css('top'), 10) + height_half;
			this.dx = 0;
			this.dy = 0;
			return false;
		}

		if (isNaN(this.x) || this.x < 0 || this.x > options.mapArea.x) {
			this.x = options.mapArea.x/2;
		}
		if (isNaN(this.y) || this.y < 0 || this.y > options.mapArea.y) {
			this.y = options.mapArea.y/2;
		}
		
		//apply accelerations
		forces = this.getForceVector();
		this.dx += forces.x * options.timeperiod;
		this.dy += forces.y * options.timeperiod;

		// damp the forces
		this.dx = this.dx * options.damping;
		this.dy = this.dy * options.damping;

		//ADD MINIMUM SPEEDS
		if (Math.abs(this.dx) < options.minSpeed) {
			this.dx = 0;
		}
		if (Math.abs(this.dy) < options.minSpeed) {
			this.dy = 0;
		}
		if (Math.abs(this.dx) + Math.abs(this.dy) === 0) {
			return true;
		}

		//apply velocity vector
		this.x += this.dx * options.timeperiod;
		this.y += this.dy * options.timeperiod;
		// round
		//this.x = Math.min(options.mapArea.x, Math.max(1, this.x));
		//this.y = Math.min(options.mapArea.y, Math.max(1, this.y));
		this.x = Math.min(options.mapArea.x, Math.max(width_half, this.x));
		this.y = Math.min(options.mapArea.y, Math.max(height_half, this.y));
		// display
		showx = this.x - width_half;
		showy = this.y - height_half - 10;
		el.css({'left': showx + "px", 'top': showy + "px"});
		return false;
	};

	Node.prototype.getForceVector = function () {
		var i, len, x1, y1, xsign, dist, theta, f,
			xdist, rightdist, bottomdist, otherend,
			fx = 0,
			fy = 0,
			options = this.options,
			activeNode = this.obj.activeNode,
			children = activeNode.children,
			parent = activeNode.parent;

		// Calculate the repulsive force from every other node
		for (i = -1, len = children.length; i <= len; i++) {
			otherend = null;
			if (i === -1) {
				otherend = activeNode;
			} else if (i < len) {
				otherend = children[i];
			} else if (parent !== null) {
				otherend = parent;
			}
			if (otherend === this || otherend === null || !otherend.visible) {
				continue;
			}
			// Repulsive force (coulomb's law)
			x1 = (otherend.x - this.x);
			y1 = (otherend.y - this.y);
			//adjust for variable node size
//		var nodewidths = (($(otherend[i]).width() + this.el.width())/2);
			dist = Math.sqrt((x1 * x1) + (y1 * y1));
//			var myrepulse = this.options.repulse;
//			if (this.parent==otherend[i]) myrepulse=myrepulse*10;	//parents stand further away
			if (Math.abs(dist) < 500 && dist !== 0) {
				if (x1 === 0) {
					theta = Math.PI / 2;
					xsign = 0;
				} else {
					theta = Math.atan(y1 / x1);
					xsign = x1 / Math.abs(x1);
				}
				// force is based on radial distance
				f = (options.repulse * 500) / (dist * dist);
				fx += -f * Math.cos(theta) * xsign;
				fy += -f * Math.sin(theta) * xsign;
			}
		}

		// add repulsive force of the "walls"
		//left wall
		xdist = this.x + this.el.width();
		f = (options.wallrepulse * 500) / (xdist * xdist);
		fx += Math.min(2, f);
		//right wall
		rightdist = (options.mapArea.x - xdist);
		f = -(options.wallrepulse * 500) / (rightdist * rightdist);
		fx += Math.max(-2, f);
		//top wall
		f = (options.wallrepulse * 500) / (this.y * this.y);
		fy += Math.min(2, f);
		//bottom wall
		bottomdist = (options.mapArea.y - this.y);
		f = -(options.wallrepulse * 500) / (bottomdist * bottomdist);
		fy += Math.max(-2, f);

		// for each line, of which I'm a part, add an attractive force.
		for (i = 0, len = children.length; i <= len; i++) {
			otherend = null;
			if (activeNode === this) {
				if (i != len) otherend = children[i];
				else if (parent !== null) otherend = parent;
				else continue;
			} else if (i != len && children[i] === this || i == len && parent == this) {
				otherend = activeNode;
			} else {
				continue;
			}
			// Ignore the pull of hidden nodes
			if (!otherend.visible || !otherend.hasPosition) {
				continue;
			}
			// Attractive force (hooke's law)
			x1 = (otherend.x - this.x);
			y1 = (otherend.y - this.y);
			dist = Math.sqrt((x1 * x1) + (y1 * y1));
			if (Math.abs(dist) > 0) {
				if (x1 === 0) {
					theta = Math.PI / 2;
					xsign = 0;
				}
				else {
					theta = Math.atan(y1 / x1);
					xsign = x1 / Math.abs(x1);
				}
				// force is based on radial distance
				f = (options.attract * dist) / 10000;
				fx += f * Math.cos(theta) * xsign;
				fy += f * Math.sin(theta) * xsign;
			}
		}

		// if I'm active, attract me to the centre of the area
		if (activeNode === this) {
			// Attractive force (hooke's law)
			otherend = options.mapArea;
			x1 = ((otherend.x / 2) - options.centreOffset - this.x);
			y1 = ((otherend.y / 2) - this.y);
			dist = Math.sqrt((x1 * x1) + (y1 * y1));
			if (Math.abs(dist) > 0) {
				if (x1 === 0) {
					theta = Math.PI / 2;
					xsign = 0;
				} else {
					xsign = x1 / Math.abs(x1);
					theta = Math.atan(y1 / x1);
				}
				// force is based on radial distance
				f = (0.1 * options.attract * dist * CENTRE_FORCE) / 1000;
				fx += f * Math.cos(theta) * xsign;
				fy += f * Math.sin(theta) * xsign;
			}
		}

		if (Math.abs(fx) > options.maxForce) {
			fx = options.maxForce * (fx / Math.abs(fx));
		}
		if (Math.abs(fy) > options.maxForce) {
			fy = options.maxForce * (fy / Math.abs(fy));
		}
		return {
			x: fx,
			y: fy
		};
	};
	
	Node.prototype.drawLine = function () {
		var activeNode = this.obj.activeNode;
		
		if (isNaN(activeNode.x) || isNaN(activeNode.y)) return ;
		
		var children = activeNode.children,
			_parent = activeNode.parent,
			start = "M" + activeNode.x + ' ' + activeNode.y + "L",
			option = {'stroke': this.options.lineColor, 'opacity': this.options.lineOpacity, 'stroke-width': this.options.lineWidth};
		
		if (_parent !== null) {
			if (_parent.visible && !isNaN(_parent.x) && !isNaN(_parent.y)) {
				this.obj.canvas.path(start + _parent.x + ' ' + _parent.y).attr(option);
			}
		}
		
		for (var i=0, len=children.length; i<len; i++) {
			if (children[i].visible && !isNaN(children[i].x) && !isNaN(children[i].y)) {
				this.obj.canvas.path(start + children[i].x + ' ' + children[i].y).attr(option);
			}
		}
	};
	
	Node.prototype.removeNode = function () {
		var i, len,
			oldnodes = this.obj.nodes,
			children = this.children;

		for (i=0, len=children.length; i < len; i++) {
			children[i].removeNode();
		}

		this.obj.nodes = new Array;
		for (i=0, len=oldnodes.length; i < len; i++) {
			if (oldnodes[i] === this) {
				continue;
			}
			this.obj.nodes.push(oldnodes[i]);
		}

		this.el.remove();
	};

	Node.prototype.removeActiveNode = function () {
		var node = this,
			nodes = this.obj.nodes;

		// hide all elements
		for (var i=0,len=nodes.length; i<len; i++) {
			nodes[i].visible = false;
		}
		node.visible = true;

		// remove all elements
		var pos_node = node.el.position();
		$("a", Area).each(function(){
			var el = $(this);
			var pos_el = el.position();
			if (node.el.text() !== el.text()
			 || pos_el.left !== pos_node.left || pos_el.top !== pos_node.top) {
				el.remove();
			}
		});
	};
	
	Node.prototype.addNodeToArea = function () {
		var thisnode = this,
			obj = this.obj;
		
		// hide or show
		if (this.visible) this.el.show();
		else this.el.hide();
		
		this.el.appendTo(Area)
		.draggable({
			drag: function () {
				obj.root.animateToStatic();
			}
		})
		.click(function () {
			if (typeof thisnode.onclick === 'function') {
				thisnode.onclick();
			}
			var activeNode = obj.activeNode;
			if (activeNode) {
				activeNode.el.removeClass('active');
				if (activeNode.parent) {
					activeNode.parent.el.removeClass('activeparent');
				}
			}
			thisnode.el.addClass('active');
			if (thisnode.parent) {
				thisnode.parent.el.addClass('activeparent');
			}
			//thisnode.x = obj.options.mapArea.x / 2;
			//thisnode.y = obj.options.mapArea.y / 2;
			obj.activeNode = thisnode;
			obj.root.animateToStatic();
			return false;
		});;
		
	};


	$.fn.addNode = function (parent, name, options) {
		var obj = this[0],
			node = obj.nodes[obj.nodes.length] = new Node(obj, name, parent, options);
		console.log(obj.root);
		obj.root.animateToStatic();
		return node;
	};

	$.fn.addRootNode = function (name, opts) {
		var node = this[0].nodes[0] = new Node(this[0], name, null, opts);
		this[0].root = node;
		return node;
	};

	$.fn.removeNode = function (name) {
		return this.each(function () {
//			if (!!this.mindmapInit) return false;
			//remove a node matching the anme
//			alert(name+' removed');
		});
	};

	$.fn.mindmap = function (options) {
		// Define default settings.
		options = $.extend({
			attract: 15,
			repulse: 6,
			damping: 0.55,
			timeperiod: 10,
			wallrepulse: 0.4,
			mapArea: {
				x: -1,
				y: -1
			},
			canvasError: 'alert',
			minSpeed: 0.05,
			maxForce: 0.1,
			updateIterationCount: 20,
			showProgressive: true,
			centreOffset: 100,
			timer: 0,
			showSublines: false,
			lineColor: "#FFF",
			lineOpacity: "0.2",
			lineWidth: "5px"
		}, options);

		Area = this;
		var offset = Area.position();

		return this.each(function () {
			var mindmap = this;

			this.mindmapInit = true;
			this.nodes = new Array;
			this.activeNode = null;
			this.options = options;
			this.animateToStatic = function () {
				this.root.animateToStatic();
			};
			Area.resize(function () {
				mindmap.animateToStatic();
			});

			//canvas
			if (options.mapArea.x === -1) {
				options.mapArea.x = Area.width();
			}
			if (options.mapArea.y === -1) {
				options.mapArea.y = Area.height();
			}
			//create drawing area
			this.canvas = Raphael(offset.left, offset.top, options.mapArea.x, options.mapArea.y);

			// Add a class to the object, so that styles can be applied
			$(this).addClass('js-mindmap-active');

			// Add keyboard support (thanks to wadefs)
			$(this).keyup(function (event) {
				var newNode, activeParent = mindmap.activeNode.parent,
					i, len = activeParent.children.length;
				switch (event.which) {
				case 33: // PgUp
				case 38: // Up, move to parent
					if (activeParent) {
						activeParent.el.click();
					}
					break;
				case 13: // Enter (change to insert a sibling)
				case 34: // PgDn
				case 40: // Down, move to first child
					if (mindmap.activeNode.children.length) {
						mindmap.activeNode.children[0].el.click();
					}
					break;
				case 37: // Left, move to previous sibling
					if (activeParent) {
						newNode = null;
						if (activeParent.children[0] === mindmap.activeNode) {
							newNode = activeParent.children[len - 1];
						} else {
							for (i = 1; i < len; i++) {
								if (activeParent.children[i] === mindmap.activeNode) {
									newNode = activeParent.children[i - 1];
								}
							}
						}
						if (newNode) {
							newNode.el.click();
						}
					}
					break;
				case 39: // Right, move to next sibling
					if (activeParent) {
						newNode = null;
						if (activeParent.children[len - 1] === mindmap.activeNode) {
							newNode = activeParent.children[0];
						} else {
							for (i = len - 2; i >= 0; i--) {
								if (activeParent.children[i] === mindmap.activeNode) {
									newNode = activeParent.children[i + 1];
								}
							}
						}
						if (newNode) {
							newNode.el.click();
						}
					}
					break;
				case 45: // Ins, insert a child
					break;
				case 46: // Del, delete this node
					break;
				case 27: // Esc, cancel insert
					break;
				case 83: // 'S', save
					break;
				}
				return false;
			});

		});
	};
}(jQuery));

/*jslint devel: true, browser: true, continue: true, plusplus: true, indent: 2 */