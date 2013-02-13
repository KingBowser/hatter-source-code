
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
    alert("Two passwords not match!");
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
