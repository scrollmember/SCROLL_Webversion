/**
 * Copyright © Alexis Jacomy, 2012
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * The Software is provided "as is", without warranty of any kind, express or
 * implied, including but not limited to the warranties of merchantability,
 * fitness for a particular purpose and noninfringement. In no event shall the
 * authors or copyright holders be liable for any claim, damages or other
 * liability, whether in an action of contract, tort or otherwise, arising
 * from, out of or in connection with the software or the use or other dealings
 * in the Software.
 */

(function() {
  'use strict';

  // Fallback in case there is no internet connection:
  var local = false;

  $(document).ready(function() {
    // Circular plugin:
    sigma.publicPrototype.circularize = function() {
      var R = 100, i = 0, L = this.getNodesCount();
   
      this.iterNodes(function(n){
        n.x = Math.cos(Math.PI*(i++)/L)*R;
        n.y = Math.sin(Math.PI*(i++)/L)*R;
      });
   
      return this.position(0,0,1).draw();
    };

    // Instanciate sigma.js:

    var s1 = sigma.init($('.sigma-container')[0]).drawingProperties({
		defaultLabelColor : '#ccc',
		defaultLabelSize : 14,
		defaultLabelBGColor : '#fff',
		defaultLabelHoverColor : '#000',
		labelThreshold : 6,
		defaultEdgeType : 'curve',
		defaultEdgeArrow: 'target',
		edgeLabels : true
	}).graphProperties({
		minNodeSize : 0.5,
		maxNodeSize : 20,
		minEdgeSize : 1,
		maxEdgeSize : 1
	}).mouseProperties({
		maxRatio : 8
	});
	
	s1.parseGexf("http://localhost:8080/learninglog/js/networkanalysis/kpt.gexf");
    // Tweak:
    // Give focus to sigma-container when sigma is clicked:
    $('#sigma_mouse_1').click(function(){
      $('.sigma-container').focus();
    });



    $('form[name="post-url-form"]').submit(function(e){
      if(local)
        reddit.localPageComments('http://localhost:8080/learninglog/js/networkanalysis/data_sample.json');
      else
        reddit.pageComments($(this).find('input[type="text"]').attr('value'));

      e.stopPropagation();
      e.preventDefault();
      return false;
    });

    $('.contains-icon').mouseover(function() {
      $(this).find('.icon-button').addClass('icon-white');
    }).mouseout(function() {
      $(this).find('.icon-button').removeClass('icon-white');
    });

    // Init first loading:
    if(local)
      reddit.localPageComments('data_sample.json');
    else
      reddit.pageComments($(this).find('input[type="text"]').attr('value'));

    /**
     * NAVIGATION:
     */
    var moveDelay = 80,
        zoomDelay = 2;

    $('.move-icon').bind('click keypress',function(event) {
      var newPos = s1.position();
      switch ($(this).attr('action')) {
        case 'up':
          newPos.stageY += moveDelay;
          break;
        case 'down':
          newPos.stageY -= moveDelay;
          break;
        case 'left':
          newPos.stageX += moveDelay;
          break;
        case 'right':
          newPos.stageX -= moveDelay;
          break;
      }

      s1.goTo(newPos.stageX, newPos.stageY);

      event.stopPropagation();
      return false;
    });

    $('.zoom-icon').bind('click keypress',function(event) {
      var ratio = s1.position().ratio;
      switch ($(this).attr('action')) {
        case 'in':
          ratio *= zoomDelay;
          break;
        case 'out':
          ratio /= zoomDelay;
          break;
      }

      s1.goTo(
        $('.sigma-container').width() / 2,
        $('.sigma-container').height() / 2,
        ratio
      );

      event.stopPropagation();
      return false;
    });

    $('.refresh-icon').bind('click keypress',function(event) {
      s1.position(0, 0, 1).draw();

      event.stopPropagation();
      return false;
    });

    $('.sigma-container').keydown(function(e) {
      var newPos = s1.position(),
          change = false;
      newPos.ratio = undefined;

      switch (e.keyCode) {
        case 32:
          s1.position(0, 0, 1).draw();
          e.stopPropagation();
          return false;
        case 38:
        case 75:
          newPos.stageY += moveDelay;
          change = true;
          break;
        case 40:
        case 74:
          newPos.stageY -= moveDelay;
          change = true;
          break;
        case 37:
        case 72:
          newPos.stageX += moveDelay;
          change = true;
          break;
        case 39:
        case 76:
          newPos.stageX -= moveDelay;
          change = true;
          break;
        case 107:
          newPos.ratio = s1.position().ratio * zoomDelay;
          newPos.stageX = $('.sigma-container').width() / 2;
          newPos.stageY = $('.sigma-container').height() / 2;
          change = true;
          break;
        case 109:
          newPos.ratio = s1.position().ratio / zoomDelay;
          newPos.stageX = $('.sigma-container').width() / 2;
          newPos.stageY = $('.sigma-container').height() / 2;
          change = true;
          break;
      }

      if(change) {
        s1.goTo(newPos.stageX, newPos.stageY, newPos.ratio);
        e.stopPropagation();
        return false;
      }
    }).focus(function(){
      s1.stopForceAtlas2();
    }).blur(function(){
      s1.startForceAtlas2();
    });

    /**
     * OTHER
     */
    function onAction() {
      // Make all nodes unactive:
      s1.iterNodes(function(n) {
        n.active = false;
      });
    }

    // Autocompleted search field:
    $('form.search-nodes-form').submit(function(e) {
      onAction();
      e.preventDefault();
    });

    // Node information:
    function loadRedditUser(node) {
      hideTwitterUser();

      if(node['label'] !== '[deleted]')
        reddit.user(node['label']);
      else
        showRedditUser();
    }

    function showRedditUser(obj) {
      hideTwitterUser();

      if(obj){
        // Name :
        $('div.node-info-container .node-name').append(
          '<h3>' +
            '<a target="_blank" href="' +
              'http://www.reddit.com/user/' + obj['name'] +
            '">' +
            obj['name'] +
            '</a>' +
          '</h3>'
        );

        // Link Karma :
        $('div.node-info-container .node-link-karma').append(
          'Link karma: ' + obj['link_karma']
        );

        // Comments Karma :
        $('div.node-info-container .node-comments-karma').append(
          'Comments karma: ' + obj['comment_karma']
        );
      }else{
        $('div.node-info-container .node-name').append(
          '<h3>' +
            'Oops, the requested user has been deleted.' +
          '</h3>'
        );
      }
    }

    function hideTwitterUser() {
      $('div.node-info-container .node-info').empty();
    }

    s1.bind(
			'overnodes',
			function(event) {
				var nodes = event.content;
				var neighbors = {};
				s1
						.iterEdges(
								function(e) {
									if (nodes.indexOf(e.source) < 0
											&& nodes
													.indexOf(e.target) < 0) {
										if (!e.attr['grey']) {
											e.attr['true_color'] = e.color;
											e.color = greyColor;
											e.attr['grey'] = 1;
										}
									} else {
										e.color = e.attr['grey'] ? e.attr['true_color']
												: e.color;
										e.attr['grey'] = 0;

										neighbors[e.source] = 1;
										neighbors[e.target] = 1;
									}
								})
						.iterNodes(
								function(n) {
									if (!neighbors[n.id]) {
										if (!n.attr['grey']) {
											n.attr['true_color'] = n.color;
											n.color = greyColor;
											n.attr['grey'] = 1;
										}
									} else {
										n.color = n.attr['grey'] ? n.attr['true_color']
												: n.color;
										n.attr['grey'] = 0;
									}
								}).draw(2, 2, 2);

			})
	.bind(
			'outnodes',
			function() {
				s1
						.iterEdges(
								function(e) {
									e.color = e.attr['grey'] ? e.attr['true_color']
											: e.color;
									e.attr['grey'] = 0;
								})
						.iterNodes(
								function(n) {
									n.color = n.attr['grey'] ? n.attr['true_color']
											: n.color;
									n.attr['grey'] = 0;
								}).draw(2, 2, 2);
			});



s1.bind('downnodes', function(event) {
s1.iterNodes(
		function(n) {
			var theMapZoom = 6;
			
			if(n.id==event.content[0]){
			//alert(n.attr.attributes[4].val);
			
			for(var i=0;i<n.attr.attributes.length;i++){
				if(n.attr.attributes[i].attr=="Authorid"){
					var tm;

					var nodeurl = "${baseURL}/analysis/show?format=json&username="
							+ n.attr.attributes[i].val;

					tm = TimeMap.init({
						mapId : "map", // Id of map div element (required)
						timelineId : "timeline", // Id of timeline div element (required)
						options : {
							eventIconPath : "${baseURL}/js/timemap/images/",
							centerOnItems : false,
							mapCenter : theMapCenter,
							mapZoom : theMapZoom

						},
						datasets : [ {
							id : "llog",
							title : "Llog",
							theme : "red",
							type : "json_string",
							options : {
								url : nodeurl
							}
						} ],
						bandIntervals : [ Timeline.DateTime.WEEK,
								Timeline.DateTime.MONTH ]
					});
					
					// add our new function to the map and timeline filters
					//tm.addFilter("map", TimeMap.filters.hasSelectedTag); // hide map markers on fail
					//tm.addFilter("timeline", TimeMap.filters.hasSelectedTag); // hide timeline events on fail
					for(var i=0;i<n.attr.attributes.length;i++){
						if(n.attr.attributes[i].attr=="createtime"){
					var nodestate = n.attr.attributes[i].val;
					var timedata = nodestate.split("/");
					obj = new Date();

					obj.setFullYear(timedata[0]);

					obj.setMonth(timedata[1]);

					//obj.setDate(timedata[2]);

				
					tm.scrollToDate(obj, false, true);
						}
					}
				}
				}
			}
			
		}
		
);

});
  });
})();
