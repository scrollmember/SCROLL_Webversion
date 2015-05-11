$(document).ready(function() {
	// json url
	var url = "/learninglog/wordmap/";// [word]?format=json
	// word data
	var data = {};
	// Root Node
	var root = null;
	// Root Word
	var root_word = $("#wordmap_btn").attr("title").toLowerCase();
	if (!root_word) return ;
	
	// view setting
	var visible_cunt = 25;
	var visible_type = "";// all
	var visible_list = ["", "hype", "syno", "hypo", "img", "pos", "time"];
	
	// create wordmap area
	var wordmap = $("<div/>").appendTo("body").attr("id", "WORDMAP");
	// create loading area
	var loading = $("<div/>").appendTo(wordmap).attr("id", "wm_loading").text("loading...");
	// create maparea and enable the wordmap
	var wordarea = $("<div/>").appendTo(wordmap).attr("id", "wordarea")
	.mindmap({
		attract: 10,
		repulse: 3,
		damping: 0.65,
		timeperiod: 10,
		wallrepulse: 0.4,
		mapArea: {
			x: -1,
			y: -1
		},
		canvasError: 'alert',
		minSpeed: 0.05,
		maxForce: 0.10,
		updateIterationCount: 20,
		showProgressive: true,
		centreOffset: 0,
		timer: 0,
		showSublines: false,
		lineColor: "#00F",
		lineOpacity: "0.7",
		lineWidth: "3px"
	});
	var svg = wordmap.next().appendTo(wordmap);// add svg

	// toggle hide/show
	var toggleArea = function(){
		if (wordmap.is(":visible")) {
			wordmap.hide();
			wordarea.hide();
			svg.hide();
		} else {
			wordmap.show();
			wordarea.show();
			svg.show();
			setTimeout(function(){if (root) { root.animateToStatic(); } }, 100);// move
		}
	};
	// hide area
	toggleArea();
	
	
	var topbar = $("<div/>").appendTo(wordmap).attr("id", "wm_topbar");
	// close button
	$("<div/>").text(" × ").appendTo(topbar).attr("id", "wm_close_btn").click(toggleArea);
	// open
	$("#wordmap_btn").click(toggleArea);
	
	
	
	
	/***************
	 * functions
	 ***************/
	Array.prototype.shuffle = function() {
		var i = this.length;
		while(i){
			var j = Math.floor(Math.random()*i);
			var t = this[--i];
			this[i] = this[j];
			this[j] = t;
		}
		return this;
	};
	
	
	// check visible node
	var isVisibleNode = function(class_name) {
		if (visible_type === "" || class_name === "wm_" + visible_type) return true;
		return false;
	};

	
	// change Visible Node
	var changeVisibleNode = function(visible_name) {
		visible_type = visible_name;
		var count = 0;
		var node = root.obj.activeNode;
		node.children.shuffle();
		$.each(node.children, function() {
			this.visible = (isVisibleNode(this.class_name) && count++ < visible_cunt);
			if (this.visible) this.el.show();
			else this.el.hide();
		});
		$("a", topbar).each(function(){
			if ($(this).text() === "all" && visible_name === "" || visible_name === $(this).text()) {
				$(this).removeClass("wm_unselected");
				$(this).addClass("wm_selected");
			} else {
				$(this).removeClass("wm_selected");
				$(this).addClass("wm_unselected");
			}
			
		});
		setTimeout(function(){if (root) { root.animateToStatic(); } }, 100);// move
	};
	
	var addChildrenToArea = function(node) {
		var count = 0;
		node.children.shuffle();
		$.each(node.children, function() {
			this.visible = (isVisibleNode(this.class_name) && count++ < visible_cunt);
			this.addNodeToArea();
		});
	};
	
	var createRoot = function(){
		// add rootnode
		root = wordarea.addRootNode(root_word, {
			class_name: "",
			href: '#',
			url: '',
			visible: true,
			onclick: function() {
				var node = this;
				if (node.obj.activeNode == node) {
					return false;
				}
				// Remove Active Node
				node.removeActiveNode();
				// add ChildrenNode
				addChildrenToArea(node);
				return false;
			}
		});
	};
	
	var addChildren = function(parentnode, parent) {
		var count = 0;
		
		// ランダム化
		parent.children.shuffle();
		
		$.each(parent.children, function(i, val) {
			
			// initialize
			if (data[val.word] === void 0) data[val.word] = {};
			var wdata = data[val.word];
			if (wdata.children === void 0) wdata.children = new Array;
			
			// parent.parent == this OR parent == this
			if (parentnode.parent && parentnode.parent.name == val.word
			 || parentnode.name == val.word) {
				return;
			}
			
			wordarea.addNode(parentnode, val.word, {
				class_name: "wm_" + val.type + ((val.flag==="1")?" wm_learned":""),
				href: "#",
				visible: (isVisibleNode("wm_" + val.type) && count++ < visible_cunt),
				onclick: function() {
					var node = this;
					if (this.obj.activeNode === this) {
						return false;
					}
					
					// Remove Active Node
					this.removeActiveNode();
					// add ParentNode
					this.parent.visible = true;
					this.parent.addNodeToArea();
					// add ChildrenNode
					if (this.children.length > 0) {
						addChildrenToArea(this);
					} else if (wdata.children.length > 0)  {
						addChildren(node, wdata);
					} else {
						loading.show();
						$.getJSON(url + val.word.replace(" ", "_") + "?format=json", function(json){
							wdata.children = json[val.word].children;
							addChildren(node, wdata);
							loading.hide();
						});
					}
					
					return false;
				}
			});
		});
	};
	
	
	$.each(visible_list, function(i, val){
		$("<a/>")
		.text(((val === "") ? "all" : val))
		.addClass("node"+ ((val === "") ? " wm_selected" : " wm_unselected wm_" + val) )
		.click(function(){ changeVisibleNode(val); })
		.appendTo(topbar);
	});
	
	
	/***************
	 * MAIN
	 ***************/
	
	// get root_word data
	loading.show();
	$.getJSON(url + root_word.replace(" ", "_") + "?format=json", function(json){
		data = json;
		createRoot();
		
		// add children
		addChildren(root, data[root_word]);
		loading.hide();
	});
	
	

});
