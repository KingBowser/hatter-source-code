
function application_body_onload() {
  // body onload code
}

function application_body_onunload() {
  // body onunload code
}

// coding here

function merge() {
  var text_template = StringUtil.trim($value("text_template"));
  var text_vars = StringUtil.trim($value("text_vars"));

  if (StringUtil.isEmpty(text_template)) {
    alarmMsg("text_template");
    return;
  }
  if (!StringUtil.contains(text_template, "$VAR$")) {
    alarmMsg("text_template");
    return;
  }
  if (StringUtil.isEmpty(text_vars)) {
    alarmMsg("text_vars");
    return;
  }
  var vars = text_vars.split(/(?:\r\n)|(?:\r)|(?:\n)/g);
  var noneEmptyVars = [];
  for (var i = 0; i < vars.length; i++) {
    var l = StringUtil.trim(vars[i]);
    if (StringUtil.isNotEmpty(l)) {
      noneEmptyVars.push(l);
    }
  }
  if (noneEmptyVars.length == 0) {
    alarmMsg("text_vars");
    return;
  }
  
  var results = [];
  for (var i = 0; i < noneEmptyVars.length; i++) {
    results.push(text_template.replace(/\$VAR\$/g, noneEmptyVars[i]));
  }

  $value("text_output", results.join("\n"));
}

function alarmMsg(id) {
  var alarm = new Alarm(Alarm.ALARM_TYPE_MESSAGE | Alarm.ALARM_TYPE_BLINKCOLOR);
  alarm.setShowTime(3);
  alarm.setCancelLastMessage(true);
  alarm.setMessageAdapter(function(msg) {
  });
  alarm.setBlinkAdapter(function(msg) {
    if (msg == Alarm.ALARM_BLINK_ON) {
      $(id).style.background = "yellow";
    } else if (msg == Alarm.ALARM_BLINK_OFF) {
      $(id).style.background = "#ffffff";
    }
  });
  
  alarm.show("Imput not illegal!");
}
