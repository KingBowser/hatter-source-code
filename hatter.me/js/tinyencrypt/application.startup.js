
function application_body_onload()
{
  // body onload code
}

function application_body_onunload()
{
  // body onunload code
}

// coding here

function wrapBeginEnd(txt) {
  return "-----BEGIN ENCRYPT-----\n" + txt + "\n-----END ENCRYPT-----";
}

function unwrapBeginEnd(txt) {
  txt = StringUtil.trim(txt);
  if (StringUtil.startWith(txt, "-----BEGIN ENCRYPT-----")) {
    txt = txt.substring("-----BEGIN ENCRYPT-----".length);
  }
  if (StringUtil.endWith(txt, "-----END ENCRYPT-----")) {
    txt = txt.substring(0, txt.length - "-----END ENCRYPT-----".length);
  }
  return StringUtil.trim(txt);
}

function encrypt()
{
  if ($value("input_password") != $value("input_password2"))
  {
    //alert("Two passwords not match!");
    var alarm = new Alarm(Alarm.ALARM_TYPE_MESSAGE | Alarm.ALARM_TYPE_BLINKCOLOR);
    alarm.setShowTime(3);
    alarm.setCancelLastMessage(true);
    alarm.setMessageAdapter(function(msg)
    {
    });
    alarm.setBlinkAdapter(function(msg)
    {
      if (msg == Alarm.ALARM_BLINK_ON)
      {
        $("input_password").style.background = "yellow";
        $("input_password2").style.background = "yellow";
      }
      else if (msg == Alarm.ALARM_BLINK_OFF)
      {
        $("input_password").style.background = "#ffffff";
        $("input_password2").style.background = "#ffffff";
      }
    });
    
    alarm.show("Two passwords not match!");
    return;
  }
  var aes = new AES();
  $value("text_output", wrapBeginEnd(aes.encrypt($value("input_password"), $value("text_input"))));
}

function decrypt()
{
  var aes = new AES();
  $value("text_input", aes.decrypt($value("input_password"), unwrapBeginEnd($value("text_output"))));
}
