var uploadConfirm;

uploadConfirm = function(params) {
  var buttonHTML, buttons, confirmOverlay, i, markup;
  buttonHTML = "";
  $.each(params.buttons, function(name, obj) {
    buttonHTML += "<a href=\"#\" class=\"button " + obj['class'] + "\">" + name + "<span></span></a>";
    return obj.action || (obj.action = function() {});
  });
  confirmOverlay = "confirmOverlay" + params.id;
  markup = ("<div id=\"" + confirmOverlay + "\" class=\"confirmOverlay\">") + '<div id="confirmBox" class="hero-unit">' + '<h6>' + params.title + '</h6>' + '<p><small>' + params.message + '</small></p>' + '<div id="confirmButtons">' + buttonHTML + '</div></div></div>';
  $(markup).hide(confirmOverlay).appendTo("body");
  buttons = $('#confirmBox').find('.button');
  i = 0;
  return $.each(params.buttons, function(name, obj) {
    return buttons.eq(i++).click(function() {
      obj.action();
      $("#" + confirmOverlay).remove();
      return false;
    });
  });
};